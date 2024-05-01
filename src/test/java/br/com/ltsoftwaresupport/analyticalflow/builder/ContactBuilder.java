package br.com.ltsoftwaresupport.analyticalflow.builder;

import br.com.ltsoftwaresupport.analyticalflow.model.Contact;
import br.com.ltsoftwaresupport.analyticalflow.model.ContactType;

public class ContactBuilder {

    private Contact contact;

    public static ContactBuilder build() {
        ContactBuilder contactBuilder = new ContactBuilder();
        contactBuilder.contact = new Contact();
        contactBuilder.contact.setType(ContactType.PHONE);
        contactBuilder.contact.setValue("18987654321");
        return contactBuilder;
    }

    public ContactBuilder addContactType(ContactType type) {
        contact.setType(type);
        return this;
    }

    public ContactBuilder addValue(String value) {
        contact.setValue(value);
        return this;
    }

    public Contact now() {
        return contact;
    }
}