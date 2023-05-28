package com.example;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.List;

import static java.lang.System.out;

@Table(name = "persons")
@Entity(name = "PersonEntity")
public class Person extends PanacheEntity {

//    @Inject
//    @Transient
//    private EntityManagerFactory emf;
//
//    public static EntityManagerFactory staticEmf;

//    @Inject
//    @Transient
//    private Mutiny.SessionFactory factory;
//
//    public static Mutiny.SessionFactory staticFactory;
//
//    @Inject
//    void setStaticFactory(Mutiny.SessionFactory factory) {
//        Person.staticFactory = factory;
//    }

//    @Inject
//    private void setStaticEntityManagerFactory(EntityManagerFactory emf) {
//        Person.staticEmf = emf;
//    }

//    public static EntityManagerFactory emf = Persistence.createEntityManagerFactory("PersonEntity");
//
//    public static Mutiny.SessionFactory factory = emf.unwrap(Mutiny.SessionFactory.class);

    public String name;

    public int age;

    public static Uni<List<Person>> getPersonsByNameAndAge(String name, int age, Mutiny.SessionFactory factory) {
        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<Person> query = builder.createQuery(Person.class);
        Root<Person> personTable = query.from(Person.class);
        query.where(builder.like(builder.upper(personTable.get("name")), '%'+name.toUpperCase()+'%'), builder.equal(personTable.get("age"), age));
        query.select(personTable);

//        return getSession().createQuery(query).getResultList().invoke(
//                books -> books.forEach( book -> out.println(book.title) )
//        );
        Uni<Mutiny.Session> sessionUni = factory.openSession();
        return sessionUni.chain(
                session -> session.createQuery(query).getResultList().invoke(
                                persons -> persons.forEach(person -> out.println(person.toString()))
                        )
                        .eventually(session::close));
    }

    @Override
    public String toString() {
        return "name: " + name + " - age: " + age;
    }
}
