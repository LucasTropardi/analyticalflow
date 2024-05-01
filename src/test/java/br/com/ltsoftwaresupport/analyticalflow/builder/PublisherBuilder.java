package br.com.ltsoftwaresupport.analyticalflow.builder;

import br.com.ltsoftwaresupport.analyticalflow.model.Publisher;

public class PublisherBuilder {

    private Publisher publisher;

    public static PublisherBuilder build() {
        PublisherBuilder publisherBuilder = new PublisherBuilder();
        publisherBuilder.publisher = new Publisher();
        publisherBuilder.publisher.setWebsite("www.eagames.com");
        publisherBuilder.publisher.setName("ea games");
        return publisherBuilder;
    }

    public PublisherBuilder addWebsite(String website) {
        publisher.setWebsite(website);
        return this;
    }


    public PublisherBuilder addName(String website) {
        publisher.setName(website);
        return this;
    }

    public Publisher now() {
        return publisher;
    }
}