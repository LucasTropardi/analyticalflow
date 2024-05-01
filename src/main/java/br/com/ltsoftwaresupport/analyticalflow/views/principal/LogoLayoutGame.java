package br.com.ltsoftwaresupport.analyticalflow.views.principal;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import br.com.ltsoftwaresupport.constants.Constants;

@CssImport("./styles/logolayout.css")
public class LogoLayoutGame extends HorizontalLayout {
    
    private Image image;

    public LogoLayoutGame() {
        image = new Image(Constants.LOGO_GAME_URL, "Minha Imagem");
        image.addClassName("logo-image");
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(image);
    }
}
