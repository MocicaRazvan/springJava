package com.moc.wellness.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.controller.PostController;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.filter.AuthFilter;
import com.moc.wellness.service.PostService;
import com.moc.wellness.setup.PostSetup;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.*;

import static org.mockito.Mockito.*;


@WebMvcTest(controllers = PostController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class PostControllerTests extends PostSetup {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private PostService postService;


    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Delete post success")
    public void deletePostSuccess() throws Exception {
        when(postService.deleteModel(postId)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/delete/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(postResponse)
                        ));

        verify(postService).deleteModel(postId);
    }

    @Test
    @DisplayName("Delete post post not found")
    public void deletePostNotFound() throws Exception {
        when(postService.deleteModel(postId)).thenThrow(new NotFoundEntity("post", postId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/posts/delete/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)),
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Entity post with id 1 was not found!"))
                );
        verify(postService).deleteModel(postId);

    }

    @Test
    @DisplayName("Get post by id success")
    public void getPostByIdSuccess() throws Exception {
        when(postService.getModelById(postId)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{id}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(postResponse)
                        ));

        verify(postService).getModelById(postId);
    }

    @Test
    @DisplayName("Get post by id not found")
    public void getPostById() throws Exception {
        when(postService.getModelById(postId)).thenThrow(new NotFoundEntity("post", postId));

        mockMvc.perform(MockMvcRequestBuilders.get("/posts/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)),
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Entity post with id 1 was not found!"))
                );
        verify(postService).getModelById(postId);

    }

    @Test
    @DisplayName("Update post success")
    public void updateModelSuccess() throws Exception {
        when(postService.updateModel(postId, postBody)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/posts/update/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postBody)))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(postResponse))
                );


        verify(postService).updateModel(postId, postBody);
    }

    @Test
    @DisplayName("Update post not found")
    public void updatePostNotFound() throws Exception {
        when(postService.updateModel(postId, postBody)).thenThrow(new NotFoundEntity("post", postId));

        mockMvc.perform(MockMvcRequestBuilders.put("/posts/update/" + postId)
                        .content(objectMapper.writeValueAsString(postBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)),
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Entity post with id 1 was not found!"))
                );

        verify(postService).updateModel(postId, postBody);
    }

    @Test
    @DisplayName("Update post all 3 arguments not valid")
    public void updatePostAllArgumentsNotValid() throws Exception {

        PostBody emptyBody = new PostBody();
        emptyBody.setBody(null);
        emptyBody.setTitle(null);
        emptyBody.setTags(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/posts/update/" + postId)
                        .content(objectMapper.writeValueAsString(emptyBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(3)));

        verify(postService, times(0)).updateModel(any(), any());
    }

    @Test
    @DisplayName("Update post 1 argument not valid")
    public void updatePostSomeArgumentsNotValid() throws Exception {

        PostBody emptyBody = new PostBody();
        emptyBody.setBody("VALID BODY");
        emptyBody.setTitle("VALID BODY");
        emptyBody.setTags(null);
        mockMvc.perform(MockMvcRequestBuilders.put("/posts/update/" + postId)
                        .content(objectMapper.writeValueAsString(emptyBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(1)));

        verify(postService, times(0)).updateModel(any(), any());
    }


    @Test
    @DisplayName("Like post, likes and dislikes should be empty")
    public void likePostEmpty() throws Exception {
        postResponse.setUserLikes(new HashSet<>());
        when(postService.reactToModel(postId, "like")).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/like/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(postResponse)
                        ));

        verify(postService).reactToModel(postId, "like");
    }

    @Test
    @DisplayName("Like post not found entity")
    public void likePostNotFound() throws Exception {
        when(postService.reactToModel(2L, "like")).thenThrow(new NotFoundEntity("post", 2L));

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/like/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(2)),
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Entity post with id 2 was not found!"))
                );

        verify(postService).reactToModel(2L, "like");
    }

    @Test
    @DisplayName("Dislike post, likes should be empty, dislikes should have user trainer")
    public void dislikePost() throws Exception {
        postResponse.setUserLikes(new HashSet<>());
        postResponse.setUserDislikes(Collections.singleton(userDto));
        when(postService.reactToModel(postId, "dislike")).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/dislike/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(postResponse)
                        ));

        verify(postService).reactToModel(postId, "dislike");
    }


    @Test
    @DisplayName("Dislike post not found entity")
    public void dislikePostNotFound() throws Exception {
        when(postService.reactToModel(2L, "dislike")).thenThrow(new NotFoundEntity("post", 2L));

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/dislike/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(2)),
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Entity post with id 2 was not found!"))
                );

        verify(postService).reactToModel(2L, "dislike");
    }

    @Test
    @DisplayName("Get posts approve success, returns post")
    public void getPostsApproveSuccessReturnsPost() throws Exception {
        when(postService.getModelApproved(pageableBody)).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/approved")
                        .content(objectMapper.writeValueAsString(pageableBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));


        verify(postService).getModelApproved(pageableBody);
    }

    @Test
    @DisplayName("Get posts approve success, returns nothing")
    public void getPostsApproveSuccessReturnsNothing() throws Exception {
        post.setApproved(false);
        PageableResponse<List<PostResponse>> empty = PageableResponse.<List<PostResponse>>builder()
                .totalElements(0)
                .totalPages(0)
                .payload(Collections.emptyList())
                .build();
        when(postService.getModelApproved(pageableBody)).thenReturn(empty);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/approved")
                        .content(objectMapper.writeValueAsString(pageableBody))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(empty)
                        ));


        verify(postService).getModelApproved(pageableBody);
    }

    @Test
    @DisplayName("Get posts approve all arguments 3 body not valid")
    public void getPostsApproveSuccessReturnsArgumentInvalid() throws Exception {
        PageableBody empty = PageableBody.builder()
                .size(0)
                .page(-1)
                .sortingCriteria(Collections.singletonMap("ceva", "altceva"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/approved")
                        .content(objectMapper.writeValueAsString(empty))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(3)));


        verify(postService, times(0)).getModelApproved(any());
    }

    @Test
    @DisplayName("Get posts approve 1 argument body not valid")
    public void getPostsApproveSuccessReturnsArgumentInvalid_1() throws Exception {
        PageableBody empty_1arg = PageableBody.builder()
                .size(0)
                .page(0)
                .sortingCriteria(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/approved")
                        .content(objectMapper.writeValueAsString(empty_1arg))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(1)));


        verify(postService, times(0)).getModelApproved(any());
    }

    @Test
    @DisplayName("Get post by trainer, return post")
    public void getPostByTrainerReturnPost() throws Exception {
        when(postService.getModelTrainer(userId, pageableBody)).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/trainer/" + userId)
                        .content(objectMapper.writeValueAsString(pageableBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));

        verify(postService).getModelTrainer(userId, pageableBody);
    }

    @Test
    @DisplayName("Get post by trainer, return not valid args, missing body")
    public void getPostByTrainerReturnAllArgsErrorNoBody() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/trainer/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest());

        verify(postService, times(0)).getModelTrainer(any(), any());
    }

    @Test
    @DisplayName("Get post by trainer only sorting not valid")
    public void getPostByTrainerReturnAllArgsErrorSorting() throws Exception {
        PageableBody tagsNotValid = PageableBody.builder()
                .page(0)
                .size(1)
                .sortingCriteria(Collections.singletonMap("ceva", "altceva"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/trainer/" + userId)
                        .content(objectMapper.writeValueAsString(tagsNotValid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(1)));

        verify(postService, times(0)).getModelTrainer(any(), any());
    }

    @Test
    @DisplayName("Create post success")
    public void createPostSuccess() throws Exception {
        postResponse.setUserDislikes(new HashSet<>());
        postResponse.setUserLikes(new HashSet<>());
        when(postService.createModel(postBody)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postBody)))
                .andExpectAll(MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(postResponse)
                        ),
                        MockMvcResultMatchers.header().string("Location",
                                "http://localhost:8080/posts/1"));

        verify(postService).createModel(postBody);
    }

    @Test
    @DisplayName("Create post no body error")
    public void createPostNoBodyError() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest());

        verify(postService, times(0)).createModel(any());
    }

    @Test
    @DisplayName("Create post no all 3 arguments of body are not valid")
    public void createPostAllBodyNotValid() throws Exception {
        PostBody empty = PostBody.builder()
                .title("")
                .body("")
                .tags(new ArrayList<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/create")
                        .content(objectMapper.writeValueAsString(empty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(3)));

        verify(postService, times(0)).createModel(any());
    }

    @Test
    @DisplayName("Create post  only tags argument of body are not valid")
    public void createPostTagsBodyNotValid() throws Exception {
        PostBody empty = PostBody.builder()
                .title("valid")
                .body("valid")
                .tags(new ArrayList<>())
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/posts/create")
                        .content(objectMapper.writeValueAsString(empty))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(1)));

        verify(postService, times(0)).createModel(any());
    }

    @Test
    @DisplayName("Approve post success")
    public void approvePostSuccess() throws Exception {
        when(postService.approveModel(postId)).thenReturn(postResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/admin/approve/" + postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(postResponse)
                        ));

        verify(postService).approveModel(postId);

    }

    @Test
    @DisplayName("Approve post not found post")
    public void approvePostNotFound() throws Exception {
        when(postService.approveModel(2L)).thenThrow(new NotFoundEntity("post", 2L));

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/admin/approve/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("post")),
                        MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(2)),
                        MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is("Entity post with id 2 was not found!")));

        verify(postService).approveModel(2L);
    }

    @Test
    @DisplayName("Approve post not already approved")
    public void approvePostAlreadyApproved() throws Exception {
        when(postService.approveModel(postId)).thenThrow(
                new IllegalActionException("post with id " + postId + " already approved")
        );

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/admin/approve/" + postId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.message",
                                CoreMatchers.is("post with id " + postId + " already approved")),
                        MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)));

        verify(postService).approveModel(postId);
    }


    @Test
    @DisplayName("Get all posts admin success")
    public void getAllPostsAdminSuccess() throws Exception {
        when(postService.getAllModels(pageableBody)).thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableBody)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));

        verify(postService).getAllModels(pageableBody);
    }

    @Test
    @DisplayName("Get all posts all body not valid")
    public void getAllPostsAllBodyNotValid() throws Exception {
        PageableBody allNotValid = PageableBody.builder()
                .page(-1)
                .size(-1)
                .sortingCriteria(Collections.singletonMap("ceva", "altceva"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allNotValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(3)));

        verify(postService, times(0)).getAllModels(any());
    }

    @Test
    @DisplayName("Get all posts only page not valid")
    public void getAllPostsPageNotValid() throws Exception {
        PageableBody pageNotValid = PageableBody.builder()
                .page(-1)
                .size(1)
                .sortingCriteria(Collections.singletonMap("title", "asc"))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageNotValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(1)));

        verify(postService, times(0)).getAllModels(any());
    }

}
