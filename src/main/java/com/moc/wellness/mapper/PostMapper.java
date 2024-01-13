package com.moc.wellness.mapper;

import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.model.Post;
import com.moc.wellness.mapper.generics.DtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PostMapper extends DtoMapper<Post, PostBody, PostResponse> {

    @Override
    public void updateModelFromBody(PostBody body, Post post) {
        post.setTags(body.getTags());
        post.setTitle(body.getTitle());
        post.setBody(body.getBody());
        post.setApproved(false);
    }
}
