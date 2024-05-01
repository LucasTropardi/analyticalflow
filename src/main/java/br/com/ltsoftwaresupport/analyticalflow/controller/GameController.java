package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.repository.GameRepository;

@Service
public class GameController  extends GenericController<Game, Long, GameRepository>{

    @Autowired
    private ReviewController reviewController;

    public GameController(GameRepository gameRepository) {
        this.repository = gameRepository;
    }

    public List<Game> findGamesByPlatform(Platform platform) {
        List<Game> games = repository.findByPlatform(platform);
        games.forEach(game -> {
            double averageRating = reviewController.calculateAverageRating(game.getId());
            game.setAverageRating(averageRating); 
        });
        return games;
    }
    

    public Game findGameById(Long gameId) {
        return repository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Jogo não encontrado para o ID: " + gameId));
    }

    public List<Game> listWithReviewStats() {
        List<Game> games = repository.findAll();
        games.forEach(game -> {
            double averageRating = reviewController.calculateAverageRating(game.getId());
            game.setAverageRating(averageRating); 
        });
        return games;
    }

    @Override
    protected void validate(Game entity, Mode mode) throws DefaultException{
        super.validate(entity, mode);

        switch (mode) {
            case SAVE:
                System.out.println("save");
//				if(repository.existsByName(entity.getName())) throw new DefaultException("Game existente");
                break;
            case UPDATE:
                System.out.println("update");
                if(!repository.existsById(entity.getId())) throw new DefaultException("Game não existente");
                break;
            case DELETE:
                System.out.println("delete");
                if(!repository.existsById(entity.getId())) throw new DefaultException("Game não existente");
                break;
        }
    }

}