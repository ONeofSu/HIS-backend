package org.csu.hiscomment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableFeignClients
public class HisCommentApplication {

	public static void main(String[] args) {
		SpringApplication.run(HisCommentApplication.class, args);
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {
			RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
			if (requestAttributes instanceof ServletRequestAttributes) {
				HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
				String token = request.getHeader("Authorization");
				if (StringUtils.hasText(token)) {
					template.header("Authorization", token);
				}
			}
		};
	}

}
