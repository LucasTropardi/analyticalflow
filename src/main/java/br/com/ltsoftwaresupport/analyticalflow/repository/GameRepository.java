package br.com.ltsoftwaresupport.analyticalflow.repository;

import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    boolean existsByName(String name);
    List<Game> findByPlatform(Platform platform);
    Optional<Game> findById(Long gameId);
}
