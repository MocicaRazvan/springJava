package com.moc.wellness.controller;

import com.moc.wellness.controller.generic.ApproveController;
import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.mapper.PostMapper;
import com.moc.wellness.model.Post;
import com.moc.wellness.repository.PostRepository;
import com.moc.wellness.service.PostService;
import com.moc.wellness.service.generics.ApprovedService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts Controller")
public class PostController extends ApproveController
        <Post, PostBody, PostResponse,
                PostRepository, PostMapper,
                ApprovedService<Post, PostBody, PostResponse,
                        PostRepository, PostMapper>> {
    public PostController(PostService modelService) {
        super(modelService, "post");
    }
}
