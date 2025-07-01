package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.service.LabService;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labs")
@RequiredArgsConstructor
public class LabController {

    private final LabService labService;

    @GetMapping("/{labId}")
    public ResponseUtil<Lab> getLab(@PathVariable int labId) {
        Lab lab = labService.getLabById(labId);
        if (lab == null) {
            return new ResponseUtil<>(-1, "Lab not found.", null);
        }
        return new ResponseUtil<>(0, "Success", lab);
    }

    @PutMapping("/{labId}")
    public ResponseUtil<Lab> updateLab(@PathVariable int labId, @RequestBody LabDTO labDTO) {
        Lab updatedLab = labService.updateLab(labId, labDTO);
        if (updatedLab == null) {
            return new ResponseUtil<>(-1, "Lab not found.", null);
        }
        return new ResponseUtil<>(0, "Lab updated successfully.", updatedLab);
    }

    @DeleteMapping("/{labId}")
    public ResponseUtil<Object> deleteLab(@PathVariable int labId) {
        boolean success = labService.deleteLab(labId);
        if (!success) {
            return new ResponseUtil<>(-1, "Lab not found.", null);
        }
        return new ResponseUtil<>(0, "Lab deleted successfully.", null);
    }
} 