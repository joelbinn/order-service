package com.polarbookshop.orderservice.service.book;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

class BookClientTest {
  MockWebServer mockWebServer;
  BookClient bookClient;

  @BeforeEach
  void setUp() throws IOException {
    this.mockWebServer = new MockWebServer();
    this.mockWebServer.start();
    var webClient = WebClient.builder().baseUrl(
        mockWebServer.url("/")
          .uri()
          .toString())
      .build();
    this.bookClient = new BookClient(webClient);
  }

  @AfterEach
  void tearDown() throws IOException {
    this.mockWebServer.shutdown();
  }

  @Test
  void whenBookExistsReturnBook() {
    // GIVEN
    var bookIsbn = "1234567890";
    var mockResponse = new MockResponse()
      .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .setBody("""
        {
          "isbn": "%s",
          "title": "Title",
          "author": "Author",
          "price": 9.90,
          "publisher": "Publisher"
        }
        """.formatted(bookIsbn));
    mockWebServer.enqueue(mockResponse);

    // WHEN
    Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

    // THEN
    StepVerifier.create(book)
      .expectNextMatches(b -> b.isbn().equals(bookIsbn))
      .verifyComplete();
  }

  @Test
  void whenBookDoesNotExistsFailDirectlyWithException() {
    // GIVEN
    var bookIsbn = "1234567890";
    var mockResponse = new MockResponse()
      .setResponseCode(404);
    mockWebServer.enqueue(mockResponse);

    // WHEN
    Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

    // THEN
    StepVerifier.create(book)
      .verifyComplete();
  }

  @Test
  void whenServiceUnavailableRetry() {
    // GIVEN
    var bookIsbn = "1234567890";
    var mockUnavailableResponse = new MockResponse()
      .setResponseCode(503);
    mockWebServer.enqueue(mockUnavailableResponse);
    var mockResponse = new MockResponse()
      .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .setBody("""
        {
          "isbn": "%s",
          "title": "Title",
          "author": "Author",
          "price": 9.90,
          "publisher": "Publisher"
        }
        """.formatted(bookIsbn));
    mockWebServer.enqueue(mockResponse);


    // WHEN
    Mono<Book> book = bookClient.getBookByIsbn(bookIsbn);

    // THEN
    StepVerifier.create(book)
      .expectNextMatches(b -> b.isbn().equals(bookIsbn))
      .verifyComplete();
  }
}
