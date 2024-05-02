package br.com.ltsoftwaresupport.analyticalflow.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.ltsoftwaresupport.analyticalflow.builder.AuthorBuilder;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.Author;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthorTest {
	@Autowired
	AuthorController authorController;
	
	private Author savedAuthor;
	
	@BeforeEach
	void setup() throws DefaultException {
		Author author = createAuthor();
		savedAuthor = authorController.save(author);
	}
	
	@AfterEach
	void cleanup() throws DefaultException {
		if (savedAuthor != null) {
			authorController.delete(savedAuthor);
		}
	}
	
	private Author createAuthor() throws DefaultException {
		return AuthorBuilder.build().now();
	}
	
	@Test
    @Order(1)
    void save() throws DefaultException {
        assertNotNull(savedAuthor);
    }
	
	@Test
    @Order(2)
	void list() throws DefaultException {
		assertNotNull(authorController.list());
	}
	
	@Test
    @Order(3)
	void load() throws DefaultException {
		assertNotNull(savedAuthor);
	}
	
	@Test
	@Order(4)
	void update() throws DefaultException {
		assertNotNull(savedAuthor);
		savedAuthor.setName("Josefino");
		Author updatedAuthor = authorController.update(savedAuthor);
		assertEquals("Josefino", updatedAuthor.getName());
	}
	
	@Test
	@Order(5)
	void delete() throws DefaultException {
		assertNotNull(savedAuthor);
		authorController.delete(savedAuthor);
		assertThrows(DefaultException.class, () -> authorController.load(savedAuthor.getId()));
	}
}
