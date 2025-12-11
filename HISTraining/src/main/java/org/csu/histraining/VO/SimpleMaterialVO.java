package org.csu.histraining.VO;

import lombok.Data;
import org.csu.histraining.entity.Material;

@Data
public class SimpleMaterialVO extends Material {
    private String username;
    private String herbName;
}
