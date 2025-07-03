package org.csu.histraining.service;

import org.csu.histraining.entity.LiveRecord;

import java.util.List;

public interface LiveRecordService {
    public LiveRecord startRecord(Long roomId);
    public LiveRecord stopRecord(Long recordId);
    public List<LiveRecord> getRecordsByRoomId(Long roomId,int page,int size);
}
