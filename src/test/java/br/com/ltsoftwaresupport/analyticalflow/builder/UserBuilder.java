package br.com.ltsoftwaresupport.analyticalflow.builder;

import br.com.ltsoftwaresupport.analyticalflow.model.Contact;
import br.com.ltsoftwaresupport.analyticalflow.model.Role;
import br.com.ltsoftwaresupport.analyticalflow.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserBuilder {

    private User user;

    public static UserBuilder build() {
        UserBuilder userBuilder = new UserBuilder();
        userBuilder.user = new User();
        userBuilder.user.setUsername("manuelson");
        userBuilder.user.setName("Manuelson");
        userBuilder.user.setLastname("Josueldo");
        userBuilder.user.setPassword("123456");
        userBuilder.user.setRole(Role.USER);
        userBuilder.user.setContacts(new HashSet<>());
        return userBuilder;
    }

    public UserBuilder addUsername(String username) {
        user.setUsername(username);
        return this;
    }

    public UserBuilder addPassword(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder addName(String name) {
        user.setName(name);
        return this;
    }
    
    public UserBuilder addLastname(String lastname) {
    	user.setLastname(lastname);
    	return this;
    }

    public UserBuilder addRole(Role role) {
        user.setRole(role);
        return this;
    }

    public UserBuilder addContacts(Set<Contact> contacts) {
        user.setContacts(contacts);
        return this;
    }

    public User now() {
        return user;
    }
}
