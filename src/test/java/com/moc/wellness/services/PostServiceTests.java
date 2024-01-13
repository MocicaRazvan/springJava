package com.moc.wellness.services;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.post.PostResponse;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.PostMapper;
import com.moc.wellness.model.Post;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.PostRepository;
import com.moc.wellness.service.impl.PostImplService;
import com.moc.wellness.setup.PostSetup;
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
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests extends PostSetup {

    @Mock
    private PostRepository postRepository;
    @Mock
    private PostMapper postMapper;
    @Mock
    private PageableUtilsCustom pageableUtils;
    @Mock
    private UserUtils userUtils;
    @Mock
    private EntitiesUtils entitiesUtils;

    @InjectMocks
    private PostImplService postService;


    @Test
    @DisplayName("Get post by id success")
    public void getPostByIdSuccess() {
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postMapper.fromModelToResponse(any(Post.class))).thenReturn(postResponse);
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        PostResponse serviceResponse = postService.getModelById(postId);

        Assertions.assertNotNull(serviceResponse);
        Assertions.assertEquals(serviceResponse, postResponse);
        verify(postRepository).findById(postId);
        verify(postMapper).fromModelToResponse(any(Post.class));
        verify(userUtils, times(0)).hasPermissionToModifyEntity(any(), any());
        verify(userUtils).getPrincipal();
    }

    @Test
    @DisplayName("Get post by id NotFoundEntity")
    public void getPostByIdNotFoundEntity() {
        when(postRepository.findById(postId)).thenThrow(NotFoundEntity.class);
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        Assertions.assertThrows(NotFoundEntity.class,
                () -> postService.getModelById(postId));
        verify(postRepository).findById(postId);
        verify(postMapper, times(0)).fromModelToResponse(any());
        verify(userUtils, times(0)).hasPermissionToModifyEntity(any(), any());
        verify(userUtils).getPrincipal();
    }

    @Test
    @DisplayName("Get post by id PrivateRoute")
    public void getPostByIdPrivateRoute() {
        post.setApproved(false);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(userUtils.hasPermissionToModifyEntity(any(UserCustom.class), any(Long.class)))
                .thenReturn(false);
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        PrivateRouteException exception = Assertions.assertThrows(
                PrivateRouteException.class,
                () -> postService.getModelById(postId)
        );

        verify(postRepository).findById(any());
        verify(userUtils).hasPermissionToModifyEntity(any(), any());
        verify(postMapper, times(0)).fromModelToResponse(any());
        verify(userUtils).getPrincipal();

    }

    @Test
    @DisplayName("Delete post success")
    public void deletePostSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        doNothing().when(postRepository).delete(any(Post.class));
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(userUtils.hasPermissionToModifyEntity(any(), any())).thenReturn(true);
        when(postMapper.fromModelToResponse(any(Post.class))).thenReturn(postResponse);
        PostResponse serviceResponse = postService.deleteModel(postId);

        Assertions.assertEquals(serviceResponse, postResponse);
        verify(userUtils).getPrincipal();
        verify(postRepository).delete(any());
        verify(postRepository).findById(any());
        verify(userUtils).hasPermissionToModifyEntity(any(), any());
    }

    @Test
    @DisplayName("Get all posts success")
    public void getAllPostsSuccess() {
        Page<Post> mockPage = new PageImpl<>(Collections.singletonList(post), PageRequest.of(page, size), 1);
        when(pageableUtils.createPageRequest(any(PageableBody.class))).thenReturn(PageRequest.of(page, size));
        when(postRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);
        when(postMapper.fromModelToResponse(any())).thenReturn(postResponse);

        PageableResponse<List<PostResponse>> resp = postService.getAllModels(pageableBody);

        Assertions.assertEquals(resp.getPayload().get(0), postResponse);
        Assertions.assertEquals(resp.getPayload().size(), 1);
        Assertions.assertEquals(resp.getTotalPages(), 1);
        Assertions.assertEquals(resp.getTotalElements(), 1);
        verify(pageableUtils).createPageRequest(any());
        verify(postRepository).findAll(any(PageRequest.class));
        verify(postMapper).fromModelToResponse(any());
    }


    @Test
    @DisplayName("Update post success")
    public void updatePostSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        doNothing().when(postMapper).updateModelFromBody(postBody, post);
        post.setTitle("CHANGED");
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postMapper.fromModelToResponse(post)).then(invocation -> {
            Post arg = invocation.getArgument(0);
            postResponse.setTitle(arg.getTitle());
            return postResponse;
        });


        PostResponse serviceResponse = postService.updateModel(postId, postBody);

        Assertions.assertEquals(serviceResponse, postResponse);
        verify(postMapper).updateModelFromBody(any(), any());
        verify(postRepository).findById(postId);
        verify(userUtils, times(0)).hasPermissionToModifyEntity(any(), any());
        verify(postRepository).save(any());
        verify(postMapper).fromModelToResponse(any());
        verify(userUtils).getPrincipal();

    }

    @Test
    @DisplayName("Update post not author")
    public void updatePostNoAuthor() {
        UserCustom user = UserCustom.builder()
                .id(2L)
                .build();
        when(userUtils.getPrincipal()).thenReturn(user);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));

        PrivateRouteException ex = Assertions.assertThrows(PrivateRouteException.class,
                () -> postService.updateModel(postId, postBody));

        verify(userUtils).getPrincipal();
        verify(postRepository).findById(postId);
        verify(postMapper, times(0)).updateModelFromBody(any(), any());
        verify(postRepository, times(0)).save(any());
        verify(postMapper, times(0)).fromModelToResponse(any());
    }

    @Test
    @DisplayName("Like post, the likes and dislikes should be empty")
    public void removeLike() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        doNothing().when(entitiesUtils).setReaction(post, userTrainer, "like");
        post.setUserLikes(new HashSet<>());
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.fromModelToResponse(post)).then(invocation -> {
            Post arg = invocation.getArgument(0);
            Assertions.assertEquals(arg.getUserLikes().size(), 0);
            postResponse.setUserLikes(new HashSet<>());
            Assertions.assertEquals(arg.getUserDislikes().size(), 0);
            postResponse.setUserDislikes(new HashSet<>());
            return postResponse;
        });

        PostResponse serviceResponse = postService.reactToModel(postId, "like");

        Assertions.assertEquals(serviceResponse, postResponse);
        verify(postRepository).findById(postId);
        verify(userUtils).getPrincipal();
        verify(entitiesUtils).setReaction(any(), any(), any());
        verify(postRepository).save(post);
        verify(postMapper).fromModelToResponse(post);

    }

    @Test
    @DisplayName("Dislike post, the likes should be empty the dislikes should have userDto")
    public void dislikePost() {

        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        doNothing().when(entitiesUtils).setReaction(post, userTrainer, "dislike");
        post.setUserLikes(new HashSet<>());
        post.setUserDislikes(Collections.singleton(userTrainer));
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.fromModelToResponse(post)).then(invocation -> {
            Post arg = invocation.getArgument(0);
            Assertions.assertEquals(arg.getUserLikes().size(), 0);
            postResponse.setUserLikes(new HashSet<>());
            Assertions.assertEquals(arg.getUserDislikes().size(), 1);
            postResponse.setUserDislikes(Collections.singleton(userDto));
            return postResponse;
        });

        PostResponse serviceResponse = postService.reactToModel(postId, "dislike");

        Assertions.assertEquals(serviceResponse, postResponse);
        verify(postRepository).findById(postId);
        verify(userUtils).getPrincipal();
        verify(entitiesUtils).setReaction(any(), any(), any());
        verify(postRepository).save(post);
        verify(postMapper).fromModelToResponse(post);

    }

    @Test
    @DisplayName("Get all posts approved, return the post")
    public void getPostApprovedReturns() {
        Page<Post> mockPage = new PageImpl<>(Collections.singletonList(post), PageRequest.of(page, size), 1);

        when(pageableUtils.createPageRequest(any(PageableBody.class))).thenReturn(PageRequest.of(page, size));
        when(postRepository.findAllByApproved(true, PageRequest.of(page, size))).thenReturn(mockPage);
        when(postMapper.fromModelToResponse(post)).thenReturn(postResponse);

        PageableResponse<List<PostResponse>> serviceResp = postService.getModelApproved(pageableBody);

        Assertions.assertEquals(serviceResp.getPayload().size(), 1);
        Assertions.assertEquals(serviceResp.getPayload().get(0), postResponse);
        Assertions.assertEquals(serviceResp.getTotalPages(), 1);
        Assertions.assertEquals(serviceResp.getTotalElements(), 1);

        verify(pageableUtils).createPageRequest(any(PageableBody.class));
        verify(postRepository).findAllByApproved(true, PageRequest.of(page, size));
        verify(postMapper).fromModelToResponse(post);
    }

    @Test
    @DisplayName("Get all posts approved, returns an empty list")
    public void getPostApprovedEmpty() {
        post.setApproved(false);
        Page<Post> mockPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), 0);

        when(pageableUtils.createPageRequest(any(PageableBody.class))).thenReturn(PageRequest.of(page, size));
        when(postRepository.findAllByApproved(true, PageRequest.of(page, size))).thenReturn(mockPage);

        PageableResponse<List<PostResponse>> serviceResp = postService.getModelApproved(pageableBody);

        Assertions.assertEquals(serviceResp.getPayload().size(), 0);
        Assertions.assertEquals(serviceResp.getTotalPages(), 0);
        Assertions.assertEquals(serviceResp.getTotalElements(), 0);

        verify(pageableUtils).createPageRequest(any(PageableBody.class));
        verify(postRepository).findAllByApproved(true, PageRequest.of(page, size));
        verify(postMapper, times(0)).fromModelToResponse(any());
    }

    @Test
    @DisplayName("Approve post")
    public void approvePost() {
        post.setApproved(false);
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.fromModelToResponse(post)).then(invocation -> {
            Post arg = invocation.getArgument(0);
            Assertions.assertTrue(arg.isApproved());
            postResponse.setApproved(true);
            return postResponse;
        });

        PostResponse serviceResponse = postService.approveModel(postId);

        Assertions.assertEquals(serviceResponse, postResponse);
        verify(postRepository).findById(postId);
        verify(postRepository).save(post);
        verify(postMapper).fromModelToResponse(post);

    }

    @Test
    @DisplayName("Approve post illegal action")
    public void approvePostIllegalAction() {
        when(postRepository.findById(postId)).thenReturn(Optional.ofNullable(post));

        IllegalActionException ex =
                Assertions.assertThrows(IllegalActionException.class,
                        () -> postService.approveModel(postId));


        Assertions.assertEquals(ex.getMessage(), "post with id 1 is already approved!");
        verify(postRepository).findById(postId);
        verify(postRepository, times(0)).save(post);
        verify(postMapper, times(0)).fromModelToResponse(post);

    }

    @Test
    @DisplayName("Create post success")
    public void createPostSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(postMapper.fromBodyToModel(postBody)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);
        when(postMapper.fromModelToResponse(post)).thenReturn(postResponse);

        PostResponse serviceResponse = postService.createModel(postBody);

        Assertions.assertEquals(postResponse, serviceResponse);

        verify(userUtils).getPrincipal();
        verify(postMapper).fromBodyToModel(postBody);
        verify(postRepository).save(post);
        verify(postMapper).fromModelToResponse(post);

    }

    @Test
    @DisplayName("Create post fail")
    public void createPostFail() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(postMapper.fromBodyToModel(postBody)).thenReturn(post);
        when(postRepository.save(post)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class,
                () -> postService.createModel(postBody));

        verify(userUtils).getPrincipal();
        verify(postMapper).fromBodyToModel(postBody);
        verify(postRepository).save(post);
        verify(postMapper, times(0)).fromModelToResponse(any());
    }

    @Test
    @DisplayName("Get posts by trainer success")
    public void getPostsByTrainerSuccess() {
        Page<Post> mockPage = new PageImpl<>(Collections.singletonList(post), PageRequest.of(page, size), 1);
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        doNothing().when(userUtils).existsTrainerOrAdmin(userTrainer.getId());
        when(userUtils.hasPermissionToModifyEntity(any(), any())).thenReturn(true);
        when(pageableUtils.createPageRequest(any())).thenReturn(PageRequest.of(page, size));
        when(postRepository.findAllByUserId(any(), any())).thenReturn(mockPage);
        when(postMapper.fromModelToResponse(post)).thenReturn(postResponse);

        PageableResponse<List<PostResponse>> servicePage = postService.getModelTrainer(userId, pageableBody);

        Assertions.assertNotNull(servicePage.getPayload());
        Assertions.assertEquals(servicePage.getPayload().size(), 1);
        Assertions.assertEquals(servicePage.getPayload().get(0), postResponse);
        Assertions.assertEquals(servicePage.getTotalPages(), 1);
        Assertions.assertEquals(servicePage.getTotalElements(), 1);

        verify(userUtils).getPrincipal();
        verify(userUtils).existsTrainerOrAdmin(userTrainer.getId());
        verify(userUtils).hasPermissionToModifyEntity(any(), any());
        verify(pageableUtils).createPageRequest(any());
        verify(postRepository).findAllByUserId(any(), any());
        verify(postMapper).fromModelToResponse(post);
    }

    @Test
    @DisplayName("Get posts by trainer PrivateRouteException")
    public void getPostsByTrainerPrivateRouteException() {
        doThrow(PrivateRouteException.class).when(userUtils).existsTrainerOrAdmin(2L);


        Assertions.assertThrows(PrivateRouteException.class,
                () -> postService.getModelTrainer(2L, pageableBody));

        verify(userUtils).existsTrainerOrAdmin(2L);
        verify(userUtils, times(0)).getPrincipal();
        verify(userUtils, times(0)).hasPermissionToModifyEntity(any(), any());
        verify(pageableUtils, times(0)).createPageRequest(any());
        verify(postRepository, times(0)).findAllByUserId(any(), any());
        verify(postMapper, times(0)).fromModelToResponse(any());
    }

}
