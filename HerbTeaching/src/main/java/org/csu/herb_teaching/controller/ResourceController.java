package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/{resourceId}")
    public ResponseEntity<?> getResource(@PathVariable int resourceId) {
        CourseResource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            return ResponseEntity.ok(Map.of("code", -1, "message", "Resource not found!"));
        }
        return ResponseEntity.ok(Map.of("code", 0, "resource", resource));
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<?> updateResource(@PathVariable int resourceId, @RequestBody CourseResourceDTO resourceDTO) {
        CourseResource updatedResource = resourceService.updateResource(resourceId, resourceDTO);
        if (updatedResource == null) {
            return ResponseEntity.ok(Map.of("code", -1, "message", "Resource not found!"));
        }
        return ResponseEntity.ok(Map.of("code", 0, "resource", updatedResource));
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<?> deleteResource(@PathVariable int resourceId) {
        boolean success = resourceService.deleteResource(resourceId);
        if (!success) {
            return ResponseEntity.ok(Map.of("code", -1, "message", "Resource not found!"));
        }
        return ResponseEntity.ok(Map.of("code", 0, "message", "Resource deleted successfully."));
    }
} 