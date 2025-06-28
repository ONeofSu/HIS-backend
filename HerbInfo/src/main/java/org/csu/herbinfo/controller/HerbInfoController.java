package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.HerbDTO;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbCategory;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
交互基本信息的Controller
 */

@RestController
public class HerbInfoController {
    @Autowired
    HerbService herbService;

    @GetMapping("herbs/info/{name}")
    public ResponseEntity<?> getHerbInfo(@PathVariable String name) {
        int herbId = herbService.getHerbIdByName(name);
        if(herbId == -1){
            return ResponseEntity.status(490).body("中药不存在");
        }
        Herb herb = herbService.getHerbById(herbId);
        if(herb == null){
            return ResponseEntity.status(500).body("获取失败");
        }
        return ResponseEntity.ok(herb);
    }

    @GetMapping("/herbs")
    public ResponseEntity<?> getAllHerbs() {
        ResponseEntity<List<Herb>> result = null;
        List<Herb> herbs = herbService.getAllHerbs();
        result = ResponseEntity.ok(herbs);
        return result;
    }

    @PostMapping("/herbs/info")
    public ResponseEntity<?> addHerb(@RequestBody HerbDTO herbDTO){
        Herb herb = herbService.transferDTOToHerb(herbDTO);
        if(herbService.isHerbNameExist(herb.getName())){
            return ResponseEntity.status(490).body("中药已存在");
        }
        if(!herbService.addHerb(herb)){
            return ResponseEntity.status(500).body("添加失败");
        }
        return ResponseEntity.ok(herb);
    }

    @DeleteMapping("/herbs/info/{herbId}")
    public ResponseEntity<?> deleteHerb(@PathVariable int herbId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.status(490).body("中药不存在");
        }
        if(herbService.isExistLinkOnHerb(herbId)){
            return ResponseEntity.status(491).body("该中药还有种类连接，不能删除");
        }
        if(!herbService.deleteHerbById(herbId)){
            return ResponseEntity.status(500).body("删除失败");
        }
        return ResponseEntity.ok(herbId);
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory() {
        ResponseEntity<List<HerbCategory>> result = null;
        List<HerbCategory> herbCategories = herbService.getAllHerbCategories();
        result = ResponseEntity.ok(herbCategories);
        return result;
    }

    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@RequestBody HerbDTO herbDTO) {
        String name = herbDTO.getName();
        if(name==null || name.equals("")){
            return ResponseEntity.status(490).body("种类名不得为空");
        }
        if(herbService.isHerbCategoryNameExist(name)){
            return ResponseEntity.status(491).body("该种类已存在");
        }

        HerbCategory herbCategory = new HerbCategory();
        herbCategory.setName(name);
        if(!herbService.addHerbCategory(herbCategory)){
           return ResponseEntity.status(500).body("添加失败");
        }
        return ResponseEntity.ok(herbCategory);
    }

    @DeleteMapping("/category/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int category_id) {
        if(!herbService.isHerbCategoryIdExist(category_id)){
            return ResponseEntity.status(490).body("中药种类不存在");
        }
        if(herbService.isExistLinkOnCategory(category_id)){
            return ResponseEntity.status(491).body("该种类还有种中药接，不能删除");
        }
        if(!herbService.deleteHerbCategoryById(category_id)){
            return ResponseEntity.status(500).body("删除失败");
        }
        return ResponseEntity.ok(category_id);
    }
}
