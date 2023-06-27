package com.polarbookshop.orderservice.service;

import com.polarbookshop.orderservice.domain.BookOrder;
import com.polarbookshop.orderservice.domain.BookOrderRepository;
import com.polarbookshop.orderservice.domain.OrderStatus;
import com.polarbookshop.orderservice.service.book.Book;
import com.polarbookshop.orderservice.service.book.BookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookOrderService {
  private final BookOrderRepository bookOrderRepository;
  private final BookClient bookClient;

  public Flux<BookOrder> getAllOrders() {
    return this.bookOrderRepository.findAll();
  }

  public Mono<BookOrder> submitOrder(String isbn, int quantity) {
    return bookClient.getBookByIsbn(isbn)
      .map(book -> buildAcceptedOrder(book, quantity))
      .defaultIfEmpty(buildRejectedOrder(isbn, quantity))
      .flatMap(this.bookOrderRepository::save);
  }

  private static BookOrder buildAcceptedOrder(Book book, int quantity) {
    return BookOrder.of(book.isbn(), "%s-%s".formatted(book.title(), book.author()), book.price(), quantity, OrderStatus.ACCEPTED);
  }

  private static BookOrder buildRejectedOrder(String isbn, int quantity) {
    return BookOrder.of(isbn, null, null, quantity, OrderStatus.REJECTED);
  }
}
