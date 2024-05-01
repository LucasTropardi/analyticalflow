package br.com.ltsoftwaresupport.analyticalflow.views.principal;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
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
import com.vaadin.flow.server.auth.AnonymousAllowed;

import br.com.ltsoftwaresupport.constants.Constants;
import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.controller.ReviewController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.service.StateStorageService;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.PrincipalLayout;

@Route(value = "reviews", layout = PrincipalLayout.class)
@PageTitle("Game Reviews")
@AnonymousAllowed
public class ReviewGameView extends VerticalLayout {

    HorizontalLayout cabecalho;

    private LogoLayoutGame logoLayoutGame;

    TextField search;

    VerticalLayout body;

    H2 titulo;

    Hr linha;

    ReviewController reviewController;

    GameController gameController;

    byte[] image;

    Long gameId;

    Div alertDiv;

    @Autowired
    public ReviewGameView(ReviewController reviewController, StateStorageService stateStorageService, GameController gameController) {
        this.reviewController = reviewController;
        this.gameController = gameController;
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
        this.alertDiv = createLoginAlert();

        add(logoLayoutGame, alertDiv);

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

        Div gameHeader = createGameHeader(game.getName(), game.getImage());
        add(gameHeader);

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

    private Div createReviewCard(GameReview gameReview) {
        Div card = new Div();
        card.addClassName("game-card");
        card.getStyle().set("cursor", "default");
    
        H3 title = new H3(gameReview.getTitle());
        title.addClassName("card-title");

        HorizontalLayout ratingStars = generateRatingStars(gameReview.getRating());

        H5 content = new H5(gameReview.getContent());
        content.addClassName("card-content");
        
        H5 date = new H5("Publicado em: " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(gameReview.getDate()));
        date.addClassName("card-content");
    
        H5 user = new H5("Avaliador: " + gameReview.getUser().getName());
        user.addClassName("card-content");
    
        Div detailsDiv = new Div(ratingStars, content, user, date);
        detailsDiv.addClassName("game-details");
    
        card.add(title, detailsDiv);
    
        return card;
    }

    private Div createLoginAlert() {
        Div messagePart1 = new Div();
        messagePart1.setText("Para cadastrar uma review, é necessário fazer");

        Anchor loginLink = new Anchor(" login ", "login");
        loginLink.getStyle().set("color", "blue")
            .set("text-decoration", "underline");
    
        Div messagePart2 = new Div();
        messagePart2.setText(".");
    
        HorizontalLayout messageLayout = new HorizontalLayout(messagePart1, loginLink, messagePart2);
        messageLayout.addClassName("alert-message-layout");
        messageLayout.setAlignItems(Alignment.CENTER);
        messageLayout.getStyle().set("color", "red")
            .set("font-weight", "bold")
            .set("padding", "4px")
            .set("gap", "3px");

        Icon warningIcon = VaadinIcon.WARNING.create();
        warningIcon.getStyle().set("width", "20px")
            .set("height", "25px")
            .set("color", "red");
    
        HorizontalLayout alertLayout = new HorizontalLayout(warningIcon, messageLayout);
        alertLayout.setAlignItems(Alignment.CENTER);
    
        return new Div(alertLayout); 
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
    
        gameHeaderDiv.add(layout);
    
        return gameHeaderDiv;
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