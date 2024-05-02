package br.com.ltsoftwaresupport.analyticalflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ltsoftwaresupport.analyticalflow.model.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	boolean existsByName(String name);
}
