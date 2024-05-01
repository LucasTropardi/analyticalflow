package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.repository.GameRepository;
import br.com.ltsoftwaresupport.analyticalflow.repository.GameReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewController extends GenericController<GameReview, Long, GameReviewRepository> {

    private final GameRepository gameRepository;

    public ReviewController(GameReviewRepository gameReviewRepository, GameRepository gameRepository) {
        this.repository = gameReviewRepository;
        this.gameRepository = gameRepository;
    }

    public List<GameReview> list() {
        return this.repository.findAll();
    }

    public List<GameReview> listByGame(Long gameId) throws DefaultException {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new DefaultException("GameReview não encontrada"));
        return this.repository.findByGame(game);
    }

    public GameReview load(Long id) throws DefaultException {

        return this.repository.findById(id)
                .orElseThrow(() -> new DefaultException("Item não encontrado"));

    }

    public List<GameReview> findAllByUsername(String filter) {
    	if(!filter.isEmpty()) {
    		return repository.findAllByUserUsername(filter);
    	}else {
			return repository.findAll();
		}	
	}

    public double calculateAverageRating(Long gameId) {
        List<GameReview> gameReviews = repository.findByGameId(gameId);
        return gameReviews.stream()
                      .mapToInt(GameReview::getRating)
                      .average()
                      .orElse(0.0);
    }

    
    @Override
    protected void validate(GameReview entity, Mode mode) throws DefaultException {
        super.validate(entity, mode);

        switch (mode) {
            case SAVE:
                System.out.println("save");
                break;
            case UPDATE:
                System.out.println("update");
                if(!repository.existsById(entity.getId())) throw new DefaultException("Não existe");
                break;
            case DELETE:
                System.out.println("delete");
                if(!repository.existsById(entity.getId())) throw new DefaultException("Não existe");
                break;
        }
    }
}
