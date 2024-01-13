package com.moc.wellness.setup;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.Post;
import com.moc.wellness.model.user.UserCustom;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PostSetup {
    protected final Long postId = 1L;
    protected final Long commentId = 1L;
    protected final Long userId = 1L;

    protected final long totalElements = 1;
    protected final long totalPages = 1;

    protected final int page = 0;
    protected final int size = 1;
    protected UserCustom userTrainer;

    protected Post post;

    protected Comment comment;

    protected UserDto userDto;
    protected PostResponse postResponse;

    protected PostBody postBody;
    protected PageableResponse<List<PostResponse>> pageableResponse;

    protected PageableBody pageableBody;


    @BeforeEach
    public void setUp() {


        setUserTrainer(UserCustom.builder()
                .id(getUserId())
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .password("1234")
                .role(Role.TRAINER)
                .build());
        Set<UserCustom> likes = new HashSet<>();
        likes.add(getUserTrainer());
        setPost(Post.builder()
                .id(getPostId())
                .tags(List.of("MMM"))
                .title("test")
                .body("Test post")
                .userDislikes(new HashSet<>())
                .userLikes(likes)
                .user(getUserTrainer())
                .approved(true)
                .build());
        setComment(Comment.builder()
                .id(getCommentId())
                .title("test")
                .body("test")
                .user(getUserTrainer())
                .post(getPost())
                .build());


        setUserDto(UserDto.builder()
                .id(1L)
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .role(Role.TRAINER)
                .build());

        Set<UserDto> likesDto = new HashSet<>();
        likesDto.add(getUserDto());

        setPostResponse(PostResponse.builder()
                .tags(List.of("MMM"))
                .approved(true)
                .body("Test post")
                .title("test")
                .userDislikes(new HashSet<>())
                .userLikes(likesDto)
                .id(getPostId())
                .user(getUserDto())
                .build());

        setPostBody(PostBody.builder()
                .tags(List.of("MMM"))
                .body("Test post")
                .title("test")
                .build());
        setPageableResponse(PageableResponse.<List<PostResponse>>builder()
                .totalPages(getTotalPages())
                .totalElements(getTotalElements())
                .payload(Collections.singletonList(getPostResponse()))
                .build());

        setPageableBody(PageableBody.builder()
                .page(getPage())
                .size(getSize())
                .build());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                getUserTrainer(), null, getUserTrainer().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    protected Long getPostId() {
        return postId;
    }

    protected Long getCommentId() {
        return commentId;
    }

    protected Long getUserId() {
        return userId;
    }

    protected long getTotalElements() {
        return totalElements;
    }

    protected long getTotalPages() {
        return totalPages;
    }

    protected int getPage() {
        return page;
    }

    protected int getSize() {
        return size;
    }

    protected UserCustom getUserTrainer() {
        return userTrainer;
    }

    protected void setUserTrainer(UserCustom userTrainer) {
        this.userTrainer = userTrainer;
    }

    protected Post getPost() {
        return post;
    }

    protected void setPost(Post post) {
        this.post = post;
    }

    protected Comment getComment() {
        return comment;
    }

    protected void setComment(Comment comment) {
        this.comment = comment;
    }

    protected UserDto getUserDto() {
        return userDto;
    }

    protected void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    protected PostResponse getPostResponse() {
        return postResponse;
    }

    protected void setPostResponse(PostResponse postResponse) {
        this.postResponse = postResponse;
    }

    protected PostBody getPostBody() {
        return postBody;
    }

    protected void setPostBody(PostBody postBody) {
        this.postBody = postBody;
    }

    protected PageableResponse<List<PostResponse>> getPageableResponse() {
        return pageableResponse;
    }

    protected void setPageableResponse(PageableResponse<List<PostResponse>> pageableResponse) {
        this.pageableResponse = pageableResponse;
    }

    protected PageableBody getPageableBody() {
        return pageableBody;
    }

    protected void setPageableBody(PageableBody pageableBody) {
        this.pageableBody = pageableBody;
    }
}
