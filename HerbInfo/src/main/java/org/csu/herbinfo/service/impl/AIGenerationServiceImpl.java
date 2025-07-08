package org.csu.herbinfo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.csu.herbinfo.DTO.ErrorResponse;
import org.csu.herbinfo.DTO.GenerationRequest;
import org.csu.herbinfo.DTO.GenerationResponse;
import org.csu.herbinfo.config.AIServiceConfig;
import org.csu.herbinfo.service.AIGenerationService;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AIGenerationServiceImpl implements AIGenerationService {
    private final RestTemplate restTemplate;
    private final AIServiceConfig config;

    public AIGenerationServiceImpl(RestTemplate restTemplate, AIServiceConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Override
    public String generateText(String query) {
        String url = config.baseUrl() + "/generate";
        GenerationRequest request = new GenerationRequest(query);

        try {
            GenerationResponse response = restTemplate.postForObject(
                    url,
                    request,
                    GenerationResponse.class
            );

            if (response == null || !response.isSuccess()) {
                throw new IllegalStateException("AI service returned unsuccessful response");
            }

            return response.response();

        } catch (HttpClientErrorException e) {
            // 处理4xx错误
            ErrorResponse error = parseErrorResponse(e);
            throw new IllegalArgumentException("Invalid request to AI service: " + error.error());

        } catch (HttpServerErrorException e) {
            // 处理5xx错误（会触发重试）
            ErrorResponse error = parseErrorResponse(e);
            throw new RuntimeException("AI service error: " + error.error());
        }
    }

    private ErrorResponse parseErrorResponse(HttpStatusCodeException e) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(e.getResponseBodyAsByteArray(), ErrorResponse.class);
        } catch (Exception ex) {
            return new ErrorResponse(e.getStatusText(), "error");
        }
    }
}
