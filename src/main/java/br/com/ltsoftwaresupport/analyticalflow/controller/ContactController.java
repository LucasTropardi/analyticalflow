package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Contact;
import br.com.ltsoftwaresupport.analyticalflow.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactController extends GenericController<Contact, Long, ContactRepository> {

    public ContactController(ContactRepository contactRepository) {
        this.repository = contactRepository;
    }

    public List<Contact> list() {
        return this.repository.findAll();
    }

    public Contact load(Long id) throws DefaultException {

        return this.repository.findById(id)
                .orElseThrow(() -> new DefaultException("Item não encontrado"));

    }
    @Override
    protected void validate(Contact entity, Mode mode) throws DefaultException {
        super.validate(entity, mode);

        switch (mode) {
            case SAVE:
                System.out.println("save");
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

