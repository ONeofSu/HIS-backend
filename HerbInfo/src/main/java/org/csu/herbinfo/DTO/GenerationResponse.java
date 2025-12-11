package org.csu.herbinfo.DTO;

public record GenerationResponse(String response,String status) {
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(status);
    }
}
