package org.csu.herbinfo.service.impl;

import ch.hsr.geohash.GeoHash;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.csu.herbinfo.DTO.HerbLocationDTO;
import org.csu.herbinfo.DTO.Location;
import org.csu.herbinfo.VO.HerbLocationVO;
import org.csu.herbinfo.entity.GisHerbLocation;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.mapper.GisHerbLocationPGSqlMapper;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.GisHerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class GisHerbLocationServiceImpl implements GisHerbLocationService {
    @Autowired
    GisHerbLocationPGSqlMapper gisHerbLocationMapper;
    @Autowired
    DistrictStreetService districtStreetService;
    @Autowired
    HerbService herbService;

    private final RedisTemplate<String, Object> redisTemplate;
    private final String NEARBY_LOCATION_KEY = "location:nearby";

    // 本地缓存配置
    private final Cache<String, List<GisHerbLocation>> localCache =
            Caffeine.newBuilder()
                    .expireAfterWrite(5, TimeUnit.MINUTES)  // 本地缓存5分钟
                    .maximumSize(1000)                      // 最大1000条
                    .build();

    GisHerbLocationServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private boolean isHerbLocationValid(GisHerbLocation herbLocation) {
        if(herbLocation==null){
            return false;
        }

        int districtId = herbLocation.getDistrictId();
        if(!districtStreetService.isDistrictExist(districtId)){
            return false;
        }

        int streetId = herbLocation.getStreetId();
        if(!districtStreetService.isStreetInDistrict(districtId, streetId)){
            return false;
        }

        if(herbLocation.getCount()==0) return false;
        return true;
    }

    @Override
    public List<GisHerbLocation> getAllHerbLocations() {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        list.addAll(gisHerbLocationMapper.selectList(null));
        return list;
    }

    @Override
    public GisHerbLocation getHerbLocationById(int id) {
        GisHerbLocation gisHerbLocation = gisHerbLocationMapper.selectById(id);
        return gisHerbLocation;
    }

    @Override
    public List<GisHerbLocation> getHerbLocationsByHerbId(int herbId) {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("herb_id", herbId);
        list.addAll(gisHerbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public List<GisHerbLocation> getHerbLocationsByDistrictName(String district) {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        int district_id = districtStreetService.getDistrictIdByName(district);
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", district_id);
        list.addAll(gisHerbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public int getHerbCountsByDistrictName(String district) {
        int count = 0;
        int district_id = districtStreetService.getDistrictIdByName(district);
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", district_id);
        List<GisHerbLocation> list = gisHerbLocationMapper.selectList(queryWrapper);
        for(GisHerbLocation gisHerbLocation : list){
            count+=gisHerbLocation.getCount();
        }
        return count;
    }

    @Override
    public List<GisHerbLocation> getHerbLocationsByStreetName(String street) {
        ArrayList<GisHerbLocation> list = new ArrayList<>();
        int street_id = districtStreetService.getStreetIdByName(street);
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("street_id", street_id);
        list.addAll(gisHerbLocationMapper.selectList(queryWrapper));
        return list;
    }

    @Override
    public int addHerbLocation(GisHerbLocation herbLocation) {
        if(!isHerbLocationValid(herbLocation)){
            return -1;
        }
        int ori_id = isHerbLocationInfoExists(herbLocation);
        if(ori_id!=-1){
            GisHerbLocation ori = gisHerbLocationMapper.selectById(ori_id);
            ori.setCount(ori.getCount()+herbLocation.getCount());
            gisHerbLocationMapper.updateById(ori);
            return ori_id;
        }
        int id = gisHerbLocationMapper.insert(herbLocation);
        clearNearbyCacheForLocation(herbLocation);  //清除附近缓存
        return id;
    }

    @Override
    public boolean updateHerbLocation(GisHerbLocation herbLocation) {
        if(!isHerbLocationValid(herbLocation)){
            return false;
        }
        GisHerbLocation ori = gisHerbLocationMapper.selectById(herbLocation.getId());
        clearNearbyCacheForLocation(ori);   //清除旧位置的缓存
        gisHerbLocationMapper.updateById(herbLocation);
        clearNearbyCacheForLocation(herbLocation);  //清除新位置缓存
        return true;
    }

    @Override
    public int isHerbLocationInfoExists(GisHerbLocation herbLocation) {
        GisHerbLocation flag;
        QueryWrapper<GisHerbLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("district_id", herbLocation.getDistrictId())
                .eq("street_id", herbLocation.getStreetId())
                .eq("geom", herbLocation.getGeom());
        flag = gisHerbLocationMapper.selectOne(queryWrapper);
        if(flag==null){
            return -1;
        }
        return Math.toIntExact(flag.getId());
    }

    @Override
    public GisHerbLocation getHerbLocationByLocationInfo(GisHerbLocation herbLocation) {
        int location_id = isHerbLocationInfoExists(herbLocation);
        if(location_id==-1){
            return null;
        }
        return gisHerbLocationMapper.selectById(location_id);
    }

    @Override
    public boolean deleteHerbLocation(int herbLocationId) {
        if(!isHerbLocationExist(herbLocationId)){
            return false;
        }
        GisHerbLocation ori = gisHerbLocationMapper.selectById(herbLocationId);
        clearNearbyCacheForLocation(ori);   //清楚旧位置附近的缓存
        gisHerbLocationMapper.deleteById(herbLocationId);
        return true;
    }

    @Override
    public boolean isHerbLocationExist(int herbLocationId) {
        if(gisHerbLocationMapper.selectById(herbLocationId)==null){
            return false;
        }
        return true;
    }

    @Override
    public GisHerbLocation transferDTOToGisHerbLocation(HerbLocationDTO herbLocationDTO) {
        GisHerbLocation herbLocation = new GisHerbLocation();

        int herbId = herbService.getHerbIdByName(herbLocationDTO.getName());
        herbLocation.setHerbId(herbId);

        herbLocation.setCount(herbLocationDTO.getCount());

        int district_id = districtStreetService.getDistrictIdByName(herbLocationDTO.getDistrict());
        herbLocation.setDistrictId(district_id);

        int street_id = districtStreetService.getStreetIdByName(herbLocationDTO.getStreet());
        herbLocation.setStreetId(street_id);

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(herbLocationDTO.getLongitude(),herbLocationDTO.getLatitude()));
        herbLocation.setGeom(point);

        return herbLocation;
    }

    @Override
    public HerbLocationVO transferHerbLocationToVO(GisHerbLocation herbLocation) {
        HerbLocationVO herbLocationVO = new HerbLocationVO();
        herbLocationVO.setId(Math.toIntExact(herbLocation.getId()));
        herbLocationVO.setHerbId(herbLocation.getHerbId());
        herbLocationVO.setCount(herbLocation.getCount());
        herbLocationVO.setDistrictId(herbLocation.getDistrictId());
        herbLocationVO.setStreetId(herbLocation.getStreetId());
        herbLocationVO.setLatitude(herbLocation.getGeom().getCoordinate().getY());
        herbLocationVO.setLongitude(herbLocation.getGeom().getCoordinate().getX());

        herbLocationVO.setHerbName(herbService.getHerbById(herbLocation.getHerbId()).getName());
        herbLocationVO.setDistrictName(districtStreetService.getDistrictById(herbLocation.getDistrictId()).getName());
        herbLocationVO.setStreetName(districtStreetService.getStreetById(herbLocation.getStreetId()).getName());
        return herbLocationVO;
    }

    @Override
    public List<HerbLocationVO> transferHerbLocationListToVOList(List<GisHerbLocation> herbLocationList) {
        List<HerbLocationVO> herbLocationVOList = new ArrayList<>();
        for(GisHerbLocation herbLocation : herbLocationList){
            herbLocationVOList.add(transferHerbLocationToVO(herbLocation));
        }
        return herbLocationVOList;
    }

//----------------------------------------NEARBY LOCATIONS--------------------------------------------------------------

    @Override
    public List<GisHerbLocation> findNearByHerbLocations(Location location) {
        return findNearByHerbLocations(location,5000);
    }

    @Override
    public List<GisHerbLocation> findNearByHerbLocations(Location location, double radius) {
        String cacheKey = generateNearbyCacheKey(location, radius);
        List<GisHerbLocation> result;

        //先查找本地缓存
        result = localCache.getIfPresent( cacheKey );
        if(result!=null){
            //System.out.println("use local cache");
            return result;
        }

        //查找Redis
        result = (ArrayList<GisHerbLocation>) redisTemplate.opsForValue().get(cacheKey);
        if(result!=null){
            localCache.put(cacheKey, result);   //回填本地缓存
            //System.out.println("use redis cache");
            return result;
        }


        String pointWkt = String.format("POINT(%f %f)", location.getLongitude(), location.getLatitude());
        result = gisHerbLocationMapper.getNearByLocations(pointWkt, radius);

        //异步更新缓存
        if (!result.isEmpty()) {
            List<GisHerbLocation> finalResult = result;
            int timeout = 600 + new Random().nextInt(100);
            CompletableFuture.runAsync(() -> {
                // 写入本地缓存
                localCache.put(cacheKey, finalResult);
                // 写入Redis缓存
                redisTemplate.opsForValue().set(cacheKey, finalResult, timeout, TimeUnit.SECONDS);
            });
        }else{
            //null 防缓存穿透
            int timeout = 25 + new Random().nextInt(10);
            redisTemplate.opsForValue().set(cacheKey, Collections.emptyList(),timeout, TimeUnit.SECONDS);
        }

        return result;
    }

    /**
     *生成缓存键
     * @param location
     * @param radius
     * @return cacheKey
     */
    private String generateNearbyCacheKey(Location location,double radius) {
        int precision = calculateGeoHashPrecision(radius);
        GeoHash geoHash = GeoHash.withCharacterPrecision(
                location.getLatitude(),
                location.getLongitude(),
                precision
        );
        return NEARBY_LOCATION_KEY + geoHash.toBase32() + ":" + (int)radius;
    }

    /**
     * 计算GeoHash精度
     * @param radius
     * @return precision
     */
    private int calculateGeoHashPrecision(double radius){
        if (radius <= 500) return 9;   // ±2米精度
        if (radius <= 2000) return 8;  // ±20米精度
        if (radius <= 5000) return 7;  // ±80米精度
        if (radius <= 20000) return 6; // ±610米精度
        return 5;                      // ±2.4公里精度
    }

    /**
     * 删除附近位置的缓存
     * @param gisHerbLocation
     */
    private void clearNearbyCacheForLocation(GisHerbLocation gisHerbLocation) {
        //获取该位置点周围可能影响的所有半径
        double[] radii = {500, 2000, 5000, 20000};
        Location loc = new Location(gisHerbLocation.getGeom().getCoordinate().getX()
                ,gisHerbLocation.getGeom().getCoordinate().getY());

        //为每个半径清理缓存
        for(double radius : radii) {
            String cacheKey = generateNearbyCacheKey(loc, radius);

            // 异步清理避免阻塞
            CompletableFuture.runAsync(() -> {
                localCache.invalidate(cacheKey);
                redisTemplate.delete(cacheKey);
            });
        }

        //MAX_VALUE情况
        String maxRadiusKey = generateNearbyCacheKey(loc, Double.MAX_VALUE);
        CompletableFuture.runAsync(() -> {
            localCache.invalidate(maxRadiusKey);
            redisTemplate.delete(maxRadiusKey);
        });
    }
}
