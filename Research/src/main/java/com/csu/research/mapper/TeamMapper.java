package com.csu.research.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.entity.Team;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TeamMapper extends BaseMapper<Team> {

    @Select("select * from team where team_name like concat('%', #{query}, '%')")
    Page<Team> searchTeamByName(Page<Team> page, @Param("query") String query);
}
