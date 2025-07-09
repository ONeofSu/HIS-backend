package org.csu.herbinfo.DTO;

import java.util.Objects;

public record GenerationRequest(String query) {
    public GenerationRequest {
        Objects.requireNonNull(query,"Query can not be null");
    }
}
