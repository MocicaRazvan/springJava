package com.moc.wellness.service.generics;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.repository.generic.ApprovedRepository;

import java.util.List;

public interface ApprovedService<MODEL extends Approve, BODY extends TitleBody, RESPONSE extends WithUser,
        S extends ApprovedRepository<MODEL>, M extends DtoMapper<MODEL, BODY, RESPONSE>>

        extends TitleBodyService<MODEL, BODY, RESPONSE, S, M> {


    RESPONSE approveModel(Long id);

    PageableResponse<List<RESPONSE>> getModelApproved(PageableBody pageableBody);

    RESPONSE createModel(BODY body);


    PageableResponse<List<RESPONSE>> getModelTrainer(Long trainerId, PageableBody pageableBody);


}
