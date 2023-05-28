package com.example;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

@Path("/hello")
public class ExampleResource {

    @Inject
    Mutiny.SessionFactory factory;

//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//        return "Hello from RESTEasy Reactive";
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Person>> getPersons(@QueryParam("name") String name, @QueryParam("age") int age) {
        return Person.getPersonsByNameAndAge(name, age, factory);
    }

}
