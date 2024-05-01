package br.com.ltsoftwaresupport.analyticalflow.repository;

import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameReviewRepository extends JpaRepository<GameReview, Long> {
    List<GameReview> findByGame(Game game);
    List<GameReview> findAllByUserUsername(String username);
    List<GameReview> findByGameId(Long gameId);

}
