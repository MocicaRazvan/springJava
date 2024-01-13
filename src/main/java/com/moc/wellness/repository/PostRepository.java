package com.moc.wellness.repository;

import com.moc.wellness.model.Post;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.generic.ApprovedRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends ApprovedRepository<Post> {

    @Query("""
            select p.user from Post p join UserCustom u
            on p.user.id = u.id where p.id=:id
                """)
    UserCustom findUserFromPostId(Long id);

    @Query("""
            select case when count(p)>0 then true else false end
            from Post p where p.id=:id and p.approved=true
            """)
    boolean existsByIdAndApproved(Long id);


}
