package com.santander.games.challenges.quarkus.resource;

import com.santander.games.challenges.quarkus.model.Book;
import com.santander.games.challenges.quarkus.model.Books;
import com.santander.games.challenges.quarkus.service.BookService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

  @Inject
  protected BookService bookService;

  @GET
  @Path("/all")
  public List<Book> list() {
    return bookService.all();
  }

  @POST
  public Response create(Book book) {
    bookService.create(book);
    return Response.created(URI.create("/books/" + book.id)).build();
  }

  @PUT
  @Path("/update/{id}")
  public Book update(@PathParam("id") Long id, Book book) {
    return bookService.update(id, book);
  }

  @GET
  @Path("/byName/{name}")
  public Book search(@PathParam("name") String name) {
    return bookService.findByName(name);
  }

  @GET
  @Path("/byPublicationYearBetween/{lowerYear}/{higherYear}")
  public Books search(@PathParam("lowerYear") int lowerYear, @PathParam("higherYear") int higherYear) {
    return bookService.search(lowerYear, higherYear);
  }

  @DELETE
  @Path("/delete/{id}")
  public Response delete(@PathParam("id") Long id) {
    bookService.delete(id);
    return Response.noContent().build();
  }
}
