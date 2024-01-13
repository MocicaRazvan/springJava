package com.moc.wellness.repository.generic;

import com.moc.wellness.model.Templates.TitleBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TitleBodyRepository<M extends TitleBody> extends ManyToOneUserRepository<M> {
}
