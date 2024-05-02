package br.com.ltsoftwaresupport.analyticalflow.views.principal;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import br.com.ltsoftwaresupport.constants.Constants;
import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.controller.ReviewController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.security.SecurityService;
import br.com.ltsoftwaresupport.analyticalflow.service.StateStorageService;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;

@Route(value = "logado/auth-reviews", layout = MainLayout.class)
@PageTitle("Game Reviews")
@RolesAllowed({"ADMIN", "USER"})
public class ReviewAuthGameView extends VerticalLayout {

    HorizontalLayout cabecalho;

    private LogoLayoutGame logoLayoutGame;

    TextField search;

    VerticalLayout body;

    H2 titulo;

    Hr linha;

    ReviewController reviewController;

    GameController gameController;

    SecurityService securityService;

    byte[] image;

    Long gameId;

    Div alertDiv;

    @Autowired
    public ReviewAuthGameView(
        ReviewController reviewController, 
        StateStorageService stateStorageService, 
        GameController gameController, 
        SecurityService securityService
        ) {
        this.reviewController = reviewController;
        this.gameController = gameController;
        this.securityService = securityService;
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        search = new TextField("Pesquisar");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());

        cabecalho = new HorizontalLayout(search);
        cabecalho.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cabecalho.setAlignItems(FlexComponent.Alignment.CENTER);
        cabecalho.setWidthFull();

        body = new VerticalLayout();
        body.setSizeFull();
        body.setAlignItems(Alignment.CENTER);

        this.logoLayoutGame = new LogoLayoutGame();

        

        add(logoLayoutGame);

        this.gameId = stateStorageService.getGameId();
        if (this.gameId != null) {
            loadGameAndReviews(this.gameId);
        } else {
            Notification.show("Game ID não fornecido", 300, Notification.Position.MIDDLE);
        }

        add(body);
    }

    private void loadGameAndReviews(Long gameId) {
        Game game = gameController.findGameById(gameId);
        if (game == null) {
            Notification.show("Jogo não encontrado.", 300, Notification.Position.MIDDLE);
            return;
        }

        createGameHeader(game.getName(), game.getImage());

        try {
            List<GameReview> gameReviews = reviewController.listByGame(gameId);
            if (!gameReviews.isEmpty()) {
                FlexLayout cardsLayout = new FlexLayout();
                cardsLayout.addClassName("flex-container");
                cardsLayout.setWidthFull();
                for (GameReview gameReview : gameReviews) {
                    Div card = createReviewCard(gameReview);
                    cardsLayout.add(card);
                }
                body.add(cardsLayout);
            }
        } catch (DefaultException e) {
            Notification.show("Erro ao carregar reviews: " + e.getMessage(), 500, Notification.Position.MIDDLE);
        }
    }

    private Div createGameHeader(String gameTitle, byte[] gameImage) {
        Div gameHeaderDiv = new Div();
        gameHeaderDiv.addClassName("game-header");

        H3 title = new H3(gameTitle);
        Image image = new Image(new StreamResource("gameImage", () -> new ByteArrayInputStream(gameImage)), "Imagem do Game");
        image.getStyle().set("width", "auto").set("height", "auto");
        image.addClassName("game-logo");

        VerticalLayout layout = new VerticalLayout(title, image);
        layout.setAlignItems(Alignment.CENTER);

        Button addReviewButton = new Button(" GameReview", VaadinIcon.PLUS.create());
        addReviewButton.addClickListener(e -> UI.getCurrent().navigate("admin/review"));

        gameHeaderDiv.add(layout);
        body.add(gameHeaderDiv, addReviewButton);

        return gameHeaderDiv;
    }

    private Div createReviewCard(GameReview review) {
        Div card = new Div();
        card.addClassName("game-card");
        card.getStyle().set("cursor", "default");

        H3 title = new H3(review.getTitle());
        title.addClassName("card-title");

        HorizontalLayout ratingStars = generateRatingStars(review.getRating());

        H5 content = new H5(review.getContent());
        content.addClassName("card-content");
        
        H5 date = new H5("Publicado em: " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(review.getDate()));
        date.addClassName("card-content");

        H5 user = new H5("Avaliador: " + review.getUser().getName());
        user.addClassName("card-content");

        Div detailsDiv = new Div(ratingStars, content, user, date);
        detailsDiv.addClassName("game-details");

        card.add(title, detailsDiv);

        return card;
    }

    private HorizontalLayout generateRatingStars(int rating) {
        HorizontalLayout starsLayout = new HorizontalLayout();
        starsLayout.setPadding(false);
        starsLayout.setSpacing(false);
        starsLayout.addClassName("rating-stars-container");
        starsLayout.setWidthFull();
    
        // estrelas completas
        for (int i = 0; i < rating; i++) {
            Image starImage = new Image(Constants.STAR_URL, "star");
            starImage.setWidth("20px");
            starImage.setHeight("20px");
            starsLayout.add(starImage);
        }
    
        // estrelas vazias
        for (int i = rating; i < 5; i++) {
            Image emptyStarImage = new Image(Constants.EMPTY_STAR_URL, "empty star");
            emptyStarImage.setWidth("20px");
            emptyStarImage.setHeight("20px");
            starsLayout.add(emptyStarImage);
        }
    
        return starsLayout;
    }
}