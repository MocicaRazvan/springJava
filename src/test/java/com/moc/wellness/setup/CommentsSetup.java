package com.moc.wellness.setup;

import com.moc.wellness.dto.comment.CommentBody;
import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.enums.Role;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.Post;
import com.moc.wellness.model.user.UserCustom;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CommentsSetup {

    protected final Long postId = 1L;
    protected final Long commentId = 1L;

    protected final long totalElements = 1;
    protected final long totalPages = 1;
    protected final int page = 0;
    protected final int size = 1;

    protected final Long adminId = 1L;
    protected final Long trainerId = 2L;
    protected final Long userId = 3L;

    protected UserCustom userTrainer;
    protected UserCustom userAdmin;
    protected UserCustom userUser;

    protected UserDto trainerDto;
    protected UserDto adminDto;

    protected UserDto userDto;

    protected Post post;

    protected Comment comment;
    protected Comment strippedComment;
    protected CommentResponse commentResponse;

    protected CommentBody commentBody;
    protected PostResponse postResponse;


    protected PageableResponse<List<CommentResponse>> pageableResponse;

    protected PageableBody pageableBody;

    protected PageRequest pageRequest;


    @BeforeEach
    public void setUp() {
        setUserTrainer(UserCustom.builder()
                .id(getTrainerId())
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .password("1234")
                .role(Role.TRAINER)
                .build());
        setUserAdmin(UserCustom.builder()
                .id(getAdminId())
                .email("Admin@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .password("1234")
                .role(Role.ADMIN)
                .build());

        setTrainerDto(UserDto.builder()
                .id(getTrainerId())
                .email("marcel@gmail.com")
                .firstName("Marcel")
                .lastName("Popescu")
                .role(Role.TRAINER)
                .build());
        setAdminDto(UserDto.builder()
                .id(getAdminId())
                .email("Admin@gmail.com")
                .firstName("Razvan")
                .lastName("Mocica")
                .role(Role.ADMIN)
                .build());
        setUserUser(UserCustom.builder()
                .id(getUserId())
                .email("user@gmail.com")
                .firstName("User")
                .lastName("user")
                .password("1234")
                .role(Role.USER)
                .build());
        setUserDto(UserDto.builder()
                .id(getUserId())
                .email("user@gmail.com")
                .firstName("User")
                .lastName("user")
                .role(Role.USER)
                .build());

        setPost(Post.builder()
                .id(getPostId())
                .tags(List.of("MMM"))
                .title("test")
                .body("Test post")
                .userDislikes(new HashSet<>())
                .userLikes(Set.of(getUserAdmin()))
                .user(getUserTrainer())
                .approved(true)
                .build());

        setPostResponse(PostResponse.builder()
                .tags(List.of("MMM"))
                .approved(true)
                .body("Test post")
                .title("test")
                .userDislikes(new HashSet<>())
                .userLikes(Set.of(getAdminDto()))
                .id(getPostId())
                .user(getUserDto())
                .build());

        setComment(Comment.builder()
                .id(getCommentId())
                .title("test")
                .body("test")
                .user(getUserUser())
                .post(getPost())
                .build());

        setStrippedComment(Comment.builder()
                .id(getCommentId())
                .title("test")
                .body("test")
                .build());

        setCommentBody(CommentBody.builder()
                .body("changed")
                .title("changed")
                .build());

        setCommentResponse(CommentResponse.builder()
                .id(getCommentId())
                .title(getComment().getTitle())
                .body(getComment().getBody())
                .user(getUserDto())
                .post(getPostResponse())
                .build());
        setPageableBody(PageableBody.builder()
                .page(getPage())
                .size(getSize())
                .build());

        setPageRequest(PageRequest.of(getPage(), getSize()));
        setPageableResponse(PageableResponse.<List<CommentResponse>>builder()
                .payload(List.of(getCommentResponse()))
                .totalPages(1)
                .totalElements(1)
                .build());

    }

    protected Long getPostId() {
        return postId;
    }

    protected Long getCommentId() {
        return commentId;
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

    protected Long getAdminId() {
        return adminId;
    }

    protected Long getTrainerId() {
        return trainerId;
    }

    protected Long getUserId() {
        return userId;
    }

    protected UserCustom getUserTrainer() {
        return userTrainer;
    }

    protected void setUserTrainer(UserCustom userTrainer) {
        this.userTrainer = userTrainer;
    }

    protected UserCustom getUserAdmin() {
        return userAdmin;
    }

    protected void setUserAdmin(UserCustom userAdmin) {
        this.userAdmin = userAdmin;
    }

    protected UserCustom getUserUser() {
        return userUser;
    }

    protected void setUserUser(UserCustom userUser) {
        this.userUser = userUser;
    }

    protected UserDto getTrainerDto() {
        return trainerDto;
    }

    protected void setTrainerDto(UserDto trainerDto) {
        this.trainerDto = trainerDto;
    }

    protected UserDto getAdminDto() {
        return adminDto;
    }

    protected void setAdminDto(UserDto adminDto) {
        this.adminDto = adminDto;
    }

    protected UserDto getUserDto() {
        return userDto;
    }

    protected void setUserDto(UserDto userDto) {
        this.userDto = userDto;
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

    protected Comment getStrippedComment() {
        return strippedComment;
    }

    protected void setStrippedComment(Comment strippedComment) {
        this.strippedComment = strippedComment;
    }

    protected CommentResponse getCommentResponse() {
        return commentResponse;
    }

    protected void setCommentResponse(CommentResponse commentResponse) {
        this.commentResponse = commentResponse;
    }

    protected CommentBody getCommentBody() {
        return commentBody;
    }

    protected void setCommentBody(CommentBody commentBody) {
        this.commentBody = commentBody;
    }

    protected PostResponse getPostResponse() {
        return postResponse;
    }

    protected void setPostResponse(PostResponse postResponse) {
        this.postResponse = postResponse;
    }

    protected PageableResponse<List<CommentResponse>> getPageableResponse() {
        return pageableResponse;
    }

    protected void setPageableResponse(PageableResponse<List<CommentResponse>> pageableResponse) {
        this.pageableResponse = pageableResponse;
    }

    protected PageableBody getPageableBody() {
        return pageableBody;
    }

    protected void setPageableBody(PageableBody pageableBody) {
        this.pageableBody = pageableBody;
    }

    protected PageRequest getPageRequest() {
        return pageRequest;
    }

    protected void setPageRequest(PageRequest pageRequest) {
        this.pageRequest = pageRequest;
    }
}
