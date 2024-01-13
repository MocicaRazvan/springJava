package com.moc.wellness.mapper;

import com.moc.wellness.dto.comment.CommentBody;
import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CommentMapper extends DtoMapper<Comment,CommentBody,CommentResponse> {

    public abstract Comment fromBodyToModel(CommentBody body);

    public abstract  CommentResponse fromModelToResponse(Comment comment);
    @Override
    public  void updateModelFromBody(CommentBody  body,Comment comment){
        comment.setBody(body.getBody());
        comment.setTitle(body.getTitle());
    }
}
