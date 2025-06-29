package org.csu.hisuser.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.csu.hisuser.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
