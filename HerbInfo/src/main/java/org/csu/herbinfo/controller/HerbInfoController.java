package org.csu.herbinfo.controller;

import org.csu.herbinfo.DTO.HerbDTO;
import org.csu.herbinfo.VO.HerbLinkCategoryVO;
import org.csu.herbinfo.VO.HerbVO;
import org.csu.herbinfo.entity.Herb;
import org.csu.herbinfo.entity.HerbCategory;
import org.csu.herbinfo.entity.HerbLinkCategory;
import org.csu.herbinfo.service.AIGenerationService;
import org.csu.herbinfo.service.HerbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/*
交互基本信息的Controller
 */

@RestController
public class HerbInfoController {
    @Autowired
    HerbService herbService;
    @Autowired
    AIGenerationService aiGenerationService;

    @GetMapping("herbs/info/{name}")
    public ResponseEntity<?> getHerbInfo(@PathVariable String name) {
        int herbId = herbService.getHerbIdByName(name);
        if(herbId == -1){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        Herb herb = herbService.getHerbById(herbId);
        if(herb == null){
            return ResponseEntity.status(500).body("error to get");
        }
        HerbVO herbVO = herbService.transferHerbToVO(herb);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herb",herbVO)
        );
    }

    @GetMapping("herbs/info/id/{herbId}")
    public ResponseEntity<?> getHerbInfoById(@PathVariable int herbId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        Herb herb = herbService.getHerbById(herbId);
        if(herb == null){
            return ResponseEntity.status(500).body("error to get");
        }
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herbId", herbId,
                        "herbName", herb.getName() != null ? herb.getName() : "",
                        "herbImageUrl", herb.getImage() != null ? herb.getImage() : "")
        );
    }

    @GetMapping("/herbs")
    public ResponseEntity<?> getAllHerbs() {
        ResponseEntity<List<Herb>> result = null;
        List<Herb> herbs = herbService.getAllHerbs();
        List<HerbVO> herbVOs = herbService.transferHerbToVOList(herbs);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herbs",herbVOs)
        );
    }

    @PostMapping("/herbs/info")
    public ResponseEntity<?> addHerb(@RequestBody HerbDTO herbDTO){
        Herb herb = herbService.transferDTOToHerb(herbDTO);
        if(herbService.isHerbNameExist(herb.getName())){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb already exist")
            );
        }
        if(!herbService.addHerb(herb)){
            return ResponseEntity.status(500).body("error to add");
        }
        herb = herbService.getHerbByName(herb.getName());
        HerbVO herbVO = herbService.transferHerbToVO(herb);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herb",herbVO)
        );
    }

    @DeleteMapping("/herbs/info/{herbId}")
    public ResponseEntity<?> deleteHerb(@PathVariable int herbId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        if(herbService.isExistLinkOnHerb(herbId)){
            //return ResponseEntity.status(491).body("该中药还有种类连接，不能删除");
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","There are still categories links on herb, so you can't delete it")
            );
        }
        Herb herb = herbService.getHerbById(herbId);
        HerbVO herbVO = herbService.transferHerbToVO(herb);

        if(!herbService.deleteHerbById(herbId)){
            return ResponseEntity.status(500).body("error to delete");
        }

        return ResponseEntity.ok(
                Map.of("code",0,
                        "herb",herbVO)
        );
    }

    @PutMapping("/herbs/info/{herbId}")
    public ResponseEntity<?> updateHerb(@RequestBody HerbDTO herbDTO, @PathVariable int herbId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }

        Herb herb = herbService.transferDTOToHerb(herbDTO);
        herb.setId(herbId);
        if(!herbService.updateHerbForId(herb)){
            return ResponseEntity.status(500).body("error to update");
        }
        HerbVO herbVO = herbService.transferHerbToVO(herb);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "herb",herbVO)
        );
    }

    @GetMapping("/category")
    public ResponseEntity<?> getAllCategory() {
        ResponseEntity<List<HerbCategory>> result = null;
        List<HerbCategory> herbCategories = herbService.getAllHerbCategories();
        return ResponseEntity.ok(
                Map.of("code",0
                ,"herbCategories",herbCategories)
        );
    }

    @PostMapping("/category")
    public ResponseEntity<?> addCategory(@RequestBody HerbDTO herbDTO) {
        String name = herbDTO.getName();
        if(name==null || name.equals("")){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb category name is empty")
            );
        }
        if(herbService.isHerbCategoryNameExist(name)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the category name already exist")
            );
        }

        HerbCategory herbCategory = new HerbCategory();
        herbCategory.setName(name);
        if(!herbService.addHerbCategory(herbCategory)){
           return ResponseEntity.status(500).body("error to add");
        }
        int herbCategoryId = herbService.getHerbCategoryIdByName(herbCategory.getName());
        herbCategory = herbService.getHerbCategoryById(herbCategoryId);

        return ResponseEntity.ok(
                Map.of("code",0,
                        "herbCategory",herbCategory)
        );
    }

    @DeleteMapping("/category/{category_id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int category_id) {
        if(!herbService.isHerbCategoryIdExist(category_id)){
            //return ResponseEntity.status(490).body("中药种类不存在");
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb category does not exist")
            );
        }
        if(herbService.isExistLinkOnCategory(category_id)){
            //return ResponseEntity.status(491).body("该种类还有中药链接，不能删除");
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","There are still herb links on category, so you can't delete it")
            );
        }
        HerbCategory herbCategory = herbService.getHerbCategoryById(category_id);

        if(!herbService.deleteHerbCategoryById(category_id)){
            return ResponseEntity.status(500).body("error to delete");
        }
        return ResponseEntity.ok(
                Map.of("code",200,
                        "herbCategory",herbCategory)
        );
    }

    @PostMapping("/{herbId}/links/{categoryId}")
    public ResponseEntity<?> addLink(@PathVariable int herbId, @PathVariable int categoryId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        if(!herbService.isHerbCategoryIdExist(categoryId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","herb category does not exist")
            );
        }
        if(herbService.isLinkExist(herbId, categoryId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","the herb-category link already exist")
            );
        }
        if(!herbService.addHerbLinkCategory(herbId, categoryId)){
            return ResponseEntity.status(500).body("error to add");
        }
        return ResponseEntity.ok(
                Map.of("code",0)
        );
    }

    @DeleteMapping("/{herbId}/links/{categoryId}")
    public ResponseEntity<?> deleteLink(@PathVariable int herbId, @PathVariable int categoryId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        if(!herbService.isHerbCategoryIdExist(categoryId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","herb category does not exist")
            );
        }
        if(!herbService.isLinkExist(herbId, categoryId)){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","the herb-category link does not exist")
            );
        }
        if(!herbService.deleteHerbLinkCategory(herbId, categoryId)){
            return ResponseEntity.status(500).body("error to add");
        }
        return ResponseEntity.ok(
                Map.of("code",0)
        );
    }

    @GetMapping("/{herbId}/links")
    public ResponseEntity<?> getAllLinksOnHerb(@PathVariable int herbId) {
        if(!herbService.isHerbIdExist(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb does not exist")
            );
        }
        ResponseEntity<List<HerbLinkCategory>> result = null;
        List<HerbLinkCategory> list = herbService.getLinksOnHerb(herbId);
        List<HerbLinkCategoryVO> linkList = herbService.transferLinkToVOList(list);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "links",linkList)
        );
    }

    @GetMapping("/links/{categoryId}")
    public ResponseEntity<?> getAllLinksOnCategory(@PathVariable int categoryId) {
        if(!herbService.isHerbCategoryIdExist(categoryId)){
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","herb category does not exist")
            );
        }
        ResponseEntity<List<HerbLinkCategory>> result = null;
        List<HerbLinkCategory> list = herbService.getLinksOnHerbCategory(categoryId);
        List<HerbLinkCategoryVO> linkList = herbService.transferLinkToVOList(list);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "links",linkList)
        );
    }

    @GetMapping("/herbs/{herbId}/exist")
    public boolean isHerbExist(@PathVariable int herbId) {
        return herbService.isHerbIdExist(herbId);
    }


    @PostMapping("/ai/generate")
    public ResponseEntity<?> ai(@RequestParam String query){
        String total_query = "作为中药专家,帮我推荐一下:" + query;
        String response = aiGenerationService.generateText(total_query);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "response",response)
        );
    }
}
