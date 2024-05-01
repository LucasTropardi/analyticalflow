package br.com.ltsoftwaresupport.analyticalflow.model;

public enum Role {
    ADMIN("Administrador"),
    USER("Usuário");

    private String role;
    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return this.getRole();
    }
}
