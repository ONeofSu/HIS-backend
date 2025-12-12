package org.csu.herbinfo.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.csu.herbinfo.entity.GisHerbLocation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@DS("postgresql")
public interface GisHerbLocationPGSqlMapper extends BaseMapper<GisHerbLocation> {
    @Select("""
        SELECT 
            location_id AS id,
            herb_id AS herbId,
            location_count AS count,
            district_id AS districtId,
            street_id AS streetId,
            geom
        FROM herb_location 
        WHERE ST_DWithin(
            geom::geography, 
            ST_GeomFromText(#{pointWkt}, 4326)::geography, 
            #{distance}
        )
    """)
    List<GisHerbLocation> getNearByLocations(@Param("pointWkt") String pointWkt, @Param("distance") double distance);

}
