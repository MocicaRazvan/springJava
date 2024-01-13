package com.moc.wellness.service.impl;

import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.mapper.PostMapper;
import com.moc.wellness.model.Post;
import com.moc.wellness.repository.PostRepository;
import com.moc.wellness.service.PostService;
import com.moc.wellness.service.impl.generics.ApprovedServiceImpl;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.UserUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostImplService
        extends ApprovedServiceImpl<Post, PostBody, PostResponse, PostRepository, PostMapper>
        implements PostService {
    public PostImplService(PostRepository modelRepository, PostMapper modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, EntitiesUtils entitiesUtils) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, "post", entitiesUtils);
    }


}
