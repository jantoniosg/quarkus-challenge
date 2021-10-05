package com.santander.games.challenges.quarkus;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santander.games.challenges.quarkus.model.Book;
import com.santander.games.challenges.quarkus.model.Books;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookResourceTest {

  private static final String LIBRO_DEL_ATLETI = "Libro del Atleti";

  @Test
  void shouldPingOpenAPI() {
    given()
            .header(ACCEPT, APPLICATION_JSON)
            .when().get("/q/openapi")
            .then()
            .statusCode(OK.getStatusCode());
  }

  @Test
  void shouldPingSwaggerUI() {
    given()
            .when().get("/q/swagger-ui")
            .then()
            .statusCode(OK.getStatusCode());
  }

  @Test
  void shouldGetAllBooks() {
    given()
            .when().get("/books/all")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON);
  }

  @Test
  void shouldGetByName() {
    given()
            .when().get("/books/byName/Billy Summers")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON);
  }

  @Test
  void shouldNoContentGetByName() {
    given()
            .when().get("/books/byName/Billy")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
  }

  @Test
  void shouldGetBetweenYears() {
    given()
            .when().get("/books/byPublicationYearBetween/2015/2016")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON);
    Books books = get("/books/byPublicationYearBetween/2015/2016")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .extract().body().as(Books.class);
    assertEquals(4, books.getTotalCount());
  }

  @Test
  void shouldNoContentBetweenYears() {
    given()
            .when().get("/books/byPublicationYearBetween/1900/1910")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .extract().body().as(Books.class);
    Books books = get("/books/byPublicationYearBetween/1900/1910")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .extract().body().as(Books.class);
    assertEquals(0, books.getTotalCount());

  }

  @Test
  void create() {
    Book newItem = new Book();
    newItem.name = LIBRO_DEL_ATLETI;
    newItem.publicationYear = 2020;

    given()
            .body(newItem)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .when().post("/books")
            .then()
            .statusCode(CREATED.getStatusCode());

    given().
            get("/books/byName/" + LIBRO_DEL_ATLETI)
            .then()
            .statusCode(OK.getStatusCode());
  }

  @Test
  void createError() {
    Book newItem = new Book();
    newItem.name = LIBRO_DEL_ATLETI;
    newItem.publicationYear = 1890;

    given()
            .body(newItem)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .when().post("/books")
            .then()
            .statusCode(INTERNAL_SERVER_ERROR.getStatusCode());
  }

  @Test
  void update() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Book item = get("/books/byName/Sapiens")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .extract().body().as(Book.class);
    item.name = LIBRO_DEL_ATLETI;

    given()
            .body(item)
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .when().put("/books/update/{id}", item.id)
            .then()
            .statusCode(OK.getStatusCode());

    Book itemUpdated = get("/books/byName/" + LIBRO_DEL_ATLETI)
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .extract().body().as(Book.class);
    assertEquals(LIBRO_DEL_ATLETI, itemUpdated.name);

    given().get("/books/byName/Sapiens")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

  }

  @Test
  void delete() {
    given()
            .when().get("/books/byName/Sleepwalkers")
            .then()
            .statusCode(OK.getStatusCode())
            .header(CONTENT_TYPE, APPLICATION_JSON);

    given()
            .when().delete("/books/delete/5")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());

    given()
            .when().get("/books/byName/Sleepwalkers")
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
  }

  @Test
  void deleteError() {
    given()
            .when().delete("/books/delete/100")
            .then()
            .statusCode(NOT_FOUND.getStatusCode());

  }

}