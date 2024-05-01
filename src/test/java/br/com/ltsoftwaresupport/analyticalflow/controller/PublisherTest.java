package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.builder.PublisherBuilder;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.exception.NotFoundException;
import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherTest {

    @Autowired
    PublisherController publisherController;


    @Test
    @Order(1)
    void save() throws DefaultException {
        Publisher publisher = PublisherBuilder.build().addWebsite("http://www.capcom.com").addName("Capcoma").now();
        assertNotNull(publisherController.save(publisher));
        publisherController.delete(publisher);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisher.getId());
        });
    }

    @Test
    @Order(2)
    void list() {
        List<Publisher> publishers = publisherController.list();

        publishers.forEach(
                e -> System.out.println(e.toString())
        );

        assertNotNull(publishers);
    }

    @Test
    @Order(3)
    void load() throws NotFoundException, DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addWebsite("website21")
                .addName("name21")
                .now();
        Publisher savedPublisher = publisherController.save(publisher);

        Publisher loadedPublisher = publisherController.load(savedPublisher.getId());
        assertNotNull(loadedPublisher);
        publisherController.delete(savedPublisher);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(savedPublisher.getId());
        });
    }


    @Test
    @Order(4)
    void update() throws DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addName("before updates")
                .addWebsite("http://www.beforeupdate.com")
                .now();
        Publisher publisherAfterSave = publisherController.save(publisher);

        Publisher updatedPublisher = PublisherBuilder.build()
                .addName("afterupdateds")
                .addWebsite("http://www.afterupdated.com")
                .now();
        updatedPublisher.setId(publisherAfterSave.getId());

        Publisher afterUpdate = publisherController.update(updatedPublisher);

        assertEquals("afterupdateds", afterUpdate.getName());
        assertEquals("http://www.afterupdated.com", afterUpdate.getWebsite());
        publisherController.delete(publisherAfterSave);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisherAfterSave.getId());
        });
    }

    @Test
    @Order(5)
    void delete() throws DefaultException {
        Publisher publisher = PublisherBuilder.build()
                .addName("xurumov")
                .addWebsite("http://www.xurumov.com")
                .now();
        Publisher publisherSaved = publisherController.save(publisher);

        publisherController.delete(publisherSaved);
        assertThrows(DefaultException.class, () -> {
            publisherController.load(publisherSaved.getId());
        });
    }

    @AfterEach
    void cleanUp() {
    }
}