package br.com.ltsoftwaresupport.analyticalflow.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;
    
    @NotEmpty
    private String lastname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
	private Set<Contact> contacts = new HashSet<>() ;

    public User() {
    }

    public User(String username, String password, String name, String lastname, Role role, Set<Contact> contacts) {
        this.username = username;
        setPassword(password);
        this.name = name;
        this.lastname = lastname;
        this.role = role;
        setContacts(contacts);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        if(contacts != null) {
            contacts.forEach(e -> e.setUser(this));
        }
        this.contacts = contacts;
    }
    
    public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Override
    public String toString() {
        return name + " " + lastname;
    }
}