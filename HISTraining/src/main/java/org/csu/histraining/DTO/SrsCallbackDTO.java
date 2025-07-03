package org.csu.histraining.DTO;

import lombok.Data;

import java.util.Map;

@Data
public class SrsCallbackDTO {
    /**
     * SRS服务器ID
     */
    private String serverId;

    /**
     * 应用名称
     */
    private String app;

    /**
     * 流名称
     */
    private String stream;

    /**
     * 回调参数（URL编码的参数字符串）
     */
    private String param;

    /**
     * 客户端IP
     */
    private String ip;

    /**
     * 客户端端口
     */
    private Integer port;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 页面URL
     */
    private String pageUrl;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 回调动作类型
     * on_publish/on_unpublish/on_play/on_stop/on_dvr
     */
    private String action;

    /**
     * DVR录制文件路径（仅on_dvr回调时有效）
     */
    private String file;

    /**
     * 推流/播放token（从param解析）
     */
    private String token;

    /**
     * token过期时间（从param解析）
     */
    private String expire;

    /**
     * 其他自定义参数（从param解析的额外参数）
     */
    private Map<String, String> extraParams;
}
