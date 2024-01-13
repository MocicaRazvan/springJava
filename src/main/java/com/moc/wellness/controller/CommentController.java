package com.moc.wellness.controller;


import com.moc.wellness.controller.generic.ManyToOneUserController;
import com.moc.wellness.controller.generic.TitleBodyController;
import com.moc.wellness.dto.auth.AuthResponse;
import com.moc.wellness.dto.comment.CommentBody;
import com.moc.wellness.dto.comment.CommentResponse;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.mapper.CommentMapper;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.CommentRepository;
import com.moc.wellness.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comments Controller")
public class CommentController
        extends TitleBodyController<Comment, CommentBody, CommentResponse,
        CommentRepository, CommentMapper, CommentService> {
    public CommentController(CommentService modelService) {
        super(modelService, "comment");
    }


    @Operation(summary = "Create comment for a post",
            description = """
                    All authenticated users can access.
                    For the comment to be created the post should exist.
                    """, responses = {
            @ApiResponse(description = "The response will contain the comment response dto",
                    responseCode = "201", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "postId", description = "The id of the post on which comments are on",
            required = true, example = "1")})
    @PostMapping("/create/{postId}")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentBody commentBody
    ) {

        CommentResponse resp = modelService.createModel(postId, commentBody);
        return ResponseEntity.created(URI.create(devUrl + "/comments/" + resp.getId())).body(resp);
    }

    @Operation(summary = "Get all the comments for a post",
            description = "All authenticated users can access. For the operation to be successful, the post should exist.",
            responses = {
                    @ApiResponse(description = "The response will contain a pageable response with the payload as a list of comment response dto",
                            responseCode = "200", useReturnTypeSchema = true)
            }, parameters = {@Parameter(name = "postId", description = "The id of the post on which comments are on",
            required = true, example = "1")})
    @PatchMapping("/{postId}")
    public ResponseEntity<PageableResponse<List<CommentResponse>>> getCommentByPost(
            @PathVariable Long postId,
            @Valid @RequestBody PageableBody pageableBody

    ) {
        return ResponseEntity.ok(modelService.getCommentByPost(postId, pageableBody));
    }

    @Operation(summary = "Get all the comments for a user",
            description = """
                    All authenticated users can access.
                    For the operation to be successfully the users should exist.
                    """, responses = {
            @ApiResponse(description = "The response will contain a pageable response with the payload as a list of comment response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "userId", description = "The id of the user that made the comments",
            required = true, example = "1")})
    @PatchMapping("/user/{userId}")
    public ResponseEntity<PageableResponse<List<CommentResponse>>> getCommentByUser(
            @PathVariable Long userId,
            @Valid @RequestBody PageableBody pageableBody

    ) {
        return ResponseEntity.ok(modelService.getModelByUser(userId, pageableBody));
    }

    @Override
    @Operation(summary = "Update comment",
            description = """
                        For the update to be successful the user that updates the model
                        needs to be the user that created the model
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto with the updated fields",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @PutMapping("/update/{id}")
    public ResponseEntity<CommentResponse> updateModel(
            @Valid @RequestBody CommentBody body,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(modelService.updateModel(id, body));
    }

}
