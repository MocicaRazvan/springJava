package com.moc.wellness.service.generics;

import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.TitleBody;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;
import com.moc.wellness.repository.generic.TitleBodyRepository;

public interface TitleBodyService<MODEL extends TitleBody, BODY, RESPONSE extends WithUser,
        S extends TitleBodyRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>>
        extends ManyToOneUserService<MODEL, BODY, RESPONSE, S, M> {

    RESPONSE reactToModel(Long id, String type);


}
