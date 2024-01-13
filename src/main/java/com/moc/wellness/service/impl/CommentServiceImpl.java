package com.moc.wellness.service.impl;

import com.moc.wellness.dto.comment.CommentBody;
import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.CommentMapper;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.Post;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.CommentRepository;
import com.moc.wellness.repository.PostRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.CommentService;
import com.moc.wellness.service.impl.generics.ManyToOneUserServiceImpl;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service


public class CommentServiceImpl
        extends ManyToOneUserServiceImpl<Comment, CommentBody, CommentResponse, CommentRepository, CommentMapper>
        implements CommentService {

    private final EntitiesUtils entitiesUtils;
    private final PostRepository postRepository;


    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository modelRepository, CommentMapper modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, EntitiesUtils entitiesUtils, PostRepository postRepository, UserRepository userRepository) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, "comment");
        this.entitiesUtils = entitiesUtils;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public CommentResponse createModel(Long postId, CommentBody commentBody) {
        UserCustom authUser = userUtils.getPrincipal();
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundEntity("post", postId));
        Comment comment = modelMapper.fromBodyToModel(commentBody);
        comment.setPost(post);
        comment.setUser(authUser);
        return modelMapper.fromModelToResponse(modelRepository.save(comment));
    }

    @Override
    public PageableResponse<List<CommentResponse>> getCommentByPost(Long postId, PageableBody pageableBody) {
        if (!postRepository.existsByIdAndApproved(postId)) {
            throw new NotFoundEntity("post", postId);
        }

        Page<Comment> page = modelRepository.findAllByPostId(postId, pageableUtils.createPageRequest(pageableBody));

        return PageableResponse.<List<CommentResponse>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList()))
                .build();


    }

    @Override
    public PageableResponse<List<CommentResponse>> getModelByUser(Long userId, PageableBody pageableBody) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntity("user", userId);
        }

        Page<Comment> page = modelRepository.findAllByUserId(userId, pageableUtils.createPageRequest(pageableBody));

        return PageableResponse.<List<CommentResponse>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public CommentResponse updateModel(Long id, CommentBody commentBody) {
        UserCustom authUser = userUtils.getPrincipal();

        Comment comment = getModel(id);
        if (isNotAuthor(comment, authUser)) {
            throw new PrivateRouteException();
        }
        comment.setBody(commentBody.getBody());
        comment.setTitle(commentBody.getTitle());
        Comment savedComment = modelRepository.save(comment);
        return modelMapper.fromModelToResponse(savedComment);
    }

    @Override
    @Transactional
    public CommentResponse deleteModel(Long id) {
        UserCustom authUser = userUtils.getPrincipal();

        Comment comment = getModel(id);
        if (isNotAuthor(comment, authUser) && !(authUser.getRole() == Role.ADMIN)) {
            throw new PrivateRouteException();
        }

        modelRepository.delete(comment);

        return modelMapper.fromModelToResponse(comment);
    }

    @Override
    @Transactional
    public CommentResponse reactToModel(Long id, String type) {
        UserCustom authUser = userUtils.getPrincipal();

        Comment comment = getModel(id);
        entitiesUtils.setReaction(comment, authUser, type);

        Comment savedComment = modelRepository.save(comment);

        return modelMapper.fromModelToResponse(savedComment);
    }


}
