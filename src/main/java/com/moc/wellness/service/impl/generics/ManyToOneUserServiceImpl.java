package com.moc.wellness.service.impl.generics;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Comment;
import com.moc.wellness.model.Templates.ManyToOneUser;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;
import com.moc.wellness.service.generics.ManyToOneUserService;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor

public abstract class ManyToOneUserServiceImpl<MODEL extends ManyToOneUser, BODY, RESPONSE extends WithUser,
        S extends ManyToOneUserRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>> implements ManyToOneUserService<MODEL, BODY, RESPONSE, S, M> {
    protected final S modelRepository;
    protected final M modelMapper;
    protected final PageableUtilsCustom pageableUtils;
    protected final UserUtils userUtils;
    protected final String modelName;


    @Override
    @Transactional
    public RESPONSE deleteModel(Long id) {
        UserCustom authUser = userUtils.getPrincipal();
        MODEL model = getModel(id);
        privateRoute(true, authUser, model.getUser().getId());
        modelRepository.delete(model);
        return modelMapper.fromModelToResponse(model);
    }

    @Override
    public RESPONSE getModelById(Long id) {
        UserCustom authUser = userUtils.getPrincipal();
        MODEL model = getModel(id);
        privateRoute(true, authUser, model.getUser().getId());

        return modelMapper.fromModelToResponse(model);
    }

    @Override
    public PageableResponse<List<RESPONSE>> getAllModels(PageableBody pageableBody) {
        Page<MODEL> page = modelRepository.findAll(pageableUtils.createPageRequest(pageableBody));

        return PageableResponse.<List<RESPONSE>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public RESPONSE updateModel(Long id, BODY body) {
        UserCustom authUser = userUtils.getPrincipal();
        MODEL model = getModel(id);

        if (isNotAuthor(model, authUser)) {
            throw new PrivateRouteException();
        }

        modelMapper.updateModelFromBody(body, model);
        MODEL savedModel = modelRepository.save(model);
        return modelMapper.fromModelToResponse(savedModel);
    }

    protected MODEL getModel(Long id) {
        return modelRepository.findById(id).orElseThrow(() -> new NotFoundEntity(modelName, id));
    }

    protected void privateRoute(boolean guard, UserCustom authUser, Long ownerId) {
        if (guard && !userUtils.hasPermissionToModifyEntity(authUser, ownerId)) {
            throw new PrivateRouteException();
        }
    }

    protected boolean isNotAuthor(MODEL model, UserCustom authUser) {
        return !Objects.equals(model.getUser().getId(), authUser.getId());

    }
}
