package org.csu.herb_teaching.controller;

import org.csu.herb_teaching.DTO.LabDTO;
import org.csu.herb_teaching.entity.Course;
import org.csu.herb_teaching.entity.Lab;
import org.csu.herb_teaching.entity.LabInfo;
import org.csu.herb_teaching.entity.LabResource;
import org.csu.herb_teaching.service.CourseService;
import org.csu.herb_teaching.utils.DataConvertUtil;
import org.csu.herb_teaching.utils.ResponseUtil;
import org.csu.herb_teaching.VO.LabVO;
import org.csu.herb_teaching.VO.HerbVO;
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
@RequestMapping("/labs")
public class LabController {

    @Autowired
    private CourseService courseService;

    // 1.0获取所有实验
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getAllLabs() {
        try {
            List<Lab> labs = courseService.getAllLabs();
            List<LabVO> labVOs = labs.stream()
                .map(lab -> {
                    Course course = courseService.getCourseById(lab.getCourseId());
                    List<LabResource> resources = courseService.getLabResourcesByLabId(lab.getId());
                    HerbVO herbInfo = DataConvertUtil.createHerbVO(1);
                    return DataConvertUtil.labToVO(lab, course, resources.size(), herbInfo);
                })
                .collect(Collectors.toList());

            Map<String, Object> data = new HashMap<>();
            data.put("labs", labVOs);
            data.put("totalCount", labVOs.size());

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取实验列表失败");
        }
    }

    // 1.1获取课程下的所有实验
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Map<String, Object>> getLabsByCourseId(@PathVariable int courseId) {
        try {
            Course course = courseService.getCourseById(courseId);
            if (course == null) {
                return ResponseUtil.error("课程不存在");
            }

            List<Lab> labs = courseService.getLabsByCourseId(courseId);
            List<LabVO> labVOs = labs.stream()
                .map(lab -> {
                    // 获取资源数量
                    List<LabResource> resources = courseService.getLabResourcesByLabId(lab.getId());
                    // 获取药材信息
                    HerbVO herbInfo = DataConvertUtil.createHerbVO(1); // 暂时写死
                    return DataConvertUtil.labToVO(lab, course, resources.size(), herbInfo);
                })
                .collect(Collectors.toList());

            Map<String, Object> courseInfo = new HashMap<>();
            courseInfo.put("courseId", courseId);
            courseInfo.put("courseName", course.getName());

            Map<String, Object> data = new HashMap<>();
            data.put("courseInfo", courseInfo);
            data.put("labs", labVOs);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取实验列表失败");
        }
    }

    // 1.2获得指定实验详情
    @GetMapping("/{labId}")
    public ResponseEntity<Map<String, Object>> getLabById(@PathVariable int labId) {
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("lab does not exist");
            }

            Course course = courseService.getCourseById(lab.getCourseId());
            List<LabResource> resources = courseService.getLabResourcesByLabId(labId);
            HerbVO herbInfo = DataConvertUtil.createHerbVO(1);
            LabVO labVO = DataConvertUtil.labToVO(lab, course, resources.size(), herbInfo);

            // 获取实验详情
            LabInfo labInfo = courseService.getLabInfoByLabId(labId);
            if (labInfo != null) {
                LabInfoVO labInfoVO = DataConvertUtil.labInfoToVO(labInfo, lab, course);
                labVO.setLabInfo(labInfoVO);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("lab", labVO);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            return ResponseUtil.error("获取实验详情失败");
        }
    }

    // 3.1新增实验信息
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addLab(@RequestBody LabDTO labDTO) {
        try {
            // 验证必填字段
            if (labDTO.getCourseId() <= 0) {
                return ResponseUtil.error("课程ID不能为空或无效");
            }
            if (labDTO.getUserId() <= 0) {
                return ResponseUtil.error("用户ID不能为空或无效");
            }
            if (labDTO.getLabEndTime() == null) {
                return ResponseUtil.error("实验结束时间不能为空");
            }

            Course course = courseService.getCourseById(labDTO.getCourseId());
            if (course == null) {
                return ResponseUtil.error("课程不存在: " + labDTO.getCourseId());
            }

            // 创建实验
            Lab lab = new Lab();
            lab.setCourseId(labDTO.getCourseId());
            lab.setUserId(labDTO.getUserId());
            lab.setSubmitTime(new Timestamp(System.currentTimeMillis()));
            lab.setEndTime(new Timestamp(labDTO.getLabEndTime().getTime()));
            lab.setDes(labDTO.getLabDes() != null ? labDTO.getLabDes() : "");

            if (!courseService.addLab(lab)) {
                return ResponseUtil.error("保存实验到数据库失败");
            }

            // 检查lab_id是否正确获取
            if (lab.getId() <= 0) {
                return ResponseUtil.error("实验ID获取失败，无法创建实验详情");
            }

            // 创建实验详情
            if (labDTO.getHerbId() > 0) {
                LabInfo labInfo = new LabInfo();
                labInfo.setLabId(lab.getId());
                labInfo.setHerbId(labDTO.getHerbId());
                labInfo.setGoal(labDTO.getLabInfoGoal() != null ? labDTO.getLabInfoGoal() : "");
                labInfo.setSummary(labDTO.getLabInfoSummary() != null ? labDTO.getLabInfoSummary() : "");
                labInfo.setIsValid(true);
                
                if (!courseService.addLabInfo(labInfo)) {
                    return ResponseUtil.error("保存实验详情失败，实验ID: " + lab.getId());
                }
            }

            // 返回完整的实验信息
            HerbVO herbInfo = DataConvertUtil.createHerbVO(labDTO.getHerbId());
            LabVO labVO = DataConvertUtil.labToVO(lab, course, 0, herbInfo);

            Map<String, Object> data = new HashMap<>();
            data.put("lab", labVO);

            return ResponseUtil.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("添加实验失败: " + e.getMessage());
        }
    }

    // 3.2删除实验信息
    @DeleteMapping("/{labId}")
    public ResponseEntity<Map<String, Object>> deleteLab(@PathVariable int labId) {
        try {
            Lab lab = courseService.getLabById(labId);
            if (lab == null) {
                return ResponseUtil.error("该实验不存在");
            }

            Course course = courseService.getCourseById(lab.getCourseId());
            if (courseService.deleteLabById(labId)) {
                Map<String, Object> deletedLab = new HashMap<>();
                deletedLab.put("labId", labId);
                deletedLab.put("labDes", lab.getDes());
                deletedLab.put("courseName", course != null ? course.getName() : "");

                Map<String, Object> data = new HashMap<>();
                data.put("deletedLab", deletedLab);

                return ResponseUtil.success(data);
            } else {
                return ResponseUtil.error("删除实验失败");
            }
        } catch (Exception e) {
            return ResponseUtil.error("删除实验失败");
        }
    }
} 