package com.arjolpanci.restservice.repositories;

import com.arjolpanci.restservice.dbmodels.ResetTokens;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends CrudRepository<ResetTokens, Long> {
    Optional<ResetTokens> findByUserId(Long id);
}
