package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.builder.GameBuilder;
import br.com.ltsoftwaresupport.analyticalflow.builder.PublisherBuilder;
import br.com.ltsoftwaresupport.analyticalflow.builder.ReviewBuilder;
import br.com.ltsoftwaresupport.analyticalflow.builder.UserBuilder;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewTest {

    @Autowired
    ReviewController reviewController;

    @Autowired
    UserController userController;

    @Autowired
    ContactController contactController;

    @Autowired
    GameController gameController;

    @Autowired
    PublisherController publisherController;

    @Test
    @Order(1)
    void save() throws DefaultException {
        User user = UserBuilder.build()
                .addUsername("lucasc")
                .addPassword("teste")
                .addName("Lucas")
                .addLastname("teste")
                .addRole(Role.USER)
                .now();
        try {
            user = userController.load("lucasc");
        } catch (DefaultException e) {
            user = userController.save(user);
        }

        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        publisher = publisherController.save(publisher);

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.WII);
        byte[] imageData = new byte[]{1, 2, 3, 4};
        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();
        game = gameController.save(game);

        GameReview gameReview = ReviewBuilder.build()
                .addGame(game)
                .addTitle("Ótimo jogo")
                .addContent("O jogo é desafiador e divertido.")
                .addRating(5)
                .addUser(user)
                .addPlatform(Platform.WII)
                .addDate(LocalDateTime.now())
                .now();
        assertNotNull(reviewController.save(gameReview));

        reviewController.delete(gameReview);
        assertThrows(DefaultException.class, () -> reviewController.load(gameReview.getId()));

        gameController.delete(game);
        Game finalGame = game;
        assertThrows(DefaultException.class, () -> gameController.load(finalGame.getId()));

        publisherController.delete(publisher);
        Publisher finalPublisher = publisher;
        assertThrows(DefaultException.class, () -> publisherController.load(finalPublisher.getId()));
    }

    @Test
    @Order(2)
    void list() {
        List<GameReview> gameReviews = reviewController.list();
        gameReviews.forEach(
                e -> System.out.println(e.toString())
        );
        assertNotNull(gameReviews);
    }

    @Test
    @Order(3)
    void load() throws DefaultException {
        User user = UserBuilder.build()
                .addUsername("lucasc")
                .addPassword("teste")
                .addName("Lucas")
                .addLastname("teste")
                .addRole(Role.USER)
                .now();
        try {
            user = userController.load("lucasc");
        } catch (DefaultException e) {
            user = userController.save(user);
        }

        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        publisher = publisherController.save(publisher);

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.WII);
        byte[] imageData = new byte[]{1, 2, 3, 4};
        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();
        game = gameController.save(game);

        GameReview gameReview = ReviewBuilder.build()
                .addGame(game)
                .addTitle("Ótimo jogo")
                .addContent("O jogo é desafiador e divertido.")
                .addRating(5)
                .addUser(user)
                .addPlatform(Platform.WII)
                .addDate(LocalDateTime.now())
                .now();
        gameReview = reviewController.save(gameReview);

        GameReview loadedReview = null;
        try {
            loadedReview = reviewController.load(gameReview.getId());
            assertNotNull(loadedReview);
        } catch (DefaultException e) {
            fail("Não foi possível carregar a review");
        }

        reviewController.delete(loadedReview);
        GameReview finalLoadedReview = loadedReview;
        assertThrows(DefaultException.class, () -> reviewController.load(finalLoadedReview.getId()));

        gameController.delete(game);
        Game finalGame = game;
        assertThrows(DefaultException.class, () -> gameController.load(finalGame.getId()));

        publisherController.delete(publisher);
        Publisher finalPublisher = publisher;
        assertThrows(DefaultException.class, () -> publisherController.load(finalPublisher.getId()));
    }

    @Test
    @Order(4)
    void update() throws DefaultException {
        User user = UserBuilder.build()
                .addUsername("lucasc")
                .addPassword("teste")
                .addName("Lucas")
                .addLastname("teste")
                .addRole(Role.USER)
                .now();
        try {
            user = userController.load("lucasc");
        } catch (DefaultException e) {
            user = userController.save(user);
        }

        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        publisher = publisherController.save(publisher);

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.WII);
        byte[] imageData = new byte[]{1, 2, 3, 4};
        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();
        game = gameController.save(game);

        GameReview gameReview = ReviewBuilder.build()
                .addGame(game)
                .addTitle("Ótimo jogo")
                .addContent("O jogo é desafiador e divertido.")
                .addRating(5)
                .addUser(user)
                .addPlatform(Platform.WII)
                .addDate(LocalDateTime.now())
                .now();
        gameReview = reviewController.save(gameReview);

        GameReview updatedReview = ReviewBuilder.build()
                .addGame(game)
                .addUser(user)
                .addTitle("Desafiador")
                .addContent("É um jogo bem legal e difícil.")
                .addRating(gameReview.getRating())
                .addPlatform(Platform.XBOX)
                .addDate(gameReview.getDate())
                .now();
        updatedReview.setId(gameReview.getId());
        updatedReview = reviewController.update(updatedReview);

        assertEquals("É um jogo bem legal e difícil.", updatedReview.getContent());
        assertEquals("Desafiador", updatedReview.getTitle());
        assertEquals(Platform.XBOX, updatedReview.getPlatform());

        reviewController.delete(updatedReview);
        gameController.delete(game);
        publisherController.delete(publisher);

        GameReview finalUpdatedReview = updatedReview;
        assertThrows(DefaultException.class, () -> reviewController.load(finalUpdatedReview.getId()));
        Game finalGame = game;
        assertThrows(DefaultException.class, () -> gameController.load(finalGame.getId()));
        Publisher finalPublisher = publisher;
        assertThrows(DefaultException.class, () -> publisherController.load(finalPublisher.getId()));
    }

    @Test
    @Order(5)
    void delete() throws DefaultException {
        User user = UserBuilder.build()
                .addUsername("lucasc")
                .addPassword("teste")
                .addName("Lucas")
                .addLastname("teste")
                .addRole(Role.USER)
                .now();
        try {
            user = userController.load("lucasc");
        } catch (DefaultException e) {
            user = userController.save(user);
        }

        Publisher publisher = PublisherBuilder.build()
                .addName("Konami")
                .addWebsite("http://www.konami.com")
                .now();
        publisher = publisherController.save(publisher);

        Set<Platform> platforms = new HashSet<>();
        platforms.add(Platform.WII);
        byte[] imageData = new byte[]{1, 2, 3, 4};
        Game game = GameBuilder.build()
                .addName("Contra III")
                .addReleaseDate(LocalDate.parse("1992-02-28"))
                .addWebsite("http://www.konami.com")
                .addPublisher(publisher)
                .addPlatforms(platforms)
                .addImage(imageData)
                .now();
        game = gameController.save(game);

        GameReview gameReview = ReviewBuilder.build()
                .addGame(game)
                .addTitle("Ótimo jogo")
                .addContent("O jogo é desafiador e divertido.")
                .addRating(5)
                .addUser(user)
                .addPlatform(Platform.WII)
                .addDate(LocalDateTime.now())
                .now();
        assertNotNull(reviewController.save(gameReview));

        reviewController.delete(gameReview);
        assertThrows(DefaultException.class, () -> reviewController.load(gameReview.getId()));

        gameController.delete(game);
        Game finalGame = game;
        assertThrows(DefaultException.class, () -> gameController.load(finalGame.getId()));

        publisherController.delete(publisher);
        Publisher finalPublisher = publisher;
        assertThrows(DefaultException.class, () -> publisherController.load(finalPublisher.getId()));
    }
}
