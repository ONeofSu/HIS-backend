package com.csu.research.service;

import com.csu.research.DTO.ContentDTO;
import com.csu.research.entity.Content;
import com.csu.research.entity.ContentBlock;
import com.csu.research.entity.ContentType;
import com.csu.research.entity.Topic;
import com.csu.research.vo.ContentVo;

import java.util.List;

public interface ContentService {
    //ContentCRUD
    Content addContent(Content content);
    boolean deleteContent(Long id);
    boolean recursionDeleteContent(Long id);
    boolean updateContent(Content content);
    Content getContent(Long id);
    List<Content> getAllContent(int size,int page);
    List<Content> searchContent(String query);
    boolean isContentExist(Long id);

    //ContentTypeCRUD
    ContentType getContentType(Long id);
    String getContentTypeNameById(Long id);
    Long getContentTypeIdByName(String contentTypeName);

    //ContentBlockBasicCRUD
    ContentBlock addContentBlock(ContentBlock contentBlock);
    ContentBlock getContentBlock(Long id);
    boolean deleteContentBlock(Long id);

    boolean updateContentBlock(ContentBlock contentBlock);
    List<ContentBlock> getAllContentBlockOnContent(Long contentId);


    //APPLY
    ContentVo transferToContentVo(Content content,boolean isSimple);
    List<ContentVo> transferToContentVo(List<Content> contentList,boolean isSimple);
    Content transferDTOToContent(ContentDTO contentDTO,int userId);
    List<ContentVo> findAllContentsOfOneTopic(Long topicId,boolean isSimple);     //根据课题id获取课题的所有资料
    List<ContentVo> findAllContentsOfOneTopicAndType(Long topicId,Long typeId,boolean isSimple);
    List<ContentVo> findAllSimpleContentsOfOneTeam(Long teamId);   //根据课题组id获取课题组所有资料简单信息
}
