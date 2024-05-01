package br.com.ltsoftwaresupport.analyticalflow.views.principal;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.service.StateStorageService;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.constants.Constants;

@Route(value = "/logado", layout = MainLayout.class)
@PageTitle("Games")
@RolesAllowed({"ADMIN", "USER"})
public class PrincipalAuthGameView  extends VerticalLayout {

    HorizontalLayout cabecalho;

    private LogoLayoutGame logoLayoutGame;

    TextField search;

    VerticalLayout body;

    H2 titulo;

    Hr linha;

    GameController gameController;

    byte[] image;

    @Autowired
    private StateStorageService stateStorageService;

    public PrincipalAuthGameView(@Autowired GameController gameController, @Autowired StateStorageService stateStorageService) throws DefaultException {
    	this.gameController = gameController;
        this.stateStorageService = stateStorageService;

        setPadding(true);
        setSizeFull();
        setAlignItems(Alignment.CENTER);

        search = new TextField();
        search.setPlaceholder("Pesquisar");
        search.setPrefixComponent(VaadinIcon.SEARCH.create());

        this.logoLayoutGame = new LogoLayoutGame();

        cabecalho = new HorizontalLayout(search);
        cabecalho.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        cabecalho.setAlignItems(FlexComponent.Alignment.CENTER);
        cabecalho.setWidthFull();

        add(logoLayoutGame);
        add(addGames(gameController));
    }

    private FlexLayout addGames(GameController gameController) throws DefaultException {
        List<Game> games = gameController.listWithReviewStats(); 
    
        FlexLayout cardsLayout = new FlexLayout();
        cardsLayout.addClassName("flex-container");
        cardsLayout.setWidthFull();
    
        for (Game game : games) {
            Div card = createGameCard(game);
            cardsLayout.add(card);
        }
    
        return cardsLayout;
    }

    private Div createGameCard(Game game) {
        Div card = new Div();
        card.addClassName("game-card");
        
        H3 title = new H3(game.getName());
        title.addClassName("card-title");
    
        Image gameImage = new Image(new StreamResource(game.getName(), () -> new ByteArrayInputStream(game.getImage())), "Imagem do Game");
        gameImage.addClassName("game-image");
    
        H5 releaseDate = new H5("Data de Lançamento: " + DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(game.getReleaseDate()));
        releaseDate.addClassName("card-content");
    
        H5 website = new H5("Site: " + game.getWebsite());
        website.addClassName("card-content");
    
        H5 publisher = new H5("Editora: " + game.getPublisher().getName());
        publisher.addClassName("card-content");
    
        H5 platforms = new H5("Plataforma: " + game.getPlatform().stream().map(Platform::toString).collect(Collectors.joining(", ")));
        platforms.addClassName("card-content");

        double averageRating = game.getAverageRating(); 
        HorizontalLayout ratingStars = generateRatingStars(averageRating);
        Div detailsDiv = new Div(ratingStars, platforms, publisher, website, releaseDate);
        detailsDiv.addClassName("game-details");
    
        card.add(title, gameImage, detailsDiv);
        card.addClickListener(event -> navigateToReviews(game.getId()));
    
        return card;
    }

    private HorizontalLayout generateRatingStars(double rating) {
        HorizontalLayout starsLayout = new HorizontalLayout();
        starsLayout.setPadding(false);
        starsLayout.setSpacing(false);
        starsLayout.addClassName("rating-stars-container");
        starsLayout.setWidthFull();

        int fullStars = (int) rating; 
        double fractionalPart = rating - fullStars;
        int emptyStars = 5 - fullStars - (fractionalPart == 0 ? 0 : 1);

        // estrelas completas
        for (int i = 0; i < fullStars; i++) {
            Image starImage = new Image(Constants.STAR_URL, "star");
            starImage.setWidth("20px");
            starImage.setHeight("20px");
            starsLayout.add(starImage);
        }

        // meia estrela
        if (fractionalPart > 0) {
            Image halfStarImage = new Image(Constants.HALF_STAR_URL, "half star");
            halfStarImage.setWidth("20px");
            halfStarImage.setHeight("20px");
            starsLayout.add(halfStarImage);
        }

        // estrelas vazias
        for (int i = 0; i < emptyStars; i++) {
            Image emptyStarImage = new Image(Constants.EMPTY_STAR_URL, "empty star");
            emptyStarImage.setWidth("20px");
            emptyStarImage.setHeight("20px");
            starsLayout.add(emptyStarImage);
        }

        return starsLayout;
    }
    
    private void navigateToReviews(Long gameId) {
        stateStorageService.setGameId(gameId);
        getUI().ifPresent(ui -> ui.navigate("logado/reviews"));
    }

}