package br.com.ltsoftwaresupport.analyticalflow.controller;

import org.springframework.stereotype.Service;

import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Author;
import br.com.ltsoftwaresupport.analyticalflow.repository.AuthorRepository;

@Service
public class AuthorController extends GenericController<Author, Long, AuthorRepository> {
	public AuthorController(AuthorRepository repository) {
		this.repository = repository;
	}
	
	@Override
	protected void validate(Author entity, Mode mode) throws DefaultException {
		super.validate(entity, mode);
		
		switch (mode) {
        case SAVE:
            System.out.println("save");
//            if(repository.existsByName(entity.getName())) throw new DefaultException("Author já cadastrado");
            break;
        case UPDATE:
            System.out.println("update");
            if(!repository.existsById(entity.getId())) throw new DefaultException("Não existe");
            break;
        case DELETE:
            System.out.println("delete");
//            if(!repository.existsById(entity.getId())) throw new DefaultException("Não existe");
            break;
		}
	}
}
