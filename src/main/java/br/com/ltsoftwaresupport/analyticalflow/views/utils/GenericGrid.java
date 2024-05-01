package br.com.ltsoftwaresupport.analyticalflow.views.utils;

import br.com.ltsoftwaresupport.analyticalflow.controller.GenericController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;
import java.util.function.Function;

public abstract class GenericGrid<T, ID, C extends GenericController<T, ID, ?>> extends VerticalLayout {

    protected Function<T, ID> idProvider;
    protected C controller;
    private Grid<T> grid;
    private String formRoute;
    private H2 h2Title;
    private Button btnNovo;

    public GenericGrid(C controller, Class<T> entityClass, Function<T, ID> idProvider) throws DefaultException {
        this.controller = controller;
        this.idProvider = idProvider;

        setPadding(true);

        grid = new Grid<>(entityClass, false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);

        refreshList();

        grid.addItemClickListener(event -> {
            T item = event.getItem();
            ID itemId = idProvider.apply(item);
            getUI().ifPresent(ui -> ui.navigate(formRoute +"/"+ itemId));
        });

        h2Title = new H2("");
        h2Title.getStyle().set("margin", "0");

        btnNovo = new Button("Novo", VaadinIcon.PLUS.create());
        btnNovo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNovo.addClickListener(e -> adicionarNovo());

        add(configurarCabecalho(), grid);
    }

    private HorizontalLayout configurarCabecalho() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.add(h2Title, btnNovo);

        return horizontalLayout;
    }

    protected void setTitle(String title) {
        this.h2Title.setText(title);
    }

    protected void setRotaForm(String route) {
        this.formRoute = route;
    }

    protected void refreshList() throws DefaultException {
        grid.setItems(controller.list());
    }

    protected void refreshList(List<T> items) throws DefaultException {
        grid.setItems(items);
    }

    protected Grid<T> getGrid() {
        return grid;
    }

    private void adicionarNovo() {
        getUI().ifPresent(ui -> ui.navigate(formRoute));
    }
}