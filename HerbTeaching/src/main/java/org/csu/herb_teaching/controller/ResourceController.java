package org.csu.herb_teaching.controller;

import lombok.RequiredArgsConstructor;
import org.csu.herb_teaching.DTO.CourseResourceDTO;
import org.csu.herb_teaching.entity.CourseResource;
import org.csu.herb_teaching.service.ResourceService;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @GetMapping("/{resourceId}")
    public ResponseUtil<CourseResource> getResource(@PathVariable int resourceId) {
        CourseResource resource = resourceService.getResourceById(resourceId);
        if (resource == null) {
            return new ResponseUtil<>(-1, "Resource not found.", null);
        }
        return new ResponseUtil<>(0, "Success", resource);
    }

    @PutMapping("/{resourceId}")
    public ResponseUtil<CourseResource> updateResource(@PathVariable int resourceId, @RequestBody CourseResourceDTO resourceDTO) {
        CourseResource updatedResource = resourceService.updateResource(resourceId, resourceDTO);
        if (updatedResource == null) {
            return new ResponseUtil<>(-1, "Resource not found.", null);
        }
        return new ResponseUtil<>(0, "Resource updated successfully.", updatedResource);
    }

    @DeleteMapping("/{resourceId}")
    public ResponseUtil<Object> deleteResource(@PathVariable int resourceId) {
        boolean success = resourceService.deleteResource(resourceId);
        if (!success) {
            return new ResponseUtil<>(-1, "Resource not found.", null);
        }
        return new ResponseUtil<>(0, "Resource deleted successfully.", null);
    }
} 