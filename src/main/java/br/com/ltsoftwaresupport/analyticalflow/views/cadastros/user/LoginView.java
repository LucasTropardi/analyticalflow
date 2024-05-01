package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.user;

import java.io.Serial;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import br.com.ltsoftwaresupport.analyticalflow.views.layout.PrincipalLayout;
import br.com.ltsoftwaresupport.constants.Constants;

@Route(value = "/login", layout = PrincipalLayout.class)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	LoginForm login;
	
	VerticalLayout form;
	
	public LoginView() {
		
		setPadding(true);
		setSizeFull();
		setJustifyContentMode(JustifyContentMode.START);
		setAlignItems(Alignment.CENTER);
		
		LoginI18n i18n = LoginI18n.createDefault();
		
		LoginI18n.Form formLogin = i18n.getForm();
		formLogin.setTitle("");
		formLogin.setUsername("Usuário");
		formLogin.setPassword("Senha");
		formLogin.setSubmit("ENTRAR");
		formLogin.setForgotPassword("Não possui cadastro?");
		i18n.setForm(formLogin);
		
		
		LoginI18n.ErrorMessage errorMessage = i18n.getErrorMessage();
		errorMessage.setTitle("Atenção");
		errorMessage.setMessage("Erro no processo de autenticação!");
		i18n.setErrorMessage(errorMessage);		
		
		login = new LoginForm();
		login.getStyle().set("background-color", "var(--lumo-contrast-5pct)")
        .set("display", "flex").set("justify-content", "center")
        .set("padding", "0")
		.set("border", "3px solid #A9A9A9")
		.set("box-shadow", "5px 10px grey")
		.set("border-radius", "6px");
		login.setI18n(i18n);
		
		login.setAction("login");
		
		login.addForgotPasswordListener(event -> {
            getUI().ifPresent(ui -> ui.navigate("/cadastrar"));
        });
		
		Image logar = new Image(Constants.LOGAR_IMAGE_URL, "logar logo");
        logar.getStyle().set("height", "auto").set("width", "300px");
		add(logar, login);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
			login.setError(true);
		}
		
	}

}