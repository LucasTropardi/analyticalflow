package br.com.ltsoftwaresupport.analyticalflow.views.layout;

import java.io.Serial;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import br.com.ltsoftwaresupport.constants.Constants;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.security.SecurityService;
import br.com.ltsoftwaresupport.analyticalflow.views.cadastros.game.GameGrid;
import br.com.ltsoftwaresupport.analyticalflow.views.cadastros.publisher.PublisherGrid;
import br.com.ltsoftwaresupport.analyticalflow.views.cadastros.review.ReviewGrid;
import br.com.ltsoftwaresupport.analyticalflow.views.cadastros.user.UserGrid;
import br.com.ltsoftwaresupport.analyticalflow.views.principal.PrincipalAuthGameView;

@CssImport("./styles/layout.css")
public class MainLayout extends AppLayout {

    @Serial
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private SecurityService securityService;
	
	boolean isAdmin;

    User user;
    
    public MainLayout(@Autowired SecurityService securityService) {

        isAdmin = securityService.isUserAdmin();
        
        user = securityService.getCurrentUserOrNull();
        
        Div navbarContainer = new Div();
        navbarContainer.addClassName("navbar");
        navbarContainer.getStyle()
	        .set("display", "flex")
	        .set("align-items", "center")
	        .set("justify-content", "space-between")
	        .set("width", "100%");

        Image logo = new Image(Constants.NAV_LOGO_URL, "analytical flow logo");
        logo.getStyle().set("height", "44px")
                      .set("width", "150px")
                      .set("margin", "0")
                      .set("position", "absolute")
                      .set("left", "var(--lumo-space-l)");
        logo.addClassName("logo-nav");

        Tabs tabs = getTabs();

        Image btnLogoutImage = new Image(Constants.BTN_LOGOUT_URL, "Sair");
        btnLogoutImage.getStyle()
            .set("cursor", "pointer") 
            .set("margin", "2px")
            .set("padding-right", "2%")
            .set("width", "27px");
        btnLogoutImage.getElement().setAttribute("title", "Sair");
        btnLogoutImage.addClickListener(event -> {
        	confirmLogout();
        });   
        
        Image btnUserEdit = new Image(Constants.BTN_USER_EDIT_URL, "Meus dados");
        btnUserEdit.getStyle()
            .set("cursor", "pointer") 
            .set("margin", "2px")
            .set("padding-right", "3px")
            .set("width", "34px");
        btnUserEdit.getElement().setAttribute("title", "Meus dados");
        btnUserEdit.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("user/" + user.getUsername())));

        navbarContainer.add(logo, tabs, btnUserEdit, btnLogoutImage);
        addToNavbar(navbarContainer);
    }

    private void confirmLogout() {
        Dialog dialog = new Dialog();
        
        Image caution = new Image(Constants.CAUTION_IMG_URL, "Cuidado");
        caution.getStyle()
	        .set("width", "90px");
        HorizontalLayout cautionImg = new HorizontalLayout(caution);
        cautionImg.getStyle().set("justify-content", "center")
        	.set("margin", "0");
        dialog.add(cautionImg);
        
        H3 message = new H3("Deseja realmente sair?");
        dialog.add(message);

        Button confirmButton = new Button("Sair", confirmEvent -> {
            securityService.logout();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Cancelar", cancelEvent -> dialog.close());
        cancelButton.addClassName("cancel-button");
                        
        HorizontalLayout buttonsLayout = new HorizontalLayout(cancelButton, confirmButton);
        buttonsLayout.getStyle().set("justify-content", "center"); 
        buttonsLayout.getStyle().set("margin-top", "15%");
        dialog.add(buttonsLayout);

        dialog.open();
    }


	private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(createTab("Games", PrincipalAuthGameView.class),
                createTab("Reviews", ReviewGrid.class));
    
        if (isAdmin) {
            tabs.add(createTab("Games", GameGrid.class),
                    createTab("Editoras", PublisherGrid.class),
                    createTab("Usuários", UserGrid.class));
        }
    
        return tabs;
    }

    private Tab createTab(String viewName, Class viewClass) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        // Demo has no routes
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }
}