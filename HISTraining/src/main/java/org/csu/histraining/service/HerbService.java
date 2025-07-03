package org.csu.histraining.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "herb-info-service")
public interface HerbService {
    @GetMapping("/inner/herb/valid/name/{herbName}")
    public boolean isHerbNameValid(@PathVariable("herbName") String herbName);
    @GetMapping("/inner/herb/valid/id/{herbId}")
    public boolean isHerbIdValid(@PathVariable("herbId") int herbId);
    @GetMapping("/inner/herb/name/{herbName}/id")
    public int getHerbIdByHerbName(@PathVariable("herbName") String herbName);
    @GetMapping("/inner/herb/id/{herbId}/name")
    public String getHerbNameByHerbId(@PathVariable("herbId") int herbId);
}
