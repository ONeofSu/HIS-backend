package org.csu.performance.controller;

import org.csu.performance.DTO.PerformTypeDTO;
import org.csu.performance.VO.PerformTypeVO;
import org.csu.performance.VO.ResultVO;
import org.csu.performance.service.PerformTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 业绩种类Controller
 */
@RestController
@RequestMapping("/perform-types")
@Validated
public class PerformTypeController {

    @Autowired
    private PerformTypeService performTypeService;

    /**
     * 获取业绩种类列表
     */
    @GetMapping
    public ResultVO<List<PerformTypeVO>> getTypeList() {
        return performTypeService.getTypeList();
    }

    /**
     * 新增业绩种类 (管理员权限)
     */
    @PostMapping
    public ResultVO<PerformTypeVO> createType(@Valid @RequestBody PerformTypeDTO typeDTO) {
        return performTypeService.createType(typeDTO);
    }

    /**
     * 更新业绩种类 (管理员权限)
     */
    @PutMapping("/{performTypeId}")
    public ResultVO<PerformTypeVO> updateType(@PathVariable @NotNull Long performTypeId,
                                            @Valid @RequestBody PerformTypeDTO typeDTO) {
        return performTypeService.updateType(performTypeId, typeDTO);
    }

    /**
     * 删除业绩种类 (管理员权限)
     */
    @DeleteMapping("/{performTypeId}")
    public ResultVO<Void> deleteType(@PathVariable @NotNull Long performTypeId) {
        return performTypeService.deleteType(performTypeId);
    }

    /**
     * 强制删除业绩种类 (管理员权限)
     */
    @DeleteMapping("/{performTypeId}/force")
    public ResultVO<Void> forceDeleteType(@PathVariable @NotNull Long performTypeId) {
        return performTypeService.deleteType(performTypeId, true);
    }
} 