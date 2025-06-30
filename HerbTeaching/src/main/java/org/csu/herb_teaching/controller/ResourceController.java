package org.csu.herb_teaching.controller;

import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.entity.LabResource;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.utils.DataConvertUtil;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.csu.herb_teaching.VO.LabResourceVO;
import org.csu.herb_teaching.VO.LabInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    @Autowired
    private CourseService courseService;

    // 1.1上传实验资源（只登记云端URL和元数据）
    @PostMapping("/labs/{labId}/resources")
    public ResponseEntity<Map<String, Object>> uploadResource(
            @PathVariable int labId,
            @RequestBody Map<String, Object> requestBody) {
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("实验不存在");
            }

            // 校验参数
            Integer labResourceType = (Integer) requestBody.get("labResourceType");
            Integer labResourceOrder = (Integer) requestBody.get("labResourceOrder");
            String labResourceTitle = (String) requestBody.get("labResourceTitle");
            String labResourceContent = (String) requestBody.get("labResourceContent"); // 云端URL
            String labResourceMetadata = (String) requestBody.get("labResourceMetadata");

            if (labResourceType == null || labResourceOrder == null || labResourceTitle == null || labResourceContent == null) {
                return ResponseUtil.error("参数不完整");
            }

            // 打印收到的labResourceMetadata内容
            System.out.println("labResourceMetadata = " + labResourceMetadata);

            // 如果为空，自动补"{}"
            if (labResourceMetadata == null || labResourceMetadata.trim().isEmpty()) {
                labResourceMetadata = "{}";
            }

            // 校验是否为合法JSON字符串
            try {
                new com.fasterxml.jackson.databind.ObjectMapper().readTree(labResourceMetadata);
            } catch (Exception e) {
                return ResponseUtil.error("labResourceMetadata 不是合法的JSON字符串");
            }

            // 创建资源记录
            LabResource resource = new LabResource();
            resource.setLabId(labId);
            resource.setType(labResourceType);
            resource.setResourceOrder(labResourceOrder);
            resource.setTitle(labResourceTitle);
            resource.setContent(labResourceContent); // 直接存云端URL
            resource.setMetadata(labResourceMetadata);
            resource.setTime(new Timestamp(System.currentTimeMillis()));
            resource.setIsValid(true);

            if (!courseService.addLabResource(resource)) {
                return ResponseUtil.error("保存资源失败");
            }

            // 构建响应
            Course course = courseService.getCourseById(lab.getCourseId());
            LabInfoVO labInfo = DataConvertUtil.labInfoToVO(null, lab, course);
            LabResourceVO resourceVO = DataConvertUtil.resourceToVO(resource, labInfo);

            Map<String, Object> data = new HashMap<>();
            data.put("resource", resourceVO);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("上传资源失败: " + e.getMessage());
        }
    }

    // 1.2获取实验资源列表
    @GetMapping("/labs/{labId}/resources")
    public ResponseEntity<Map<String, Object>> getLabResources(@PathVariable int labId) {
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("实验不存在");
            }

            List<LabResource> resources = courseService.getLabResourcesByLabId(labId);
            Course course = courseService.getCourseById(lab.getCourseId());
            LabInfoVO labInfo = DataConvertUtil.labInfoToVO(null, lab, course);

            List<LabResourceVO> resourceVOs = resources.stream()
                .map(resource -> DataConvertUtil.resourceToVO(resource, labInfo))
                .collect(Collectors.toList());

            // 统计信息
            Map<String, Object> resourceSummary = new HashMap<>();
            resourceSummary.put("totalCount", resources.size());
            resourceSummary.put("textCount", (int) resources.stream().filter(r -> r.getType() == 0).count());
            resourceSummary.put("imageCount", (int) resources.stream().filter(r -> r.getType() == 1).count());
            resourceSummary.put("videoCount", (int) resources.stream().filter(r -> r.getType() == 2).count());
            resourceSummary.put("documentCount", (int) resources.stream().filter(r -> r.getType() == 3).count());

            Map<String, Object> labInfoMap = new HashMap<>();
            labInfoMap.put("labId", labId);
            labInfoMap.put("labDes", lab.getDes());
            labInfoMap.put("courseName", course != null ? course.getName() : "");

            Map<String, Object> data = new HashMap<>();
            data.put("labInfo", labInfoMap);
            data.put("resourceSummary", resourceSummary);
            data.put("resources", resourceVOs);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取资源列表失败");
        }
    }

    // 1.3获取实验特定类型资源
    @GetMapping("/labs/{labId}/resources/type/{resourceType}")
    public ResponseEntity<Map<String, Object>> getLabResourcesByType(
            @PathVariable int labId, 
            @PathVariable int resourceType) {
        
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("实验不存在");
            }

            List<LabResource> resources = courseService.getLabResourcesByType(labId, resourceType);
            Course course = courseService.getCourseById(lab.getCourseId());
            LabInfoVO labInfo = DataConvertUtil.labInfoToVO(null, lab, course);

            List<LabResourceVO> resourceVOs = resources.stream()
                .map(resource -> DataConvertUtil.resourceToVO(resource, labInfo))
                .collect(Collectors.toList());

            Map<String, Object> labInfoMap = new HashMap<>();
            labInfoMap.put("labId", labId);
            labInfoMap.put("labDes", lab.getDes());
            labInfoMap.put("courseName", course != null ? course.getName() : "");

            Map<String, Object> filter = new HashMap<>();
            filter.put("resourceType", resourceType);
            filter.put("resourceTypeName", DataConvertUtil.getResourceTypeName(resourceType));

            Map<String, Object> data = new HashMap<>();
            data.put("labInfo", labInfoMap);
            data.put("filter", filter);
            data.put("resources", resourceVOs);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取资源列表失败");
        }
    }

    // 1.4删除实验资源
    @DeleteMapping("/{labResourceId}")
    public ResponseEntity<Map<String, Object>> deleteResource(@PathVariable int labResourceId) {
        try {
            LabResource resource = courseService.getLabResourceById(labResourceId);
            if (resource == null) {
                return ResponseUtil.error("该资源不存在");
            }

            Lab lab = courseService.getLabById(resource.getLabId());
            if (courseService.deleteLabResourceById(labResourceId)) {
                Map<String, Object> deletedResource = new HashMap<>();
                deletedResource.put("labResourceId", labResourceId);
                deletedResource.put("labResourceTitle", resource.getTitle());
                
                Map<String, Object> labInfo = new HashMap<>();
                labInfo.put("labId", resource.getLabId());
                labInfo.put("labDes", lab != null ? lab.getDes() : "");
                deletedResource.put("labInfo", labInfo);

                Map<String, Object> data = new HashMap<>();
                data.put("deletedResource", deletedResource);

                return ResponseUtil.success(data);
            } else {
                return ResponseUtil.error("删除资源失败");
            }
        } catch (Exception e) {
            return ResponseUtil.error("删除资源失败");
        }
    }

    // 2.1资源预览
    @GetMapping("/{labResourceId}/preview")
    public ResponseEntity<Map<String, Object>> previewResource(@PathVariable int labResourceId) {
        try {
            LabResource resource = courseService.getLabResourceById(labResourceId);
            if (resource == null) {
                return ResponseUtil.error("资源不存在");
            }

            Lab lab = courseService.getLabById(resource.getLabId());
            Course course = courseService.getCourseById(lab.getCourseId());

            Map<String, Object> labInfo = new HashMap<>();
            labInfo.put("labId", resource.getLabId());
            labInfo.put("labDes", lab != null ? lab.getDes() : "");
            labInfo.put("courseName", course != null ? course.getName() : "");

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("width", 800);
            metadata.put("height", 600);
            metadata.put("fileSize", "1.2MB");

            Map<String, Object> resourceData = new HashMap<>();
            resourceData.put("labResourceId", resource.getId());
            resourceData.put("labResourceType", resource.getType());
            resourceData.put("labResourceTypeName", DataConvertUtil.getResourceTypeName(resource.getType()));
            resourceData.put("labResourceTitle", resource.getTitle());
            resourceData.put("previewUrl", "/static/preview/" + resource.getId());
            resourceData.put("labInfo", labInfo);
            resourceData.put("metadata", metadata);

            Map<String, Object> data = new HashMap<>();
            data.put("resource", resourceData);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取资源预览失败");
        }
    }

    // 2.2资源下载
    @GetMapping("/{labResourceId}/download")
    public ResponseEntity<Map<String, Object>> downloadResource(@PathVariable int labResourceId) {
        try {
            LabResource resource = courseService.getLabResourceById(labResourceId);
            if (resource == null) {
                return ResponseUtil.error("资源不存在");
            }

            Map<String, Object> data = new HashMap<>();
            data.put("downloadUrl", "/static/download/" + resource.getId());
            data.put("fileName", resource.getTitle());

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("下载资源失败");
        }
    }

    private String getResourceTypePath(int resourceType) {
        switch (resourceType) {
            case 0: return "text";
            case 1: return "images";
            case 2: return "videos";
            case 3: return "documents";
            default: return "others";
        }
    }
} 