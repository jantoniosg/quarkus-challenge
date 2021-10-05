package com.santander.games.challenges.quarkus.service;

import com.santander.games.challenges.quarkus.model.Book;
import com.santander.games.challenges.quarkus.model.Books;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

@ApplicationScoped
public class BookService {

  @Transactional
  public void create(@NotNull @Valid Book book) {
    book.persist();
  }

  public List<Book> all() {
    return Book.listAll();
  }

  @Transactional
  public Book update(Long id, @NotNull @Valid Book book) {
    Book entity = Book.findById(id);
    if (entity == null) {
      throw new NotFoundException();
    }

    // map all fields from the book parameter to the existing entity
    entity.name = book.name;

    return entity;
  }

  public Book findByName(String name) {
    return Book.findByName(name);
  }

  public Books search(int lowerYear, int higherYear) {
    return Book.findByPublicationYearBetween(lowerYear, higherYear);
  }

  @Transactional
  public void delete(Long id) {
    Book entity = Book.findById(id);
    if (entity == null) {
      throw new WebApplicationException("Book with id of " + id + " does not exist.", 404);
    }
    entity.delete();
  }
}
