package com.moc.wellness.service.impl.generics;

import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.TitleBody;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.generic.TitleBodyRepository;
import com.moc.wellness.service.generics.TitleBodyService;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class TitleBodyServiceImpl<MODEL extends TitleBody, BODY, RESPONSE extends WithUser,
        S extends TitleBodyRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>>
        extends ManyToOneUserServiceImpl<MODEL, BODY, RESPONSE, S, M>
        implements TitleBodyService<MODEL, BODY, RESPONSE, S, M> {

    protected final EntitiesUtils entitiesUtils;

    public TitleBodyServiceImpl(S modelRepository, M modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, String modelName, EntitiesUtils entitiesUtils) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, modelName);
        this.entitiesUtils = entitiesUtils;
    }

    @Override
    @Transactional
    public RESPONSE reactToModel(Long id, String type) {
        UserCustom authUser = userUtils.getPrincipal();
        MODEL model = getModel(id);

        entitiesUtils.setReaction(model, authUser, type);

        MODEL savedModel = modelRepository.save(model);

        return modelMapper.fromModelToResponse(savedModel);
    }
}
