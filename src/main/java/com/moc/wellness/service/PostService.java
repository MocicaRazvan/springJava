package com.moc.wellness.service;

import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.mapper.PostMapper;
import com.moc.wellness.model.Post;
import com.moc.wellness.repository.PostRepository;
import com.moc.wellness.service.generics.ApprovedService;

public interface PostService extends ApprovedService<Post, PostBody, PostResponse, PostRepository, PostMapper> {

}
