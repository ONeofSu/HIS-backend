package org.csu.histraining.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpUtil {
    /**
     * 将查询字符串解析为Map
     * @param query 查询字符串，如 "name=value&key=value"
     * @param charset 字符集
     * @return 参数Map
     */
    public static Map<String, String> decodeParamMap(String query, String charset) {
        Map<String, String> params = new LinkedHashMap<>();
        if (query == null || query.isEmpty()) {
            return params;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            try {
                String key = URLDecoder.decode(kv[0], charset);
                String value = kv.length == 2 ? URLDecoder.decode(kv[1], charset) : "";
                params.put(key, value);
            } catch (UnsupportedEncodingException e) {
                // 处理解码异常
                throw new RuntimeException("Failed to decode query parameter", e);
            }
        }
        return params;
    }

    /**
     * 使用UTF-8字符集解码查询字符串
     */
    public static Map<String, String> decodeParamMap(String query) {
        return decodeParamMap(query, StandardCharsets.UTF_8.name());
    }
}
