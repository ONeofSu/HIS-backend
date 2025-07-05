package com.csu.research.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csu.research.DTO.ContentDTO;
import com.csu.research.entity.Content;
import com.csu.research.entity.ContentBlock;
import com.csu.research.entity.ContentType;
import com.csu.research.entity.Topic;
import com.csu.research.mapper.*;
import com.csu.research.service.AuthService;
import com.csu.research.service.ContentService;
import com.csu.research.service.TeamService;
import com.csu.research.service.TopicService;
import com.csu.research.vo.ContentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    ContentMapper contentMapper;
    @Autowired
    ContentBlockMapper contentBlockMapper;
    @Autowired
    ContentTypeMapper contentTypeMapper;
    @Autowired
    AuthService authService;
    @Autowired
    TopicService topicService;

    //--------------------------------------------------Content-------------------------------------------------------
    @Override
    public Content addContent(Content content) {
        content.setContentTime(LocalDateTime.now());
        content.setContentIsValid(true);
        contentMapper.insert(content);
        return content;
    }

    @Override
    public boolean deleteContent(Long id) {
        Content content = contentMapper.selectById(id);
        content.setContentIsValid(false);
        contentMapper.updateById(content);
        return true;
    }

    @Override
    public boolean recursionDeleteContent(Long id) {
        List<ContentBlock> contentBlocks = getAllContentBlockOnContent(id);
        for (ContentBlock contentBlock : contentBlocks) {
            deleteContentBlock(contentBlock.getContentBlockId());
        }
        deleteContent(id);
        return true;
    }

    @Override
    public Content getContent(Long id) {
        Content content = contentMapper.selectById(id);
        if (content == null || content.isContentIsValid() == false) {
            return null;
        }
        return content;
    }

    @Override
    public boolean updateContent(Content content) {
        content.setContentIsValid(true);
        contentMapper.updateById(content);
        return true;
    }

    @Override
    public List<Content> getAllContent(int page,int size) {
        Page<Content> pageParma =new Page<>(page,size);
        QueryWrapper<Content> wrapper = new QueryWrapper<>();
        wrapper.eq("content_is_valid", 1);
        return contentMapper.selectPage(pageParma,wrapper).getRecords();
    }

    @Override
    public boolean isContentExist(Long id) {
        Content content = contentMapper.selectById(id);
        if (content == null || content.isContentIsValid() == false) {
            return false;
        }
        return true;
    }

    //--------------------------------------------ContentType--------------------------------------------------------
    @Override
    public ContentType getContentType(Long id) {
        return contentTypeMapper.selectById(id);
    }

    @Override
    public String getContentTypeNameById(Long id) {
        ContentType contentType = contentTypeMapper.selectById(id);
        if (contentType == null) {
            return null;
        }
        return contentType.getContentTypeName();
    }

    @Override
    public Long getContentTypeIdByName(String name) {
        QueryWrapper<ContentType> wrapper = new QueryWrapper<>();
        wrapper.eq("content_type_name", name);
        ContentType contentType = contentTypeMapper.selectOne(wrapper);
        if (contentType == null) {
            return null;
        }
        return contentType.getContentTypeId();
    }

    //---------------------------------------------ContentBlock------------------------------------------------------

    @Override
    public ContentBlock addContentBlock(ContentBlock contentBlock) {
        contentBlock.setContentBlockIsValid(true);
        contentBlockMapper.insert(contentBlock);
        return contentBlock;
    }

    @Override
    public boolean deleteContentBlock(Long id) {
        ContentBlock contentBlock = contentBlockMapper.selectById(id);
        contentBlock.setContentBlockIsValid(false);
        contentBlockMapper.updateById(contentBlock);
        return true;
    }

    @Override
    public ContentBlock getContentBlock(Long id) {
        ContentBlock contentBlock = contentBlockMapper.selectById(id);
        if (contentBlock == null || !contentBlock.isContentBlockIsValid()) {
            return null;
        }
        return contentBlock;
    }

    @Override
    public boolean updateContentBlock(ContentBlock contentBlock) {
        contentBlock.setContentBlockIsValid(true);
        contentBlockMapper.updateById(contentBlock);
        return true;
    }

    @Override
    public List<ContentBlock> getAllContentBlockOnContent(Long contentId) {
        QueryWrapper<ContentBlock> wrapper = new QueryWrapper<>();
        wrapper.eq("content_id", contentId).eq("content_block_isvalid", true)
                .orderByAsc("content_block_order");
        return contentBlockMapper.selectList(wrapper);
    }

    //-------------------------------------------APPLY-------------------------------------------------------

    @Override
    public ContentVo transferToContentVo(Content content,boolean isSimple) {
        ContentVo contentVo = new ContentVo();

        contentVo.setContentId(content.getContentId());
        contentVo.setContentName(content.getContentName());
        contentVo.setContentDes(content.getContentDes());
        contentVo.setContentTime(content.getContentTime());
        contentVo.setContentIsValid(content.isContentIsValid());

        if(!isSimple){
            contentVo.setContentBlocks(getAllContentBlockOnContent(content.getContentId()));
        }

        contentVo.setContentTypeId(content.getContentTypeId());
        contentVo.setContentTypeName(getContentTypeNameById(content.getContentTypeId()));

        contentVo.setTopicId(content.getTopicId());
        contentVo.setTopicName(topicService.getTopicById(content.getTopicId()).getTopicName());

        contentVo.setUserId(content.getUserId());
        return contentVo;
    }

    @Override
    public List<ContentVo> transferToContentVo(List<Content> contentList, boolean isSimple) {
        List<ContentVo> contentVoList = new ArrayList<>();
        for (Content content : contentList) {
            ContentVo contentVo = transferToContentVo(content,isSimple);
            contentVoList.add(contentVo);
        }
        return contentVoList;
    }

    @Override
    public Content transferDTOToContent(ContentDTO contentDTO,int userId) {
        Content content = new Content();
        content.setTopicId(contentDTO.getTopicId());
        content.setContentName(contentDTO.getContentName());
        content.setContentDes(contentDTO.getContentDes());
        content.setUserId(userId);

        Long contentTypeId = getContentTypeIdByName(contentDTO.getContentType());
        content.setContentTypeId(contentTypeId);

        Long authId= authService.getAuthByName(contentDTO.getAuth()).getAuthId();
        content.setAuthId(authId);

        content.setContentIsValid(true);

        return content;
    }

    @Override
    public List<ContentVo> findAllContentsOfOneTopic(Long teamId,boolean isSimple) {
        List<Content> contents = contentMapper.selectList(new QueryWrapper<Content>().eq("topic_id", teamId));
        return transferToContentVo(contents,true);
    }

    @Override
    public List<ContentVo> findAllContentsOfOneTopicAndType(Long topicId, Long typeId, boolean isSimple) {
        QueryWrapper<Content> wrapper = new QueryWrapper<>();
        wrapper.eq("topic_id", topicId).eq("content_type_id", typeId);
        List<Content> contents = contentMapper.selectList(wrapper);
        return transferToContentVo(contents,isSimple);
    }

    @Override
    public List<ContentVo> findAllSimpleContentsOfOneTeam(Long teamId) {
        List<Topic> topics = topicService.getAllTopicsByTeamId(teamId);
        System.out.println(topics);
        List<ContentVo> result = new ArrayList<>();
        for (Topic topic : topics) {
            List<ContentVo> contentVos = findAllContentsOfOneTopic(topic.getTopicId(),true);
            System.out.println(contentVos);
            result.addAll(contentVos);
        }
        return result;
    }
}
