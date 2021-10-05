package com.santander.games.challenges.quarkus.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@Entity
public class Book extends PanacheEntity {

  @Column(columnDefinition = "TEXT")
  public String name;

  // Min for testing purpose
  @Min(1900)
  public Integer publicationYear;

  public static Book findByName(String name) {
    return find("name", name).firstResult();
  }

  public static Books findByPublicationYearBetween(int lowerYear, int higherYear) {
    List<Book> bookList = list("publicationYear BETWEEN ?1 AND ?2", lowerYear, higherYear);
    return new Books(bookList, bookList.size());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Book book = (Book) o;
    return Objects.equals(name, book.name) && Objects.equals(publicationYear, book.publicationYear);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, publicationYear);
  }

  @Override
  public String toString() {
    return "Book{" +
            "name='" + name + '\'' +
            ", publicationYear=" + publicationYear +
            ", id=" + id +
            '}';
  }
}
