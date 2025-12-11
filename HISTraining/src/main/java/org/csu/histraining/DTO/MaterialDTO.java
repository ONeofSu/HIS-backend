package org.csu.histraining.DTO;

import lombok.Data;

import java.util.List;

@Data
public class MaterialDTO {
    private String title;
    private String type;
    private String des;
    private String herbName;
    private List<ContentDTO> contents;
}
