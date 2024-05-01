package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.user;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import br.com.ltsoftwaresupport.analyticalflow.controller.ContactController;
import br.com.ltsoftwaresupport.analyticalflow.controller.UserController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Contact;
import br.com.ltsoftwaresupport.analyticalflow.model.ContactType;
import br.com.ltsoftwaresupport.analyticalflow.model.Role;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericForm;

@Route(value = "user", layout = MainLayout.class)
@PageTitle("Users | GameRating")
@AnonymousAllowed
public class UserForm extends GenericForm<User, String, UserController> implements HasUrlParameter<String> {

	TextField txtContact;

	TextField txtUsername;

	TextField txtName;
	
	TextField txtLastname;

	TextField txtValue;

	PasswordField passPassword;

	Button btnNovoContact;

	ComboBox<Role> cbxRole;

	ComboBox<ContactType> cbxContactType;

	Grid<Contact> gridContact;

	private BeanValidationBinder<User> binder;
	
	private BeanValidationBinder<Contact> binderContact;

	private Contact contactBean;

	ContactController contactController;
	
	UserController userController;
	
	FormLayout contactLayout;

	ConfirmDialog contactDialog;

	public UserForm(@Autowired UserController userController, @Autowired ContactController contactController) throws DefaultException {
		super(User.class, userController, new User());
		
		setTitle("Users");
		setSuccessRoute("users");

		txtUsername = new TextField("Usuário");
		txtName = new TextField("Nome");
		txtLastname = new TextField("Sobrenome");
		passPassword = new PasswordField("Senha");
		cbxRole = new ComboBox<Role>("Tipo");
		cbxRole.setItems(Role.values());

		contactLayout = new FormLayout();
		txtValue = new TextField("Contato");
		cbxContactType = new ComboBox<ContactType>("Tipo");
		cbxContactType.setItems(ContactType.values());
		btnNovoContact = new Button("Novo Contato");
		btnNovoContact.addClickListener(e -> cadastraContact(new Contact()));

		gridContact = new Grid<Contact>(Contact.class, true);
		gridContact.addClassName("form-container");
		gridContact.setItems(new ArrayList<>());
		gridContact.setSelectionMode(Grid.SelectionMode.MULTI);
		gridContact.addComponentColumn(contact -> {
            Button editButton = new Button(VaadinIcon.TRASH.create(), e -> {
                gridContact.getListDataView().removeItem(contact);
            });
            editButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            editButton.addClickListener(e -> {
                gridContact.getListDataView().removeItem(contact);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);
		gridContact.addItemClickListener(e -> {
			Contact contact = e.getItem();

			cadastraContact(contact);
		});
		
		addInForm(txtName, 6, "name");
		addInForm(txtLastname, 6, "lastname");
		addInForm(txtUsername, 6, "username");
		addInForm(passPassword, 6, "password");
		addInForm(cbxRole, 12, "role");
	
		add(btnNovoContact, gridContact);
		
		createBinder();		
	}

	private void cadastraContact(Contact contact) {
		contactDialog = new ConfirmDialog();
		contactDialog.setHeader("Cadastro de Contato");
		VerticalLayout layout = new VerticalLayout(cbxContactType, txtValue);
		layout.setAlignItems(FlexComponent.Alignment.STRETCH);
		contactDialog.add(layout);
		contactDialog.setCancelable(true);
		contactDialog.addCancelListener(e -> contactDialog.close());
		contactDialog.setCancelText("Cancelar");
		createContactBinder(contact);
		System.out.println("user" + contact.getValue());
		contactDialog.setConfirmText("Salvar");
		contactDialog.addConfirmListener(e -> {
			adicionarContato(contact);
		});
		contactDialog.open();
		gridContact.getDataProvider().refreshAll();
	}

	private void createContactBinder(Contact contact) {

		binderContact = new BeanValidationBinder<>(Contact.class);
		binderContact.forField(cbxContactType).bind("type");
		binderContact.forField(txtValue).bind("value");
		contact.setUser(getCurrentBean());
		contactBean = contact;

		binderContact.readBean(contactBean);
	}

	private void adicionarContato(Contact contact) {
		try {

			binderContact.writeBean(contactBean);
			
			getCurrentBean().getContacts().add(contactBean);
			
			gridContact.setItems(getCurrentBean().getContacts());
		} catch (ValidationException e) {
			throw new RuntimeException(e);
		}

		cbxContactType.clear();
		txtValue.clear();
		contactBean = contact;
		gridContact.getDataProvider().refreshAll();
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		try {

			if (parameter != null) {
				User user = controller.load(parameter);
				if (user != null) {
					setBean(user);
					if(getCurrentBean().getUsername() != null) {
						txtUsername.setEnabled(false);
						passPassword.setEnabled(false);
						if(getCurrentBean().getRole().toString() == Role.USER.toString()) {
							cbxRole.setEnabled(false);
						}
					}
					gridContact.setItems(user.getContacts());
				}
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