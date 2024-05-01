package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.review;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import br.com.ltsoftwaresupport.analyticalflow.controller.ReviewController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.model.Role;
import br.com.ltsoftwaresupport.analyticalflow.security.SecurityService;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericGrid;

@Route(value = "admin/reviews", layout = MainLayout.class)
@PageTitle("Reviews | GameRating")
@PermitAll
public class ReviewGrid extends GenericGrid<GameReview, Long, ReviewController> {
	
	
	public ReviewGrid(@Autowired ReviewController reviewController, @Autowired SecurityService securityService) throws DefaultException {
		super(reviewController, GameReview.class, GameReview::getId);
		setTitle("Reviews");
		setRotaForm("admin/review");
		configurarGrid();
		
		if (securityService.getCurrentUser().get().getRole().equals(Role.USER)) {
			refreshList(reviewController.findAllByUsername(securityService.getCurrentUser().get().getUsername()));
		}
	}

	private void configurarGrid() {
        getGrid().addColumn(GameReview::getId).setHeader("Id");
        getGrid().addColumn(GameReview::getTitle).setHeader("Título");
        getGrid().addColumn(GameReview::getContent).setHeader("Conteúdo");
        getGrid().addColumn(review -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(review.getDate())).setHeader("Data");
        getGrid().addColumn(review -> review.getGame() != null ? review.getGame().getName() : "").setHeader("Game");
        getGrid().addColumn(GameReview::getPlatform).setHeader("Plataforma");
        getGrid().addColumn(GameReview::getRating).setHeader("Pontuação");
        getGrid().addColumn(review -> review.getUser() != null ? review.getUser().getName() : "").setHeader("Usuário");
        
    }

}