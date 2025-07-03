package org.csu.histraining.controller;

import org.csu.histraining.DTO.MaterialDTO;
import org.csu.histraining.DTO.UpdateMaterialDTO;
import org.csu.histraining.VO.MaterialVO;
import org.csu.histraining.VO.SimpleMaterialVO;
import org.csu.histraining.entity.Content;
import org.csu.histraining.entity.Material;
import org.csu.histraining.model.MaterialModel;
import org.csu.histraining.service.HerbService;
import org.csu.histraining.service.MaterialService;
import org.csu.histraining.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MaterialController {
    @Autowired
    MaterialService materialService;
    @Autowired
    UserService userService;
    @Autowired
    HerbService herbService;

    @PostMapping("/material/info")
    public ResponseEntity<?> addMaterial(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody MaterialDTO materialDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int herbId = herbService.getHerbIdByHerbName(materialDTO.getHerbName());
        if(!herbService.isHerbIdValid(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the herb does not exist")
            );
        }

        MaterialModel materialModel = materialService.transferDTOToMaterialModel(materialDTO,userId);
        int id = materialService.addMaterialModel(materialModel);
        if(id < 0){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","failed to add material, there are some error on your input")
            );
        }

        materialModel.getMaterial().setId(id);
        MaterialVO materialVO = materialService.transferModelToMaterialVO(materialModel);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "material",materialVO)
        );
    }

    @PutMapping("/material/info")
    public ResponseEntity<?> updateMaterial(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody UpdateMaterialDTO updateMaterialDTO) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }
        int herbId = herbService.getHerbIdByHerbName(updateMaterialDTO.getHerbName());
        if(!herbService.isHerbIdValid(herbId)){
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the herb does not exist")
            );
        }

        if(!materialService.isMaterialIdExist(updateMaterialDTO.getId())){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","the materialId does not exist")
            );
        }

        MaterialModel materialModel = materialService.transferDTOToMaterialModel(updateMaterialDTO,userId);
        boolean flag = materialService.updateMaterialModel(materialModel);
        if(!flag){
            return ResponseEntity.ok(
                    Map.of("code",-4,
                            "message","failed to add material, there are some error on your input")
            );
        }

        MaterialVO materialVO = materialService.transferModelToMaterialVO(materialModel);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "material",materialVO)
        );
    }

    @DeleteMapping("/material/{materialId}")
    public ResponseEntity<?> deleteMaterial(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable int materialId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!materialService.isMaterialIdExist(materialId)) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the materialId does not exist")
            );
        }

        Material material = materialService.getMaterialById(materialId);
        if(userId!=material.getUserId()){
            return ResponseEntity.ok(
                    Map.of("code",-3,
                            "message","you can't delete material made by others")
            );
        }

        List<Content> contents = materialService.getContentByMaterialId(materialId);
        MaterialModel materialModel = new MaterialModel(material,contents);
        if(!materialService.deleteMaterialContentByMaterialId(materialId)){
            return ResponseEntity.internalServerError().body("error to delete");
        }

        MaterialVO materialVO = materialService.transferModelToMaterialVO(materialModel);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "material",materialVO)
        );
    }

    @GetMapping("/material/all")
    public ResponseEntity<?> getAllMaterial(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Material> materials = materialService.getAllMaterialDividePages(page, size);
        List<SimpleMaterialVO> simpleMaterialVOS = materialService.transferMaterialToSimpleVOList(materials);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "materials",simpleMaterialVOS)
        );
    }

    @GetMapping("/material/info/{materialId}")
    public ResponseEntity<?> getMaterialById(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable int materialId) {
        String token = authHeader.substring(7);
        int userId = userService.getUserId(token);
        if(!userService.isUserIdExist(userId)) {
            return ResponseEntity.ok(
                    Map.of("code",-1,
                            "message","invalid token")
            );
        }

        if(!materialService.isMaterialIdExist(materialId)) {
            return ResponseEntity.ok(
                    Map.of("code",-2,
                            "message","the materialId does not exist")
            );
        }

        Material material = materialService.getMaterialById(materialId);
        List<Content> contents = materialService.getContentByMaterialId(materialId);

        material.setCount(material.getCount()+1);
        materialService.updateMaterial(material);

        MaterialModel materialModel = new MaterialModel(material,contents);

        MaterialVO materialVO = materialService.transferModelToMaterialVO(materialModel);
        return ResponseEntity.ok(
                Map.of("code",0,
                        "material",materialVO)
        );
    }
}
