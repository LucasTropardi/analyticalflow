package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.publisher;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.ltsoftwaresupport.analyticalflow.controller.PublisherController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericForm;

@Route(value = "logado/publisher", layout = MainLayout.class)
@PageTitle("Editoras")
@RolesAllowed({"ADMIN"})
public class PublisherForm extends GenericForm<Publisher, Long, PublisherController> implements HasUrlParameter<Long>  {
	
	TextField txtName;
	
	TextField txtWebsite;
		
	private BeanValidationBinder<Publisher> binder;
	
	public PublisherForm(@Autowired PublisherController publisherController) {
		super(Publisher.class, publisherController, new Publisher());
		
		setTitle("Publisher");
		setSuccessRoute("logado/publishers");
		
		txtName = new TextField("Nome");
		txtWebsite = new TextField("Website");
		
		addInForm(txtName, 6, "name");
		addInForm(txtWebsite, 6, "website");
			
		createBinder();	
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
		try {

			if (parameter != null) {
				setBean(controller.load(parameter)); 
			} else {
				setBean(new Publisher());
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