package com.mytest.gamehub.repository;


import com.mytest.gamehub.entity.GameLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameLinkRepository extends CrudRepository<GameLink, Long> {
    List<GameLink> findByEmail(String email);
}
