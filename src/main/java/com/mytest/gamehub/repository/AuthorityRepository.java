package com.mytest.gamehub.repository;


import com.mytest.gamehub.entity.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, String> {
    Optional<Authority> findByEmail(String email);
}
