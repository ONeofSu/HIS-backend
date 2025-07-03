package com.csu.research.service;

import com.csu.research.entity.Content;
import com.csu.research.entity.ContentBlock;
import com.csu.research.entity.ContentType;
import com.csu.research.entity.Topic;
import com.csu.research.vo.ContentVo;

import java.util.List;

public interface ContentService {

    ContentVo transferToContentVo(Content content,
                                  List< ContentBlock> contentBlocks,
                                  ContentType contentType,
                                  Topic topic);

    // 根据课题组id获取课题组的所有资料
    List<ContentVo> findAllContentsOfOneTeam(Long teamId);
}
