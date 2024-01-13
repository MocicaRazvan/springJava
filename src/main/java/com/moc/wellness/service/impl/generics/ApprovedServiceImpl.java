package com.moc.wellness.service.impl.generics;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.service.generics.ApprovedService;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.UserUtils;
import com.moc.wellness.repository.generic.ApprovedRepository;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


public abstract class ApprovedServiceImpl<MODEL extends Approve, BODY extends TitleBody, RESPONSE extends WithUser,
        S extends ApprovedRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>>
        extends TitleBodyServiceImpl<MODEL, BODY, RESPONSE, S, M>
        implements ApprovedService<MODEL, BODY, RESPONSE, S, M> {


    public ApprovedServiceImpl(S modelRepository, M modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, String modelName, EntitiesUtils entitiesUtils) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, modelName, entitiesUtils);

    }

    @Override
    public PageableResponse<List<RESPONSE>> getModelApproved(PageableBody pageableBody) {
        Page<MODEL> page = modelRepository.findAllByApproved(true, pageableUtils.createPageRequest(pageableBody));

        return PageableResponse.<List<RESPONSE>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public RESPONSE approveModel(Long id) {
        MODEL entity = getModel(id);

        if (entity.isApproved()) {
            throw new IllegalActionException(modelName + " with id " + id + " is already approved!");
        }

        entity.setApproved(true);
        return modelMapper.fromModelToResponse(modelRepository.save(entity));
    }

    @Override
    @Transactional
    public RESPONSE createModel(BODY body) {
        UserCustom authUser = userUtils.getPrincipal();
        MODEL model = modelMapper.fromBodyToModel(body);
        model.setUser(authUser);
        MODEL savedModel = modelRepository.save(model);
        return modelMapper.fromModelToResponse(savedModel);
    }

    @Override
    public RESPONSE getModelById(Long id) {
        UserCustom authUser = userUtils.getPrincipal();

        MODEL model = getModel(id);

        privateRoute(!model.isApproved(), authUser, model.getUser().getId());

        return modelMapper.fromModelToResponse(model);
    }

    @Override
    public PageableResponse<List<RESPONSE>> getModelTrainer(Long trainerId,
                                                            PageableBody pageableBody) {

        userUtils.existsTrainerOrAdmin(trainerId);
        UserCustom authUser = userUtils.getPrincipal();

        privateRoute(true, authUser, trainerId);

        Page<MODEL> page = modelRepository.findAllByUserId(trainerId, pageableUtils.createPageRequest(pageableBody));

        return PageableResponse.<List<RESPONSE>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(
                        page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList())
                )
                .build();
    }


}
