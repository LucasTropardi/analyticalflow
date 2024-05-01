package br.com.ltsoftwaresupport.analyticalflow.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("testando")
public class MainView extends VerticalLayout {
    public MainView() {
        TextField textField = new TextField("Seu nome");
        Button button = new Button("Diga olá",
                e -> add(new com.vaadin.flow.component.html.Label("Olá " + textField.getValue())));

        add(textField, button);
    }
}
