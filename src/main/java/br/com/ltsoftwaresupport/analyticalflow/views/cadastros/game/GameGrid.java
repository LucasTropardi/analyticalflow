package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.game;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericGrid;

@Route(value = "games", layout = MainLayout.class)
@PageTitle("Games")
@RolesAllowed("ADMIN")
public class GameGrid extends GenericGrid<Game, Long, GameController> {
	
	TextField search;
	
	
	public GameGrid(@Autowired GameController gameController) throws DefaultException {
		super(gameController, Game.class, Game::getId);
		setTitle("Games");
		setRotaForm("game");
		configurarGrid();

    }

	private void configurarGrid() {
        getGrid().addColumn(Game::getId).setHeader("Id");
        getGrid().addColumn(Game::getName).setHeader("Nome");
        getGrid().addColumn(Game::getWebsite).setHeader("WebSite");
        getGrid().addColumn(game -> game.getPublisher().getName()).setHeader("Publisher");
        getGrid().addColumn(game -> DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(game.getReleaseDate())).setHeader("Data de Lançamento");    
        getGrid().addColumn(game -> {
        	List<Platform> platforms = new ArrayList<>(game.getPlatform());
        	List<String> platformsNames = platforms.stream().map(Platform::toString).collect(Collectors.toList());
        	return String.join(", ", platformsNames);
        }).setHeader("Plataformas");
    }

}