package com.moc.wellness.repository.generic;

import com.moc.wellness.model.Templates.ManyToOneUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ManyToOneUserRepository<M extends ManyToOneUser> extends JpaRepository<M,Long> {
    Page<M> findAllByUserId(Long userId, PageRequest request);
}
