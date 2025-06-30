package org.csu.herbinfo.service.impl;

import org.csu.herbinfo.VO.DistrictCountVO;
import org.csu.herbinfo.VO.TopHerbVO;
import org.csu.herbinfo.entity.District;
import org.csu.herbinfo.entity.HerbLocation;
import org.csu.herbinfo.model.TopHerbModel;
import org.csu.herbinfo.service.DistrictStreetService;
import org.csu.herbinfo.service.HerbLocationService;
import org.csu.herbinfo.service.HerbService;
import org.csu.herbinfo.service.LocationCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LocationCountServiceImpl implements LocationCountService {
    @Autowired
    HerbLocationService herbLocationService;
    @Autowired
    HerbService herbService;
    @Autowired
    DistrictStreetService districtStreetService;

    @Override
    public List<DistrictCountVO> getDistrictCount() {
        //设置区名
        List<District> districts = districtStreetService.getAllDistricts();
        List<DistrictCountVO> districtCountVOs = new ArrayList<DistrictCountVO>();
        for (District district : districts) {
            DistrictCountVO districtCountVO = new DistrictCountVO();
            districtCountVO.setDistrictName(district.getName());
            districtCountVOs.add(districtCountVO);
        }

        //设置数量
        for (DistrictCountVO districtCountVO : districtCountVOs) {
            districtCountVO.setHerbCount(herbLocationService.
                    getHerbCountsByDistrictName(districtCountVO.getDistrictName()));
        }

        return districtCountVOs;
    }

    @Override
    public List<TopHerbModel> getHerbNameAndCountNumsOnHerbLocation() {
        //获取所有位置信息
        List<HerbLocation> herbLocations = herbLocationService.getAllHerbLocations();
        HashMap<Integer,Integer> herbMap = new HashMap<>();

        //遍历
        for (HerbLocation herbLocation : herbLocations) {
            if(!herbMap.containsKey(herbLocation.getHerbId())){
                //没有则新增
                herbMap.put(herbLocation.getHerbId(), herbLocation.getCount());
            }else{
                //有则加上
                int count = herbMap.get(herbLocation.getHerbId());
                herbMap.put(herbLocation.getHerbId(), count + herbLocation.getCount());
            }
        }

        //转化为List
        List<TopHerbModel> topHerbModelList = new ArrayList<>(herbMap.entrySet().stream()
                .map(entry -> new TopHerbModel(entry.getKey(), entry.getValue()))
                .toList());

        topHerbModelList.sort((a, b) -> b.getCount() - a.getCount());   //降序排序
        return topHerbModelList;
    }

    @Override
    public List<TopHerbVO> transferTopHerbModelToVO(List<TopHerbModel> topHerbModels) {
        List<TopHerbVO> topHerbVOs = new ArrayList<TopHerbVO>();
        int showHerbNums = 4;
        if(topHerbModels.size()<4){
            showHerbNums = topHerbModels.size();
        }
        for(int i=0;i<showHerbNums;i++){
            TopHerbVO topHerbVO = new TopHerbVO();
            TopHerbModel topHerbModel = topHerbModels.get(i);
            topHerbVO.setHerbName(herbService.getHerbById(topHerbModel.getHerbId()).getName());
            topHerbVO.setHerbNumber(topHerbModel.getCount());
            topHerbVOs.add(topHerbVO);
        }

        if(topHerbModels.size()<4){
            return topHerbVOs;
        }

        //其他项统一计算
        int else_count = 0;
        TopHerbVO elseHerb = new TopHerbVO();
        elseHerb.setHerbName("其他");
        for(int i=4;i<topHerbModels.size();i++){
            else_count += topHerbModels.get(i).getCount();
        }
        elseHerb.setHerbNumber(else_count);
        topHerbVOs.add(elseHerb);

        return topHerbVOs;
    }

    @Override
    public List<TopHerbVO> getTopHerbs() {
        List<TopHerbModel> topHerbModelList = getHerbNameAndCountNumsOnHerbLocation();
        return transferTopHerbModelToVO(topHerbModelList);
    }
}
