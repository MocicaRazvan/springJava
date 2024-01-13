package com.moc.wellness.repository;

import com.moc.wellness.model.user.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Query("""
                    select t from JwtToken t 
                    join UserCustom u on t.user.id=u.id 
                    where u.id=:userId and t.revoked=false
            """)
    List<JwtToken> findAllValidTokensByUser(Long userId);


    Optional<JwtToken> findByToken(String token);

}
