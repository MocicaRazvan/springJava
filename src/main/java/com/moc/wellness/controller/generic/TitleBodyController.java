package com.moc.wellness.controller.generic;

import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.TitleBody;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;
import com.moc.wellness.repository.generic.TitleBodyRepository;
import com.moc.wellness.service.generics.ManyToOneUserService;
import com.moc.wellness.service.generics.TitleBodyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

public abstract class TitleBodyController
        <MODEL extends TitleBody, BODY, RESPONSE extends WithUser,
                S extends TitleBodyRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>,
                G extends TitleBodyService<MODEL, BODY, RESPONSE, S, M>>
        extends ManyToOneUserController<MODEL, BODY, RESPONSE, S, M, G> {

    public TitleBodyController(G modelService, String modelName) {
        super(modelService, modelName);
    }


    @Operation(summary = "Like a model",
            description = """
                     All authenticated users can access.
                     If the user didn't dislike the model previously, he will just be\s
                     added to the likes set. If he disliked the model previously he will also\s
                     be removed from the dislikes set. But, if he liked the model previously
                     he will just be removed from the liked set.
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto with the updated likes and dislikes",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @PatchMapping("/like/{id}")
    public ResponseEntity<RESPONSE> likeModel(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(modelService.reactToModel(
                id,
                "like"
        ));
    }

    @Operation(summary = "Dislike a model",
            description = """
                    All authenticated users can access.
                    If the user didn't like the model previously, he will just be\s
                    added to the dislikes set. If he liked the model previously he will also\s
                    be removed from the likes set. But, if he disliked the model previously
                    he will just be removed from the disliked set.
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto with the updated likes and dislikes",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @PatchMapping("/dislike/{id}")
    public ResponseEntity<RESPONSE> dislikeModel(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(modelService.reactToModel(
                id,
                "dislike"
        ));
    }
}
