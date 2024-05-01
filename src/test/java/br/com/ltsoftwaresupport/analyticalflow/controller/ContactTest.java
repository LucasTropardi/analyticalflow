package br.com.ltsoftwaresupport.analyticalflow.controller;

import br.com.ltsoftwaresupport.analyticalflow.builder.ContactBuilder;
import br.com.ltsoftwaresupport.analyticalflow.exception.DefaultException;
import br.com.ltsoftwaresupport.analyticalflow.model.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ContactTest {

    @Autowired
    ContactController contactController;

    @Autowired
    UserController userController;

    @Test
    @Order(1)
    void save() throws DefaultException {
        Contact contact = ContactBuilder.build()
                .addContactType(ContactType.PHONE)
                .addValue("18997239694")
                .now();

        assertNotNull(contactController.save(contact));
    }
    @Test
    @Order(2)
    void list() {
        List<Contact> contacts = contactController.list();
        contacts.forEach(
                e -> System.out.println(e.toString())
        );
        assertNotNull(contacts);
    }

    @Test
    @Order(3)
    void load() throws DefaultException {
        Contact contact = ContactBuilder.build()
                .addContactType(ContactType.PHONE)
                .addValue("18111222333")
                .now();

        assertNotNull(contactController.save(contact));
        try {
            assertNotNull(contactController.load(contactController.save(contact).getId()));
        } catch (DefaultException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(4)
    void update() throws DefaultException {
        Contact contact = ContactBuilder.build()
                .addContactType(ContactType.PHONE)
                .addValue("18111222333")
                .now();

        Contact contactAfterSave = contactController.save(contact);

        contactAfterSave.setType(ContactType.EMAIL);
        contactAfterSave.setValue("lucas@update.com");

        Contact contactAfterUpdate = contactController.save(contactAfterSave);

        assertEquals(ContactType.EMAIL, contactAfterUpdate.getType());
        assertEquals("lucas@update.com", contactAfterUpdate.getValue());
    }

    @Test
    @Order(5)
    void delete() throws DefaultException {
        Contact contact = ContactBuilder.build()
                .addContactType(ContactType.PHONE)
                .addValue("19252525252")
                .now();

        assertNotNull(contactController.save(contact));

        contactController.delete(contact);

        assertThrows(DefaultException.class, () -> {
            contactController.load(contact.getId());
        });
    }
}
