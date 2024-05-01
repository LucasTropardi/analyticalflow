package br.com.ltsoftwaresupport.analyticalflow.builder;

import br.com.ltsoftwaresupport.analyticalflow.model.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class GameBuilder {

    private Game game;

    public static GameBuilder build() {
        byte[] imageData = new byte[]{1,2,1,2};
        Publisher publisher = new Publisher();
        GameBuilder gameBuilder = new GameBuilder();
        gameBuilder.game = new Game();
        gameBuilder.game.setName("Super Mário Bros");
        gameBuilder.game.setReleaseDate(LocalDate.of(1985, 3, 15));
        gameBuilder.game.setWebsite("http://nintendo.com");
        gameBuilder.game.setPublisher(publisher);
        gameBuilder.game.setPlatform(new HashSet<>());
        gameBuilder.game.setImage(imageData);
        return gameBuilder;
    }

    public GameBuilder addName(String name) {
        game.setName(name);
        return this;
    }

    public GameBuilder addReleaseDate(LocalDate releaseDate) {
        game.setReleaseDate(releaseDate);
        return this;
    }

    public GameBuilder addWebsite(String website) {
        game.setWebsite(website);
        return this;
    }

    public GameBuilder addPublisher(Publisher publisher) {
        game.setPublisher(publisher);
        return this;
    }

    public GameBuilder addPlatforms(Set<Platform> platforms) {
        game.setPlatform(platforms);
        return this;
    }

    public GameBuilder addImage(byte[] image) {
        game.setImage(image);
        return this;
    }

    public Game now() {
        return game;
    }
}
