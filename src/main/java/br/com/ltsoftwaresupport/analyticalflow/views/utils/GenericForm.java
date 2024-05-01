package br.com.ltsoftwaresupport.analyticalflow.views.utils;

import br.com.ltsoftwaresupport.analyticalflow.controller.GenericController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public abstract class GenericForm<T, ID, C extends GenericController<T, ID, ?>> extends VerticalLayout {

    private BeanValidationBinder<T> binder;
    protected C controller;
    private T currentBean;
    private FormLayout form;
    private String successRoute;
    private H2 title;
    Button btnNovo;
    Button btnVoltar;

    public GenericForm(Class<T> beanClass, C controller, T bean) {
        this.controller = controller;
        setPadding(true);

        title = new H2("");
        title.getStyle().set("margin", "0");

        btnNovo = new Button("Salvar");
        btnNovo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnNovo.addClickListener(e -> salvar());

        btnVoltar = new Button("Voltar");
        btnVoltar.getStyle().set("margin-left", "auto");
        btnVoltar.addClickListener( e-> voltar());

        form = new FormLayout();
        form.addClassName("form-container");
        currentBean = bean;
        binder = new BeanValidationBinder<>(beanClass);

        configForm();

        add(definirCabecalho(), form);
    }

    private void configForm() {
        form.getStyle().set("width", "91.5%");
        form.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("320px", 12));
    }

    private HorizontalLayout definirCabecalho() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(Alignment.CENTER);

        horizontalLayout.add(title, btnVoltar, btnNovo);
        horizontalLayout.addClassName("head-form");
        return horizontalLayout;
    }

    protected void createBinder() {
        binder.readBean(currentBean);
        form.getChildren().forEach(
                e -> {
                    if (e instanceof HasValue<?, ?> hasValue && e.getId().isPresent()) {
                        binder.forField(hasValue).bind(e.getId().get());
                    }
                }
        );
    }


    private FormLayout getForm() {
        return form;
    }

    private void setForm(FormLayout form) {
        this.form = form;
    }

    protected void addInForm(Component component, Integer colSpan, String fieldName) {
        component.setId(fieldName);
        form.add(component, colSpan);
    }

    protected void setSuccessRoute(String successRoute) {
        this.successRoute = successRoute;
    }

    protected void setBean(T bean) {
        this.currentBean = bean;
    }

    protected T getCurrentBean() {
        return currentBean;
    }


    private void salvar() {

        try {
            binder.writeBean(currentBean);
            controller.save(currentBean);
            voltar();

        } catch (ValidationException | DefaultException e) {
            throw new RuntimeException(e);
        }

    }

    protected BeanValidationBinder<T> getBinder() {
        return this.binder;
    }

    protected void voltar() {
        getUI().ifPresent(ui -> ui.navigate(successRoute));
    }

    protected void setTitle(String title) {
        this.title.setText(title);
    }
}