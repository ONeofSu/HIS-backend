package com.csu.research.service.impl;

import com.csu.research.entity.Content;
import com.csu.research.entity.ContentBlock;
import com.csu.research.entity.ContentType;
import com.csu.research.entity.Topic;
import com.csu.research.mapper.ContentMapper;
import com.csu.research.service.ContentService;
import com.csu.research.vo.ContentVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ContentServiceImpl implements ContentService {


    @Autowired
    ContentMapper contentMapper;


    @Override
    public ContentVo transferToContentVo(Content content,
                                         List<ContentBlock> contentBlocks,
                                         ContentType contentType,
                                         Topic topic) {
        ContentVo contentVo = new ContentVo();

        contentVo.setContentId(content.getContentId());
        contentVo.setContentName(content.getContentName());
        contentVo.setContentDes(content.getContentDes());
        contentVo.setContentTime(content.getContentTime());
        contentVo.setContentIsValid(content.getContentIsValid());

        contentVo.setContentBlocks(contentBlocks);

        contentVo.setContentTypeId(contentType.getContentTypeId());
        contentVo.setContentTypeName(contentType.getContentTypeName());

        contentVo.setTopicId(topic.getTopicId());
        contentVo.setTopicName(topic.getTopicName());

        contentVo.setUserId(content.getUserId());
        return contentVo;
    }

    @Override
    public List<ContentVo> findAllContentsOfOneTeam(Long teamId) {

    }
}
