package com.moc.wellness.controller.generic;

import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.ManyToOneUser;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;
import com.moc.wellness.service.generics.ManyToOneUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
public abstract class ManyToOneUserController
        <MODEL extends ManyToOneUser, BODY, RESPONSE extends WithUser,
                S extends ManyToOneUserRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>,
                G extends ManyToOneUserService<MODEL, BODY, RESPONSE, S, M>> {

    protected final G modelService;
    protected final String modelName;
    @Value("${wellness.openapi.dev-url}")
    protected String devUrl;

    @Operation(summary = "Delete model",
            description = """
                    Only admin and trainer can access.
                    For the delete to be successful the user either needs to be an admin
                    or the be the owner of the model being deleted.
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto od the deleted object",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RESPONSE> deleteModel(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(modelService.deleteModel(id));
    }

    @Operation(summary = "Get model by id",
            description = "All authenticated users can access. For the retrieve to be successful the model should be approved or the user should be the owner or admin",
            responses = {
                    @ApiResponse(description = "The response will contain an entity response dto",
                            responseCode = "200", useReturnTypeSchema = true)
            }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @GetMapping("/{id}")
    public ResponseEntity<RESPONSE> getModelById(
            @PathVariable Long id) {
        return ResponseEntity.ok(modelService.getModelById(id));
    }

    @Operation(summary = "Update model",
            description = """
                        Only admin and trainer can access.
                        For the update to be successful the user that updates the model
                        needs to be the user that created the model
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto with the updated fields",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @PutMapping("/update/{id}")
    public ResponseEntity<RESPONSE> updateModel(
            @Valid @RequestBody BODY body,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                modelService.updateModel(id, body));
    }

}
