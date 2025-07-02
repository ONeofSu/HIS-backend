package org.csu.herbinfo.controller.inner;

import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner")
public class InnerHerbController {
    @Autowired
    HerbService herbService;

    @GetMapping("/herb/valid/id/{herbId}")
    public boolean validHerbId(@PathVariable("herbId") int herbId) {
        return herbService.isHerbIdExist(herbId);
    }

    @GetMapping("/herb/valid/name/{herbName}")
    public boolean validHerbName(@PathVariable("herbName") String herbName) {
        return herbService.isHerbNameExist(herbName);
    }

    @GetMapping("/herb/name/{herbName}/id")
    public int getIdByHerbName(@PathVariable("herbName") String herbName) {
        return herbService.getHerbIdByName(herbName);
    }

    @GetMapping("/herb/id/{herbId}/name")
    public String getHerbNameById(@PathVariable("herbId") int herbId) {
        return herbService.getHerbById(herbId).getName();
    }
}
