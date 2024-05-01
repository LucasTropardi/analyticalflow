package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;
import br.com.ltsoftwaresupport.analyticalflow.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PublisherController extends GenericController<Publisher, Long, PublisherRepository> {


    public PublisherController(PublisherRepository publisherRepository) {
        this.repository = publisherRepository;
    }

    public List<Publisher> list() {
        return this.repository.findAll();
    }

    public Publisher load(Long id) throws DefaultException {

        return this.repository.findById(id)
                .orElseThrow(() -> new DefaultException("Item não encontrado"));

    }
    @Override
    protected void validate(Publisher entity, Mode mode) throws DefaultException {
        super.validate(entity, mode);

        switch (mode) {
            case SAVE:
                System.out.println("save");
                if(repository.existsByName(entity.getName())) throw new DefaultException("Publisher já existe");
                break;
            case UPDATE:
                System.out.println("update");
                if(!repository.existsById(entity.getId())) throw new DefaultException("Não existe");
                break;
            case DELETE:
                System.out.println("delete");
                if(!repository.existsById(entity.getId())) throw new DefaultException("Não existe");
                break;
        }
    }
}