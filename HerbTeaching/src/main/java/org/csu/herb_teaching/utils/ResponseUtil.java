package org.csu.herb_teaching.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUtil<T> {

    private int code;
    private String message;
    private T data;

    public ResponseUtil(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseUtil(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
    }
} 