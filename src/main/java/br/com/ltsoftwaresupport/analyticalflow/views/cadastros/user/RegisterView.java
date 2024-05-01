package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.user;

import java.io.Serial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import br.com.ltsoftwaresupport.analyticalflow.controller.UserController;
import br.com.ltsoftwaresupport.analyticalflow.model.Role;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.PrincipalLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.NotificationComponent;
import br.com.ltsoftwaresupport.constants.Constants;

@Route(value = "/cadastrar", layout = PrincipalLayout.class)
@PageTitle("Cadastre-se")
@AnonymousAllowed
public class RegisterView extends VerticalLayout{

	@Serial
    private static final long serialVersionUID = 1L;

    private final Binder<User> binder = new Binder<>(User.class);

    User currentBean;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public RegisterView(@Autowired UserController controller) {

        currentBean = new User();

        setAlignItems(Alignment.CENTER);
        Div registerContainer = new Div();
        registerContainer.setMaxWidth("300px");
        registerContainer.setClassName("register-container");

        TextField username = new TextField("Usuário");
        username.setWidthFull();

        PasswordField password = new PasswordField("Senha");
        password.setWidthFull();
        
        PasswordField confirm = new PasswordField("Confirmar senha");
        confirm.setWidthFull();

        Button registerButton = new Button("Cadastrar");
        registerButton.getStyle().set("margin-top", "20px");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.setWidthFull();

        TextField name = new TextField("Nome");
        name.setWidthFull();
        
        TextField lastname = new TextField("Sobrenome");
        lastname.setWidthFull();

        binder.readBean(currentBean);
        binder.forField(name).asRequired("Nome é obrigatório").bind("name");
        binder.forField(lastname).asRequired("Sobrenome é obrigatório").bind("lastname");
        binder.forField(username).asRequired("Username é obrigatório").bind("username");
        binder.forField(password).asRequired("Password é obrigatório").bind("password");

        currentBean.setRole(Role.USER);

        registerButton.addClickListener(event -> {

            NotificationComponent notification = null;
            if (!password.getValue().equals(confirm.getValue())) {
            	notification = new NotificationComponent("As senhas não correspondem, verifique.", "error",
                        "top_end");
            } else {
	            try {
	            		binder.writeBean(currentBean);
		                currentBean.setPassword(PASSWORD_ENCODER.encode(currentBean.getPassword()));
		                controller.save(currentBean);
		
		                notification = new NotificationComponent("Cadastro realizado com sucesso",
		                        "success", "top_end");
		                add(notification);
		
		                String rota = "/login";
		
		                getUI().ifPresent(ui -> ui.navigate(rota));
	            } catch (ValidationException e) {
	
	                notification = new NotificationComponent("Preencha todos os campos", "error",
	                        "top_end");
	
	                add(notification);
	                e.printStackTrace();
	            } catch (Exception e) {
	                notification = new NotificationComponent(e.getMessage(), "error", "top_end");
	
	                add(notification);
	                e.printStackTrace();
	            }            
            }
	    });
        
        Image joinLogo = new Image(Constants.REGISTER_IMAGE_URL, "Cadastre-se");
        joinLogo.getStyle().set("height", "auto").set("width", "400px");

        registerContainer.add(username, name, lastname, password, confirm, registerButton);

        add(joinLogo, registerContainer);

    }
}