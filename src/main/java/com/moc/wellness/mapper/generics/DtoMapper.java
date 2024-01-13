package com.moc.wellness.mapper.generics;

import com.moc.wellness.dto.common.TitleBody;
import com.moc.wellness.dto.common.WithUser;
import com.moc.wellness.dto.post.PostBody;
import com.moc.wellness.model.Post;
import com.moc.wellness.model.Templates.Approve;
import com.moc.wellness.model.Templates.IdGenerated;
import com.moc.wellness.model.Templates.ManyToOneUser;

public abstract class DtoMapper<MODEL extends ManyToOneUser, BODY, RESPONSE extends WithUser> {

    public abstract RESPONSE fromModelToResponse(MODEL model);

    public abstract MODEL fromBodyToModel(BODY body);

    public abstract void updateModelFromBody(BODY body, MODEL model);

}
