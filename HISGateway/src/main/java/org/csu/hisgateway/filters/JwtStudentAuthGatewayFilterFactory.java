package org.csu.hisgateway.filters;

import org.csu.hisgateway.utils.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtStudentAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<JwtStudentAuthGatewayFilterFactory.Config> {
    private final JwtUtil jwtUtil;

    JwtStudentAuthGatewayFilterFactory(JwtUtil jwtUtil) {
        super(JwtStudentAuthGatewayFilterFactory.Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(JwtStudentAuthGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            //请求头获取Token
            String token = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            //格式是否合法
            if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            //提取Token
            token = token.substring(7);

            //验证有效
            if (!jwtUtil.validateToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)  // 保留原始 Token
                    .build();

            //提取权限并验证
            Integer userRoleLevel = jwtUtil.extractRoleLevel(token);
            if (userRoleLevel == null) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            if(userRoleLevel<1){
                //System.out.println("userRoleLevel:"+userRoleLevel);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            //继续路由
            return chain.filter(exchange.mutate().request(request).build());
        };
    }

    public static class Config {
    }
}
