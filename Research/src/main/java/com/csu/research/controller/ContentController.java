package com.csu.research.controller;

import com.csu.research.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contents")
public class ContentController {
    @Autowired
    private ContentService contentService;

}
