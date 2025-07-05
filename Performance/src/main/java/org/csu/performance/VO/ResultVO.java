package org.csu.performance.VO;

import lombok.Data;

/**
 * 统一返回结果VO
 */
@Data
public class ResultVO<T> {

    /**
     * 状态码：0-成功，-1-失败
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public ResultVO() {
    }

    public ResultVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultVO(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回
     */
    public static <T> ResultVO<T> success() {
        return new ResultVO<>(0, "操作成功");
    }

    /**
     * 成功返回
     */
    public static <T> ResultVO<T> success(String message) {
        return new ResultVO<>(0, message);
    }

    /**
     * 成功返回
     */
    public static <T> ResultVO<T> success(String message, T data) {
        return new ResultVO<>(0, message, data);
    }

    /**
     * 失败返回
     */
    public static <T> ResultVO<T> error(String message) {
        return new ResultVO<>(-1, message);
    }

    /**
     * 失败返回
     */
    public static <T> ResultVO<T> error(Integer code, String message) {
        return new ResultVO<>(code, message);
    }
} 