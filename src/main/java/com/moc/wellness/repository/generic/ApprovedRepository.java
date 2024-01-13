package com.moc.wellness.repository.generic;


import com.moc.wellness.model.Post;
import com.moc.wellness.model.Templates.Approve;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean

public interface ApprovedRepository<T extends Approve> extends TitleBodyRepository<T> {
    Page<T> findAllByApproved(boolean approved, PageRequest request);

}
