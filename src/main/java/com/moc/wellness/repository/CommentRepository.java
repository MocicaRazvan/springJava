package com.moc.wellness.repository;

import com.moc.wellness.model.Comment;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;
import com.moc.wellness.repository.generic.TitleBodyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends TitleBodyRepository<Comment> {

    Page<Comment> findAllByPostId(Long postId, PageRequest request);


}
