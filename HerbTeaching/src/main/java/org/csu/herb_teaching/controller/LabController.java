package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.service.LabService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/labs")
@RequiredArgsConstructor
public class LabController {

    private final LabService labService;

    @GetMapping("/{labId}")
    public ResponseEntity<?> getLab(@PathVariable int labId) {
        Lab lab = labService.getLabById(labId);
        if (lab == null) {
            return ResponseEntity.ok(Map.of("code", -1, "message", "Lab not found!"));
        }
        return ResponseEntity.ok(Map.of("code", 0, "lab", lab));
    }

    @PutMapping("/{labId}")
    public ResponseEntity<?> updateLab(@PathVariable int labId, @RequestBody LabDTO labDTO) {
        Lab updatedLab = labService.updateLab(labId, labDTO);
        if (updatedLab == null) {
            return ResponseEntity.ok(Map.of("code", -1, "message", "Lab not found!"));
        }
        return ResponseEntity.ok(Map.of("code", 0, "lab", updatedLab));
    }

    @DeleteMapping("/{labId}")
    public ResponseEntity<?> deleteLab(@PathVariable int labId) {
        boolean success = labService.deleteLab(labId);
        if (!success) {
            return ResponseEntity.ok(Map.of("code", -1, "message", "Lab not found!"));
        }
        return ResponseEntity.ok(Map.of("code", 0, "message", "Lab deleted successfully."));
    }
} 