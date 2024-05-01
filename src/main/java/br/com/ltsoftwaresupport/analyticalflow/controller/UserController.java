package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.User;
import br.com.ltsoftwaresupport.analyticalflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserController extends GenericController<User, String, UserRepository> {

    public UserController(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public List<User> list() {
        return this.repository.findAll();
    }

    public User load(String username) throws DefaultException {

        return this.repository.findById(username)
                .orElseThrow(() -> new DefaultException("Usuário não encontrado"));

    }

    @Override
    protected void validate(User entity, Mode mode) throws DefaultException {
        super.validate(entity, mode);

        switch (mode) {
            case SAVE:
                System.out.println("save");
//                if(repository.existsByUsername(entity.getUsername())) throw new DefaultException("Usuário já cadastrado");
                break;
            case UPDATE:
                System.out.println("update");
                if(!repository.existsById(entity.getUsername())) throw new DefaultException("Não existe");
                break;
            case DELETE:
                System.out.println("delete");
//                if(!repository.existsById(entity.getUsername())) throw new DefaultException("Não existe");
                break;
        }
    }

}