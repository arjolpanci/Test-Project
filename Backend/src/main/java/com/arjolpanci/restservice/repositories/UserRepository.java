package com.arjolpanci.restservice.repositories;

import com.arjolpanci.restservice.dbmodels.Flight;
import com.arjolpanci.restservice.dbmodels.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByEmail(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userID);

    @Query(value = "SELECT * FROM user",
            countQuery = "SELECT count(*) FROM user",
            nativeQuery = true)
    Page<User> getUsers(Pageable pageable);
}
