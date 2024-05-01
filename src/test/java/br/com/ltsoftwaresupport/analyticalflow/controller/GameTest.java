package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.builder.GameBuilder;
import br.com.ltsoftwaresupport.analyticalflow.builder.PublisherBuilder;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameTest {
    @Autowired
    GameController gameController;

    @Autowired
    PublisherController publisherController;

    @Test
    @Order(1)
    void save() throws DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        assertNotNull(publisherController.save(publisher));

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.WII);
        byte[] imageData = new byte[]{1,2,1,2};

        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();

        assertNotNull(gameController.save(game));

        gameController.delete(game);
        assertThrows(DefaultException.class, () -> {
            gameController.load(game.getId());
        });

        publisherController.delete(publisher);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisher.getId());
        });
    }

    @Test
    @Order(2)
    void list() throws DefaultException {
        List<Game> games = gameController.list();
        assertNotNull(games);

        games.forEach(game -> {
            System.out.println(game.toString());

            if (game.getPlatform() != null) {
                game.getPlatform().forEach(platform -> {
                    System.out.println("\t" + platform.toString());
                });
            }
        });
    }

    @Test
    @Order(3)
    void load() throws DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        assertNotNull(publisherController.save(publisher));

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.XBOX);
        byte[] imageData = new byte[]{1, 2, 1, 2};

        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();
        Game savedGame = gameController.save(game);
        assertNotNull(savedGame);

        Game loadedGame = null;
        try {
            loadedGame = gameController.load(savedGame.getId());
        } catch (DefaultException e) {
            fail("Não foi possível carregar o game");
        }

        assertNotNull(loadedGame);
        assertEquals(savedGame.getId(), loadedGame.getId());

        gameController.delete(savedGame);
        assertThrows(DefaultException.class, () -> {
            gameController.load(savedGame.getId());
        });

        publisherController.delete(publisher);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisher.getId());
        });
    }


    @Test
    @Order(4)
    void update() throws DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        assertNotNull(publisherController.save(publisher));

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.PLAYSTATION);
        byte[] imageData = new byte[]{1, 2, 1, 2};

        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();
        Game savedGame = gameController.save(game);
        assertNotNull(savedGame);

        savedGame.setName("Contra 3");
        savedGame.setWebsite("http://www.kinamo.com");

        Game updatedGame = gameController.update(savedGame);
        assertEquals("Contra 3", updatedGame.getName());
        assertEquals("http://www.kinamo.com", updatedGame.getWebsite());

        gameController.delete(updatedGame);
        assertThrows(DefaultException.class, () -> {
            gameController.load(updatedGame.getId());
        });

        publisherController.delete(publisher);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisher.getId());
        });
    }


    @Test
    @Order(5)
    void delete() throws DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        assertNotNull(publisherController.save(publisher));

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.WII);
        byte[] imageData = new byte[]{1,2,1,2};

        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();

        assertNotNull(gameController.save(game));

        gameController.delete(game);
        assertThrows(DefaultException.class, () -> {
            gameController.load(game.getId());
        });

        publisherController.delete(publisher);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisher.getId());
        });
    }
}
