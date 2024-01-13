package com.moc.wellness.services;

import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.CommentMapper;
import com.moc.wellness.model.Comment;
import com.moc.wellness.repository.CommentRepository;
import com.moc.wellness.repository.PostRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.impl.CommentServiceImpl;
import com.moc.wellness.setup.CommentsSetup;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTests extends CommentsSetup {
    @Mock
    private EntitiesUtils entitiesUtils;
    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private PageableUtilsCustom pageableUtils;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    @DisplayName("Create comment success")
    public void createCommentSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(commentMapper.fromBodyToModel(commentBody)).thenReturn(strippedComment);
        when(commentRepository.save(strippedComment)).then(invocation -> {
            Comment arg = invocation.getArgument(0);
            Assertions.assertEquals(arg.getUser(), userUser);
            Assertions.assertEquals(arg.getPost(), post);
            return strippedComment;
        });
        when(commentMapper.fromModelToResponse(strippedComment)).thenReturn(commentResponse);

        CommentResponse made = commentService.createModel(postId, commentBody);

        Assertions.assertEquals(made, commentResponse);

        verify(userUtils).getPrincipal();
        verify(postRepository).findById(postId);
        verify(commentMapper).fromBodyToModel(commentBody);
        verify(commentRepository).save(strippedComment);
        verify(commentMapper).fromModelToResponse(strippedComment);
    }

    @Test
    @DisplayName("Create comment post not found")
    public void createCommentPostNotFound() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(postRepository.findById(postId)).thenThrow(new NotFoundEntity("post", postId));

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> commentService.createModel(postId, commentBody));

        Assertions.assertEquals(ex.getName(), "post");
        Assertions.assertEquals(ex.getId(), postId);

        verify(userUtils).getPrincipal();
        verify(postRepository).findById(postId);
        verify(commentMapper, times(0)).fromBodyToModel(commentBody);
        verify(commentRepository, times(0)).save(strippedComment);
        verify(commentMapper, times(0)).fromModelToResponse(strippedComment);
    }

    @Test
    @DisplayName("Get comments by post success")
    public void getCommentsByPostSuccess() {
        Page<Comment> mockPage = new PageImpl<>(List.of(comment), pageRequest, 1);
        when(postRepository.existsByIdAndApproved(postId)).thenReturn(true);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(commentRepository.findAllByPostId(postId, pageRequest)).thenReturn(mockPage);
        when(commentMapper.fromModelToResponse(comment)).thenReturn(commentResponse);

        PageableResponse<List<CommentResponse>> made =
                commentService.getCommentByPost(postId, pageableBody);

        Assertions.assertEquals(pageableResponse, made);
        verify(postRepository).existsByIdAndApproved(postId);
        verify(pageableUtils).createPageRequest(pageableBody);
        verify(commentRepository).findAllByPostId(postId, pageRequest);
        verify(commentMapper).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Get comments by post not found post")
    public void getCommentsByPostNotFoundPost() {
        when(postRepository.existsByIdAndApproved(postId)).thenReturn(false);

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> commentService.getCommentByPost(postId, pageableBody));

        Assertions.assertEquals(ex.getId(), postId);
        Assertions.assertEquals(ex.getName(), "post");

        verify(postRepository).existsByIdAndApproved(postId);
        verify(pageableUtils, times(0)).createPageRequest(any());
        verify(commentRepository, times(0)).findAllByPostId(any(), any());
        verify(commentMapper, times(0)).fromModelToResponse(any());
    }

    @Test
    @DisplayName("Update comment success")
    public void updateCommentSuccess() {
        commentResponse.setTitle(commentBody.getTitle());
        commentResponse.setBody(commentBody.getBody());
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
        when(commentRepository.save(comment)).then(invocation -> {
            Comment arg = invocation.getArgument(0);
            Assertions.assertEquals(arg.getBody(), commentBody.getBody());
            Assertions.assertEquals(arg.getTitle(), commentBody.getTitle());
            return comment;
        });
        when(commentMapper.fromModelToResponse(comment)).then(invocation -> {
            Comment arg = invocation.getArgument(0);
            Assertions.assertEquals(arg.getUser(), userUser);
            Assertions.assertEquals(arg.getPost(), post);
            return CommentResponse.builder().id(arg.getId())
                    .title(arg.getTitle())
                    .body(arg.getBody())
                    .user(userDto)
                    .post(postResponse)
                    .build();
        });

        CommentResponse made =
                commentService.updateModel(commentId, commentBody);

        Assertions.assertEquals(made, commentResponse);

        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
        verify(commentMapper).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Update comment comment not found")
    public void updateCommentNotFound() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(commentRepository.findById(commentId)).thenThrow(new NotFoundEntity("comment", commentId));


        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> commentService.updateModel(commentId, commentBody));

        Assertions.assertEquals(ex.getName(), "comment");
        Assertions.assertEquals(ex.getId(), commentId);

        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(commentRepository, times(0)).save(comment);
        verify(commentMapper, times(0)).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Update comment comment not author")
    public void updateCommentNotAuthor() {
        when(userUtils.getPrincipal()).thenReturn(userAdmin);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));


        Assertions.assertThrows(PrivateRouteException.class,
                () -> commentService.updateModel(commentId, commentBody));


        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(commentRepository, times(0)).save(comment);
        verify(commentMapper, times(0)).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Delete comment success owner")
    public void deleteCommentSuccessOwner() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
        doNothing().when(commentRepository).delete(comment);
        when(commentMapper.fromModelToResponse(comment)).thenReturn(commentResponse);

        CommentResponse made = commentService.deleteModel(commentId);

        Assertions.assertEquals(made, commentResponse);

        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(commentRepository).delete(comment);
        verify(commentMapper).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Delete comment success admin")
    public void deleteCommentSuccessAdmin() {
        when(userUtils.getPrincipal()).thenReturn(userAdmin);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
        doNothing().when(commentRepository).delete(comment);
        when(commentMapper.fromModelToResponse(comment)).thenReturn(commentResponse);

        CommentResponse made = commentService.deleteModel(commentId);

        Assertions.assertEquals(made, commentResponse);

        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(commentRepository).delete(comment);
        verify(commentMapper).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Delete comment not authorized")
    public void deleteCommentNotAuthorized() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));

        Assertions.assertThrows(PrivateRouteException.class,
                () -> commentService.deleteModel(commentId));

        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(commentRepository, times(0)).delete(comment);
        verify(commentMapper, times(0)).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("React to comment")
    public void reactToComment() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
        doNothing().when(entitiesUtils).setReaction(comment, userTrainer, "like");
        doNothing().when(entitiesUtils).setReaction(comment, userTrainer, "dislike");
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.fromModelToResponse(comment)).thenReturn(commentResponse);
        List.of("like", "dislike").forEach(t -> {

            CommentResponse made = commentService.reactToModel(commentId, t);

            Assertions.assertEquals(made, commentResponse);

        });


        verify(userUtils, times(2)).getPrincipal();
        verify(commentRepository, times(2)).findById(commentId);
        verify(commentRepository, times(2)).save(comment);
        verify(entitiesUtils).setReaction(comment, userTrainer, "like");
        verify(entitiesUtils).setReaction(comment, userTrainer, "dislike");
        verify(commentMapper, times(2)).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Get comment by id success")
    public void getCommentByIdSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(comment));
        when(userUtils.hasPermissionToModifyEntity(userUser, userId)).thenReturn(true);
        when(commentMapper.fromModelToResponse(comment)).thenReturn(commentResponse);

        CommentResponse exp = commentService.getModelById(commentId);

        Assertions.assertEquals(exp, commentResponse);

        verify(userUtils).getPrincipal();
        verify(commentRepository).findById(commentId);
        verify(userUtils).hasPermissionToModifyEntity(userUser, userId);
        verify(commentMapper).fromModelToResponse(comment);
    }

    @Test
    @DisplayName("Get comments by user success")
    public void getCommentsByUserSuccess() {
        Page<Comment> mockPage = new PageImpl<>(List.of(comment), pageRequest, 1);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(commentRepository.findAllByUserId(userId, pageRequest)).thenReturn(mockPage);
        when(commentMapper.fromModelToResponse(comment)).thenReturn(commentResponse);

        PageableResponse<List<CommentResponse>> exp =
                commentService.getModelByUser(userId, pageableBody);

        Assertions.assertEquals(exp, pageableResponse);

        verify(userRepository).existsById(userId);
        verify(pageableUtils).createPageRequest(pageableBody);
        verify(commentRepository).findAllByUserId(userId, pageRequest);
        verify(commentMapper).fromModelToResponse(comment);
    }


    @Test
    @DisplayName("Get comments by user not found user")
    public void getCommentsByUserNotFoundUser() {
        when(userRepository.existsById(userId)).thenReturn(false);

        NotFoundEntity ex = Assertions.assertThrows(NotFoundEntity.class,
                () -> commentService.getModelByUser(userId, pageableBody));

        Assertions.assertEquals(ex.getId(), userId);
        Assertions.assertEquals(ex.getName(), "user");

        verify(userRepository).existsById(userId);
        verify(pageableUtils, times(0)).createPageRequest(any());
        verify(commentRepository, times(0)).findAllByUserId(any(), any());
        verify(commentMapper, times(0)).fromModelToResponse(any());
    }


}
