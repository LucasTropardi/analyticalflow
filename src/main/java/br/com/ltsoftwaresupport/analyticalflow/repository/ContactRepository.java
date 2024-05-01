package br.com.ltsoftwaresupport.analyticalflow.repository;

import br.com.ltsoftwaresupport.analyticalflow.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
}
