package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.game;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import br.com.ltsoftwaresupport.analyticalflow.controller.GameController;
import br.com.ltsoftwaresupport.analyticalflow.controller.PublisherController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Game;
import br.com.ltsoftwaresupport.analyticalflow.model.Platform;
import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericForm;

@Route(value = "game", layout = MainLayout.class)
@PageTitle("Game")
@RolesAllowed("ADMIN")
public class GameForm extends GenericForm<Game, Long, GameController> implements HasUrlParameter<Long> {

	TextField txtName;
	
	TextField txtWebsite;
	
	Text txtNomeImage;
	
	DatePicker.DatePickerI18n dtReleaseDateFormat;
	
	DatePicker dtReleaseDate;
	
	ComboBox<Publisher> cbxPublisher;
	
	MultiSelectComboBox<Platform> cbxPlatform;
	
	Upload upImage;
	
	byte[] imagem;
	
	MemoryBuffer buffer;
	
	Image image;
	
	private BeanValidationBinder<Game> gameBinder;
	
	private Game gameBean;
	
	GameController gameController;
	
	PublisherController publisherController;
	
	FormLayout platformLayout;
	
	ConfirmDialog platformDialog;
	
	public GameForm(@Autowired GameController gameController, @Autowired PublisherController publisherController) throws DefaultException {
		super(Game.class, gameController, new Game());
		
		setTitle("Games");
		setSuccessRoute("logado/games");
		
		this.gameController = gameController;
		this.publisherController = publisherController;		
		
		setPadding(true);
		
		txtName = new TextField("Nome");
		txtWebsite = new TextField("WebSite");
		cbxPublisher = new ComboBox<Publisher>("Editora");
		cbxPublisher.setItems(publisherController.list());
		cbxPublisher.setItemLabelGenerator(Publisher::getName);	
		dtReleaseDateFormat = new DatePicker.DatePickerI18n();
		dtReleaseDateFormat.setDateFormat("dd/MM/yyyy");	
		dtReleaseDate = new DatePicker("Data de Lançamento");
		dtReleaseDate.setI18n(dtReleaseDateFormat);	
		
		image = new Image();
		
		txtNomeImage = new Text("");
		
		buffer = new MemoryBuffer();
		upImage = new Upload(buffer);
		upImage.setReceiver(buffer);
		upImage.setDropAllowed(false);
		upImage.setMaxFiles(1);
		upImage.setAcceptedFileTypes("image/*");
		upImage.addSucceededListener(e -> {
			try {
				InputStream inputStream = buffer.getInputStream();
				imagem = IOUtils.toByteArray(inputStream);
				txtNomeImage.setText(buffer.getFileName().toString());
				getCurrentBean().setImage(imagem);
				if (image.getParent() != null) {
					remove(image);
				}
				addInForm(addImage(imagem), 12, "null");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		platformLayout = new FormLayout();
		cbxPlatform= new MultiSelectComboBox<Platform>("Plataforma");
		cbxPlatform.setItems(Platform.values());	
		
		addInForm(cbxPublisher, 6, "publisher");
		addInForm(cbxPlatform, 6, "platform");
		addInForm(txtWebsite, 6, "website");
		addInForm(dtReleaseDate, 6, "releaseDate");
		addInForm(txtName, 12, "name");
		addInForm(upImage, 12, "image");	
		
		createBinder();
		
	}

	private Image addImage(byte[] imagem) {
		String nomeArquivo = txtNomeImage.getText().replace("[^a-zA-Z0-9]", " ");
		
		StreamResource resource = new StreamResource(nomeArquivo, () -> new ByteArrayInputStream(imagem));
		image = new Image(resource, "Imagem do Game");
		image.getStyle().set("align-self", "center");
		
		return image;
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
		try {

			if (parameter != null) {
				Game game = controller.load(parameter);
				
				if (game != null) {
					setBean(game);					
					imagem = getCurrentBean().getImage();
					add(addImage(imagem));
				}
			} else {
				setBean(new Game());
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