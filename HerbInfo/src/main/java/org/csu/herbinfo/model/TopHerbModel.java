package org.csu.herbinfo.model;

import lombok.Data;

@Data
public class TopHerbModel {
    private int herbId;
    private int count;

    public TopHerbModel(int herbId, int count) {
        this.herbId = herbId;
        this.count = count;
    }
}
