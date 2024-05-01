package br.com.ltsoftwaresupport.analyticalflow.builder;

import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.model.User;

import java.time.LocalDateTime;

public class ReviewBuilder {

    private GameReview gameReview;

    public static ReviewBuilder build() {
        Game game = new Game();
        User user = new User();
        ReviewBuilder reviewBuilder = new ReviewBuilder();
        reviewBuilder.gameReview = new GameReview();
        reviewBuilder.gameReview.setGame(game);
        reviewBuilder.gameReview.setTitle("Muito louco");
        reviewBuilder.gameReview.setContent("Super maneiro mesmo esse game");
        reviewBuilder.gameReview.setRating(5);
        reviewBuilder.gameReview.setUser(user);
        reviewBuilder.gameReview.setPlatform(Platform.WII);
        reviewBuilder.gameReview.setDate(LocalDateTime.now());
        return reviewBuilder;
    }

    public ReviewBuilder addGame(Game game) {
        gameReview.setGame(game);
        return this;
    }

    public ReviewBuilder addTitle(String title) {
        gameReview.setTitle(title);
        return this;
    }

    public ReviewBuilder addContent(String content) {
        gameReview.setContent(content);
        return this;
    }

    public ReviewBuilder addRating(int rating) {
        gameReview.setRating(rating);
        return this;
    }

    public ReviewBuilder addUser(User user) {
        gameReview.setUser(user);
        return this;
    }

    public ReviewBuilder addPlatform(Platform platform) {
        gameReview.setPlatform(platform);
        return this;
    }

    public ReviewBuilder addDate(LocalDateTime date) {
        gameReview.setDate(date);
        return this;
    }

    public GameReview now() {
        return gameReview;
    }
}
