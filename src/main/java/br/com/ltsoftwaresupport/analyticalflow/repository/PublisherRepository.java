package br.com.ltsoftwaresupport.analyticalflow.repository;

import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    boolean existsByName(String name);
}
