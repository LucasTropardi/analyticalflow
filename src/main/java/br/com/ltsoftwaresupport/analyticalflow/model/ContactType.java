package br.com.ltsoftwaresupport.analyticalflow.model;

public enum ContactType {
    EMAIL("E-mail"),
    PHONE("Telefone"),
    SOCIAL_MEDIA("Rede social");

    private String contactType;

    ContactType() {
    }

    public String getContactType() {
        return this.contactType;
    }
    ContactType (String contactType) {
        this.contactType = contactType;
    }

    @Override
    public String toString() {
        return this.getContactType();
    }
}
