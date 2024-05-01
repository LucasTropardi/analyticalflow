package br.com.ltsoftwaresupport.analyticalflow.views.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import br.com.ltsoftwaresupport.constants.Constants;
import br.com.ltsoftwaresupport.analyticalflow.views.principal.PrincipalGameView;

@CssImport("./styles/layout.css")
public class PrincipalLayout extends AppLayout {

    public PrincipalLayout() {
    	
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
        
        Image btnLoginImage = new Image(Constants.BTN_LOGIN_URL, "Entrar");
        btnLoginImage.getStyle()
            .set("cursor", "pointer") 
            .set("margin", "2px")
            .set("width", "28px");
        btnLoginImage.getElement().setAttribute("title", "Entrar");
        btnLoginImage.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("login")));

        Image btnSigninImage = new Image(Constants.BTN_SIGNIN_URL, "Cadastre-se");
        btnSigninImage.getStyle()
            .set("cursor", "pointer") 
            .set("margin-left", "6px")
            .set("padding-right", "2%")
            .set("width", "30px");
        btnSigninImage.getElement().setAttribute("title", "Cadastre-se");
        btnSigninImage.addClickListener(event -> getUI().ifPresent(ui -> ui.navigate("cadastrar")));

        navbarContainer.add(logo, tabs, btnLoginImage, btnSigninImage);
        addToNavbar(navbarContainer);
    }
    
    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(createTab("Games", PrincipalGameView.class));
        return tabs;
    }

    private Tab createTab(String viewName, Class viewClass) {
        RouterLink link = new RouterLink();
        link.add(viewName);
        link.setRoute(viewClass);
        link.setTabIndex(-1);
    
        Tab tab = new Tab(link);
        if("Todos".equals(viewName)) {
            tab.addClassName("tab-todos");
        } else {
            tab.addClassName("tab-outros");
        }
    
        return tab;
    }
}