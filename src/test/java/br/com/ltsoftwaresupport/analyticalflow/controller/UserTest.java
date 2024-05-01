package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.builder.UserBuilder;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {

    @Autowired
    UserController userController;
    
    private User savedUser;
    
    @BeforeEach
    void setup() throws DefaultException {
    	User user = createUser();
    	savedUser = userController.save(user);
    }
    
    @AfterEach
    void cleanup() throws DefaultException {
    	if (savedUser != null) {
    		userController.delete(savedUser);
    	}
    }
    
    private User createUser() throws DefaultException {
    	return UserBuilder.build().now();
    }

    @Test
    @Order(1)
    void save() throws DefaultException {
        assertNotNull(savedUser);
    }

    @Test
    @Order(2)
    void update() throws DefaultException {
        assertNotNull(savedUser);
        savedUser.setLastname("updated");
        User updatedUser = userController.update(savedUser);
        assertEquals("updated", updatedUser.getLastname());
    }

    @Test
    @Order(3)
    void list()  {
        assertNotNull(userController.list());
    }

    @Test
    @Order(4)
    void delete() throws DefaultException {
    	assertNotNull(savedUser);
    	userController.delete(savedUser);
    	assertThrows(DefaultException.class, () -> userController.load(savedUser.getUsername()));
    }

}