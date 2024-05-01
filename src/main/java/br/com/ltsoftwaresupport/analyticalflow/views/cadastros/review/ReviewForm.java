package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.review;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.controller.ReviewController;
import br.com.ltsoftwaresupport.analyticalflow.controller.UserController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericForm;

@Route(value = "admin/review", layout = MainLayout.class)
@PageTitle("Reviews | GameRating")
@PermitAll
public class ReviewForm extends GenericForm<GameReview, Long, ReviewController> implements HasUrlParameter<Long> {

	TextField txtTitle;
	
	TextArea txtContent;
	
	DatePicker.DatePickerI18n dtDateFormat;
	
	DateTimePicker dtDate;
	
	ComboBox<Game> cbxGame;
	
	ComboBox<Platform> cbxPlatform;
	
	ComboBox<User> cbxUser; 
	
	IntegerField rating;
	
	
	private BeanValidationBinder<GameReview> reviewBinder;
	
	private GameReview reviewBean;
	
	ReviewController reviewController;
	
	GameController gameController;
	
	UserController userController;
	
	public ReviewForm(@Autowired ReviewController reviewController, @Autowired GameController gameController, @Autowired UserController userController) throws DefaultException {
		super(GameReview.class, reviewController, new GameReview());
		setTitle("Review");
		setSuccessRoute("logado");
		this.reviewController = reviewController;	
		this.gameController = gameController;
		this.userController = userController;
		
		txtTitle = new TextField("Título");
		txtContent = new TextArea("Conteúdo");
		cbxGame = new ComboBox<Game>("Game");
		cbxGame.setItems(gameController.list());
		cbxGame.setItemLabelGenerator(Game::getName);	
		dtDateFormat = new DatePicker.DatePickerI18n();
		dtDateFormat.setDateFormat("dd/MM/yyyy");	
		dtDate = new DateTimePicker("Data");
		dtDate.setDatePickerI18n(dtDateFormat);	
		
		cbxPlatform = new ComboBox<Platform>("Plataforma");
		cbxPlatform.setItems(Platform.values());
		
		cbxUser = new ComboBox<User>("Usuário");
		cbxUser.setItems(userController.list());
		cbxUser.setItemLabelGenerator(User::getName);
		
		rating = new IntegerField("Pontuação");
		rating.setMin(1);
		rating.setMax(5);
		rating.setValue(1);
		rating.setStepButtonsVisible(true);
		
		addInForm(cbxGame, 6, "game");
		addInForm(cbxPlatform, 6, "platform");
		addInForm(dtDate, 5, "date");
		addInForm(rating, 1, "rating");
		addInForm(cbxUser, 6, "user");
		addInForm(txtTitle, 12, "title");
		addInForm(txtContent, 12, "content");
		
		
		
		
	
		createBinder();
		
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
		try {

			if (parameter != null) {
				GameReview gameReview = controller.load(parameter);
				if (gameReview != null) {
					setBean(gameReview);
				}
			} else {
				setBean(new GameReview());
			}

		} catch (DefaultException e) {
			mostrarAviso();
		}
		createBinder();
	}

	private void mostrarAviso() {
		ConfirmDialog dialogMessage = new ConfirmDialog();
		dialogMessage.setHeader("Registro não encontrado");
		dialogMessage.setText(new Html("<p>Clique no botão para visualizar todos os registros</p>"));

		dialogMessage.setConfirmText("Redirecionar");

		dialogMessage.addConfirmListener(ev -> {
			voltar();
			dialogMessage.close();
		});
		dialogMessage.open();
	}

}