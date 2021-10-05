package com.santander.games.challenges.quarkus.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RegisterForReflection
public class Books {

  private long totalCount;
  private List<Book> bookList;

  public Books() {
  }

  public Books(List<Book> bookList, long totalCount) {
    this.bookList = Collections.unmodifiableList(bookList);
    this.totalCount = totalCount;
  }

  public List<Book> getBooksList() {
    return Collections.unmodifiableList(bookList);
  }

  public void setBooksList(List<Book> discountCodesList) {
    this.bookList = Collections.unmodifiableList(discountCodesList);
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Books books = (Books) o;
    return totalCount == books.totalCount && Objects.equals(bookList, books.bookList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalCount, bookList);
  }

  @Override
  public String toString() {
    return "Books{" +
            "totalCount=" + totalCount +
            ", bookList=" + bookList +
            '}';
  }
}
