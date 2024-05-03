package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.review;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;

import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.controller.ReviewController;
import br.com.ltsoftwaresupport.analyticalflow.controller.UserController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.GameReview;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.service.StateStorageService;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericForm;

public class FormReview extends GenericForm<GameReview, Long, ReviewController> implements HasUrlParameter<Long> {

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
	
	UserController user1Controller;
	
	@Autowired
    private StateStorageService stateStorageService;
	
	public FormReview(
		@Autowired ReviewController reviewController, 
		@Autowired GameController gameController, 
		@Autowired UserController userController, 
		Game defaultGame, 
		User defaultUser
		) throws DefaultException {
		super(GameReview.class, reviewController, new GameReview());
		setTitle("Reviews");
		setSuccessRoute("logado/auth-review");
		this.reviewController = reviewController;	
		this.gameController = gameController;
		this.user1Controller = userController;
		
		txtTitle = new TextField("Título");
		txtContent = new TextArea("Conteúdo");
		cbxGame = new ComboBox<Game>("Game");
		cbxGame.setItems(gameController.list());
		cbxGame.setItemLabelGenerator(Game::getName);	
		cbxGame.setValue(defaultGame);
		dtDateFormat = new DatePicker.DatePickerI18n();
		dtDateFormat.setDateFormat("dd/MM/yyyy");	
		dtDate = new DateTimePicker("Data");
		dtDate.setDatePickerI18n(dtDateFormat);	
		
		cbxPlatform= new ComboBox<Platform>("Plataforma");
		cbxPlatform.setItems(Platform.values());
		
		cbxUser = new ComboBox<User>("Usuário");
		cbxUser.setItems(userController.list());
		cbxUser.setItemLabelGenerator(User::getName);
		cbxUser.setValue(defaultUser);

		cbxGame.setEnabled(false);
		cbxGame.setVisible(false);
        cbxUser.setEnabled(false);
		cbxUser.setVisible(false);
		
		rating = new IntegerField("Pontuação");
		rating.setMin(1);
		rating.setMax(5);
		rating.setValue(1);
		rating.setStepButtonsVisible(true);
		
		addInForm(cbxGame, 3, "game");
		addInForm(cbxUser, 3, "user");
		addInForm(txtTitle, 5, "title");
		addInForm(cbxPlatform, 3, "platform");
		addInForm(dtDate, 3, "date");
		addInForm(rating, 1, "rating");
		addInForm(txtContent, 12, "content");
		
		stateStorageService.setGameId(defaultGame.getId());
		createBinder();
		
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
		try {

			if (parameter != null) {
				GameReview review = controller.load(parameter);
				if (review != null) {
					setBean(review);
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