package com.moc.wellness.service.generics;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.model.Templates.ManyToOneUser;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.generic.ApprovedRepository;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;

import java.util.List;

public interface ManyToOneUserService<MODEL extends ManyToOneUser, BODY, RESPONSE extends WithUser,
        S extends ManyToOneUserRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>> {

    RESPONSE deleteModel(Long id);

    RESPONSE getModelById(Long id);

    PageableResponse<List<RESPONSE>> getAllModels(PageableBody pageableBody);

    RESPONSE updateModel(Long id, BODY body);
}
