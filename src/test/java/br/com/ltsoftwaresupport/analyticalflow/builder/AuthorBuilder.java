package br.com.ltsoftwaresupport.analyticalflow.builder;

import br.com.ltsoftwaresupport.analyticalflow.model.Author;

public class AuthorBuilder {

	private Author author;
	
	public static AuthorBuilder build() {
		AuthorBuilder authorBuilder = new AuthorBuilder();
		authorBuilder.author = new Author();
		authorBuilder.author.setName("Autor Mentado");
		return authorBuilder;
	}
	
	public AuthorBuilder addName(String name) {
		author.setName(name);
		return this;
	}
	
	public Author now() {
		return author;
	}
}
