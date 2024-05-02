package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.user;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.ltsoftwaresupport.analyticalflow.controller.UserController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericGrid;

@Route(value = "logado/users", layout = MainLayout.class)
@PageTitle("Users | GameRating")
@RolesAllowed("ADMIN")
public class UserGrid extends GenericGrid<User, String, UserController>{	
	
	public UserGrid(@Autowired UserController userController) throws DefaultException {
		super(userController, User.class, User::getUsername);
		setTitle("Users");
		setRotaForm("logado/user");
		configurarGrid();
    }

	private void configurarGrid() { 
        getGrid().addColumn(user -> user.getUsername() != null ? user.getUsername(): "").setHeader("Usuário");
        getGrid().addColumn(user -> user.getName() != null ? user.getName(): "").setHeader("Nome");  
        getGrid().addColumn(user -> user.getRole() != null ? user.getRole().getRole(): "").setHeader("Acesso"); 
    }
}