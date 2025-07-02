package org.csu.hiscomment.controller;

import org.csu.hiscomment.DTO.CommentDTO;
import org.csu.hiscomment.VO.CommentVO;
import org.csu.hiscomment.entity.Comment;
import org.csu.hiscomment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class CommentController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private org.csu.hiscomment.feign.UserFeignClient userFeignClient;
    @Autowired
    private org.csu.hiscomment.feign.CourseFeignClient courseFeignClient;
    @Autowired
    private org.csu.hiscomment.feign.HerbFeignClient herbFeignClient;

    // 发布评论/回复
    @PostMapping("/comments")
    public Map<String, Object> addComment(@RequestBody CommentDTO dto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        Map<String, Object> result = new HashMap<>();
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return result;
        }
        if ("course".equals(dto.getTargetType()) && !courseFeignClient.isCourseExist(dto.getTargetId())) {
            result.put("code", -1);
            result.put("message", "课程不存在");
            return result;
        }
        if ("herb".equals(dto.getTargetType())) {
            try {
                boolean exist = herbFeignClient.isHerbExist(dto.getTargetId());
                if (!exist) {
                    result.put("code", -1);
                    result.put("message", "中药不存在");
                    return result;
                }
            } catch (Exception e) {
                result.put("code", -1);
                result.put("message", "中药服务异常，请稍后重试");
                return result;
            }
        }
        // 回复评论时校验parentId对应评论的targetType/targetId一致
        if (dto.getParentId() != 0) {
            Comment parent = commentService.getCommentById(dto.getParentId());
            if (parent == null || parent.getIsDeleted() == 1) {
                result.put("code", -1);
                result.put("message", "被回复的评论不存在");
                return result;
            }
            if (!parent.getTargetType().equals(dto.getTargetType()) || parent.getTargetId() != dto.getTargetId()) {
                result.put("code", -1);
                result.put("message", "目标对象不一致，无法回复");
                return result;
            }
        }
        CommentVO vo = commentService.addComment(dto, userId);
        if (vo != null) {
            result.put("code", 0);
            result.put("data", vo);
        } else {
            result.put("code", -1);
            result.put("message", "评论发布失败");
        }
        return result;
    }

    // 获取评论列表
    @GetMapping("/comments")
    public Map<String, Object> listComments(@RequestParam String targetType,
                                            @RequestParam int targetId,
                                            @RequestParam(defaultValue = "new") String sort,
                                            @RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        Map<String, Object> result = new HashMap<>();
        // 目标对象存在性校验
        if ("herb".equals(targetType)) {
            try {
                boolean exist = herbFeignClient.isHerbExist(targetId);
                if (!exist) {
                    result.put("code", -1);
                    result.put("message", "中药不存在");
                    return result;
                }
            } catch (Exception e) {
                result.put("code", -1);
                result.put("message", "中药服务异常，请稍后重试");
                return result;
            }
        }
        if ("course".equals(targetType)) {
            try {
                boolean exist = courseFeignClient.isCourseExist(targetId);
                if (!exist) {
                    result.put("code", -1);
                    result.put("message", "课程不存在");
                    return result;
                }
            } catch (Exception e) {
                result.put("code", -1);
                result.put("message", "课程服务异常，请稍后重试");
                return result;
            }
        }
        // 校验userId存在性
        if (userId != -1 && !userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return result;
        }
        List<CommentVO> list = commentService.listComments(targetType, targetId, sort, page, size, userId);
        result.put("code", 0);
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        data.put("page", page);
        data.put("size", size);
        data.put("total", list != null ? list.size() : 0); // 可根据实际分页返回总数
        result.put("data", data);
        return result;
    }

    // 点赞评论
    @PostMapping("/comments/{commentId}/like")
    public Map<String, Object> likeComment(@PathVariable int commentId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        Map<String, Object> result = new HashMap<>();
        // 校验用户是否存在
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return result;
        }
        if (commentService.getCommentDetail(commentId, null) == null) {
            result.put("code", -1);
            result.put("message", "评论不存在");
            return result;
        }
        boolean ok = commentService.likeComment(commentId, userId);
        if (ok) {
            result.put("code", 0);
        } else {
            result.put("code", -1);
            result.put("message", "已点赞或点赞失败");
        }
        return result;
    }

    // 取消点赞
    @DeleteMapping("/comments/{commentId}/like")
    public Map<String, Object> unlikeComment(@PathVariable int commentId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        Map<String, Object> result = new HashMap<>();
        // 校验用户是否存在
        if (!userFeignClient.isUserExist(userId)) {
            result.put("code", -1);
            result.put("message", "用户不存在");
            return result;
        }
        if (commentService.getCommentDetail(commentId, null) == null) {
            result.put("code", -1);
            result.put("message", "评论不存在");
            return result;
        }
        boolean ok = commentService.unlikeComment(commentId, userId);
        if (ok) {
            result.put("code", 0);
        } else {
            result.put("code", -1);
            result.put("message", "未点赞或取消失败");
        }
        return result;
    }

    // 删除评论
    @DeleteMapping("/comments/{commentId}")
    public Map<String, Object> deleteComment(@PathVariable int commentId, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        Map<String, Object> result = new HashMap<>();
        boolean isAdmin = false;
        try {
            isAdmin = userFeignClient.isUserAdmin(userId);
        } catch (Exception e) {
            result.put("code", -1);
            result.put("message", "用户服务异常，请稍后重试");
            return result;
        }
        boolean ok = commentService.deleteComment(commentId, userId, isAdmin);
        if (ok) {
            result.put("code", 0);
        } else {
            result.put("code", -1);
            result.put("message", "无权限或评论不存在");
        }
        return result;
    }

    // 获取单条评论详情
    @GetMapping("/comments/{commentId}")
    public Map<String, Object> getCommentDetail(@PathVariable int commentId, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = authHeader.substring(7);
        int userId = userFeignClient.getUserIdByToken(token);
        Map<String, Object> result = new HashMap<>();
        CommentVO vo = commentService.getCommentDetail(commentId, userId);
        if (vo != null) {
            result.put("code", 0);
            result.put("data", vo);
        } else {
            result.put("code", -1);
            result.put("message", "评论不存在");
        }
        return result;
    }
} 