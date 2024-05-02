package br.com.ltsoftwaresupport.analyticalflow.model;

import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Table(name = "author")
public class Author {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "name")
	@NotEmpty
	private String name;
	
	public Author() {
	}
	
	public Author(Long id, String authorName) {
		this.id = id;
		this.name = authorName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
        return name;
    }
}
