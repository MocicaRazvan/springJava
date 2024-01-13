package com.moc.wellness.service;

import com.moc.wellness.dto.comment.CommentBody;
import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.mapper.CommentMapper;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.CommentRepository;
import com.moc.wellness.service.generics.ManyToOneUserService;
import com.moc.wellness.service.generics.TitleBodyService;

import java.util.List;

public interface CommentService
        extends TitleBodyService<Comment, CommentBody, CommentResponse, CommentRepository, CommentMapper> {
    CommentResponse createModel(Long postId, CommentBody commentBody);

    PageableResponse<List<CommentResponse>> getCommentByPost(Long postId, PageableBody pageableBody);

    PageableResponse<List<CommentResponse>> getModelByUser(Long userId, PageableBody pageableBody);


}
