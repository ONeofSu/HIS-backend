package org.csu.userfeign.Service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value="user-service-provider")
public interface UserService {
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String Hello();
}
