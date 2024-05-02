package br.com.ltsoftwaresupport.analyticalflow.views.cadastros.publisher;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.ltsoftwaresupport.analyticalflow.controller.PublisherController;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;
import br.com.ltsoftwaresupport.analyticalflow.views.layout.MainLayout;
import br.com.ltsoftwaresupport.analyticalflow.views.utils.GenericGrid;

@Route(value = "logado/publishers", layout = MainLayout.class)
@PageTitle("Editora")
@RolesAllowed({"ADMIN"})
public class PublisherGrid  extends GenericGrid<Publisher, Long, PublisherController>{

	
	public PublisherGrid(@Autowired PublisherController publisherController) throws DefaultException {
		super(publisherController, Publisher.class, Publisher::getId);
		setTitle("Publishers");
		setRotaForm("publisher");
		configurarGrid();
  
    }

	private void configurarGrid() {
        getGrid().addColumn(Publisher::getId).setHeader("Id");
        getGrid().addColumn(Publisher::getName).setHeader("Publisher");
        getGrid().addColumn(Publisher::getWebsite).setHeader("Site");        
    }

    
}