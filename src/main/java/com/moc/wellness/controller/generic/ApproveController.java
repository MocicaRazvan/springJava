package com.moc.wellness.controller.generic;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.service.generics.ApprovedService;
import com.moc.wellness.repository.generic.ApprovedRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

public abstract class ApproveController<MODEL extends Approve, BODY extends TitleBody, RESPONSE extends WithUser,
        S extends ApprovedRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>,
        G extends ApprovedService<MODEL, BODY, RESPONSE, S, M>>

        extends TitleBodyController<MODEL, BODY, RESPONSE, S, M, G> {
    public ApproveController(G modelService, String modelName) {
        super(modelService, modelName);
    }

    @Operation(summary = "Get all approved models",
            description = "All authenticated users can access",
            responses = {
                    @ApiResponse(description = "The response will contain a pageable response with the payload as a list of entity response dto",
                            responseCode = "200", useReturnTypeSchema = true)
            })
    @PatchMapping("/approved")
    public ResponseEntity<PageableResponse<List<RESPONSE>>> getModelsApproved(
            @Valid @RequestBody PageableBody pageableBody
    ) {
        return ResponseEntity.ok(modelService.getModelApproved(pageableBody));
    }

    @Operation(summary = "Get all the models of a trainer",
            description = """
                    Only admin and trainer can access.
                    For the "get" to be successful the user either needs to be an admin
                    or the be the owner of the models.
                    """, responses = {
            @ApiResponse(description = "The response will contain a pageable response with the payload as a list of entity response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "trainerId", description = "The id of the trainer that made the entities",
            required = true, example = "1")}

    )
    @PatchMapping("/trainer/{trainerId}")
    public ResponseEntity<PageableResponse<List<RESPONSE>>> getModelsTrainer(
            @Valid @RequestBody PageableBody pageableBody,
            @PathVariable Long trainerId) {
        return ResponseEntity.ok(
                modelService.getModelTrainer(trainerId, pageableBody)
        );

    }

    @Operation(summary = "Create a model",
            description = """
                    Only admin and trainer can access.
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto",
                    responseCode = "201", useReturnTypeSchema = true)
    })
    @PostMapping("/create")
    public ResponseEntity<RESPONSE> createModel(
            @Valid @RequestBody BODY body) {
        RESPONSE response = modelService.createModel(body);

        return ResponseEntity.created(URI.create(devUrl + "/" + modelName + "s/" + response.getId()))
                .body(response);
    }

    @Operation(summary = "Approve a model",
            description = """
                    Only admin can access.
                    For the approval to be successfully the model needs to be unapproved.
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto with the update approved status",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @PatchMapping("/admin/approve/{id}")
    private ResponseEntity<RESPONSE> approveModel(@PathVariable Long id) {
        return ResponseEntity.ok(modelService.approveModel(id));
    }

    @Operation(summary = "Get all the models",
            description = "Only admin can access", responses = {
            @ApiResponse(description = "The response will contain a pageable response with the payload as a list of entity response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    })
    @PatchMapping("/admin")
    public ResponseEntity<PageableResponse<List<RESPONSE>>> getAllModels(
            @Valid @RequestBody PageableBody pageableBody
    ) {
        return ResponseEntity.ok(modelService.getAllModels(pageableBody));
    }


}
