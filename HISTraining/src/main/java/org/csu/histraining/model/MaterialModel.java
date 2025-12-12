package org.csu.histraining.model;

import lombok.Data;
import org.csu.histraining.entity.Content;
import org.csu.histraining.entity.Material;

import java.util.ArrayList;
import java.util.List;

@Data
public class MaterialModel {
    private Material material;
    private List<Content> contents;

    public MaterialModel(Material material,List<Content> contents){
        this.material = material;
        this.contents = contents;   //浅拷贝即可
    }
}
