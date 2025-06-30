package org.csu.herb_teaching.controller;

import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.entity.LabInfo;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.utils.DataConvertUtil;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.csu.herb_teaching.VO.LabInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/lab-info")
public class LabInfoController {

    @Autowired
    private CourseService courseService;

    // 1.获得实验详情
    @GetMapping("/labs/{labId}/info")
    public ResponseEntity<Map<String, Object>> getLabInfo(@PathVariable int labId) {
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("实验不存在");
            }

            LabInfo labInfo = courseService.getLabInfoByLabId(labId);
            if (labInfo == null) {
                return ResponseUtil.error("实验详情不存在");
            }

            Course course = courseService.getCourseById(lab.getCourseId());
            LabInfoVO labInfoVO = DataConvertUtil.labInfoToVO(labInfo, lab, course);

            Map<String, Object> data = new HashMap<>();
            data.put("labInfo", labInfoVO);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取实验详情失败");
        }
    }

    // 2.更新实验详情
    @PutMapping("/labs/{labId}/info")
    public ResponseEntity<Map<String, Object>> updateLabInfo(
            @PathVariable int labId,
            @RequestBody Map<String, Object> requestBody) {
        
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("实验不存在");
            }

            LabInfo labInfo = courseService.getLabInfoByLabId(labId);
            if (labInfo == null) {
                return ResponseUtil.error("实验详情不存在");
            }

            // 更新实验详情
            if (requestBody.containsKey("herbId")) {
                labInfo.setHerbId((Integer) requestBody.get("herbId"));
            }
            if (requestBody.containsKey("labInfoGoal")) {
                labInfo.setGoal((String) requestBody.get("labInfoGoal"));
            }
            if (requestBody.containsKey("labInfoSummary")) {
                labInfo.setSummary((String) requestBody.get("labInfoSummary"));
            }

            if (!courseService.updateLabInfoForId(labInfo)) {
                return ResponseUtil.error("更新实验详情失败");
            }

            Course course = courseService.getCourseById(lab.getCourseId());
            LabInfoVO labInfoVO = DataConvertUtil.labInfoToVO(labInfo, lab, course);

            Map<String, Object> data = new HashMap<>();
            data.put("labInfo", labInfoVO);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("更新实验详情失败");
        }
    }

    // 3.新增实验详情
    @PostMapping("/labs/{labId}/info")
    public ResponseEntity<Map<String, Object>> addLabInfo(
            @PathVariable int labId,
            @RequestBody Map<String, Object> requestBody) {
        
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("实验不存在");
            }

            // 检查是否已存在实验详情
            LabInfo existingLabInfo = courseService.getLabInfoByLabId(labId);
            if (existingLabInfo != null) {
                return ResponseUtil.error("该实验已有详情，请使用更新接口");
            }

            // 创建新的实验详情
            LabInfo labInfo = new LabInfo();
            labInfo.setLabId(labId);
            
            if (requestBody.containsKey("herbId")) {
                labInfo.setHerbId((Integer) requestBody.get("herbId"));
            } else {
                return ResponseUtil.error("药材ID不能为空");
            }
            
            if (requestBody.containsKey("labInfoGoal")) {
                labInfo.setGoal((String) requestBody.get("labInfoGoal"));
            } else {
                labInfo.setGoal("");
            }
            
            if (requestBody.containsKey("labInfoSummary")) {
                labInfo.setSummary((String) requestBody.get("labInfoSummary"));
            } else {
                labInfo.setSummary("");
            }
            
            labInfo.setIsValid(true);

            if (!courseService.addLabInfo(labInfo)) {
                return ResponseUtil.error("新增实验详情失败");
            }

            Course course = courseService.getCourseById(lab.getCourseId());
            LabInfoVO labInfoVO = DataConvertUtil.labInfoToVO(labInfo, lab, course);

            Map<String, Object> data = new HashMap<>();
            data.put("labInfo", labInfoVO);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("新增实验详情失败");
        }
    }
} 