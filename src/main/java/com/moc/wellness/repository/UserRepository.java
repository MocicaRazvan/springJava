package com.moc.wellness.repository;

import com.moc.wellness.enums.Role;
import com.moc.wellness.model.user.UserCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserCustom, Long> {

    Optional<UserCustom> findByEmail(String email);


    Page<UserCustom> findAllByRoleIn(Set<Role> roles, PageRequest request);


    @Query("""
                    select case when count(*)>0 then true else false end
                    from UserCustom where id=:userId and role in :roles
            """)
    boolean existsRouteForEntity(Long userId, List<Role> roles);


    boolean existsByEmail(String mail);
}
