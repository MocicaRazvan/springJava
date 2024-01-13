package com.moc.wellness.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.controller.CommentController;
import com.moc.wellness.dto.comment.CommentBody;
import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.filter.AuthFilter;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.Post;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.service.CommentService;
import com.moc.wellness.setup.CommentsSetup;
import lombok.Data;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = CommentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class CommentControllerTests extends CommentsSetup {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;


    @Test
    @DisplayName("Create comment success")
    public void createCommentSuccess() throws Exception {
        when(commentService.createModel(postId, commentBody)).thenReturn(commentResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/comments/create/" + postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentBody)))
                .andExpectAll(MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("location",
                                "http://localhost:8080/comments/" + comment.getId()),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(commentResponse)
                        ));

        verify(commentService).createModel(postId, commentBody);
    }

    @Test
    @DisplayName("Create comment post not found")
    public void createCommentPostNotFound() throws Exception {
        when(commentService.createModel(postId, commentBody)).thenThrow(new NotFoundEntity("post", postId));

        mockMvc.perform(MockMvcRequestBuilders.post("/comments/create/" + postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentBody)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.id",
                                CoreMatchers.is(postId.intValue())),
                        MockMvcResultMatchers.jsonPath("$.name",
                                CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.error",
                                CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())));

        verify(commentService).createModel(postId, commentBody);
    }

    @Test
    @DisplayName("Create comment body not valid")
    public void createCommentBodyNotValid() throws Exception {
        CommentBody notValid = CommentBody.builder()
                .title("")
                .body("")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/comments/create/" + postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(2)),
                        MockMvcResultMatchers.jsonPath("$.error",
                                CoreMatchers.is(HttpStatus.BAD_REQUEST.getReasonPhrase())));

        verify(commentService, times(0)).createModel(any(), any());
    }

    @Test
    @DisplayName("Get comments by post success")
    public void getCommentsByPostSuccess() throws Exception {
        when(commentService.getCommentByPost(postId, pageableBody)).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/" + postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(pageableBody)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));

        verify(commentService).getCommentByPost(postId, pageableBody);
    }

    @Test
    @DisplayName("Get comments by user success")
    public void getCommentsByUserSuccess() throws Exception {
        when(commentService.getModelByUser(userId, pageableBody)).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/comments/user/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableBody)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));

        verify(commentService).getModelByUser(userId, pageableBody);
    }

    @Test
    @DisplayName("Update comment success")
    public void updateCommentSuccess() throws Exception {
        when(commentService.updateModel(commentId, commentBody)).thenReturn(commentResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/comments/update/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentBody)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(commentResponse)
                        ));

        verify(commentService).updateModel(commentId, commentBody);
    }
}
