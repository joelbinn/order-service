package com.polarbookshop.orderservice.service;

import com.polarbookshop.orderservice.domain.BookOrder;
import com.polarbookshop.orderservice.domain.BookOrderRepository;
import com.polarbookshop.orderservice.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookOrderService {
  private final BookOrderRepository bookOrderRepository;

  public Flux<BookOrder> getAllOrders() {
    return this.bookOrderRepository.findAll();
  }

  public Mono<BookOrder> submitOrder(String isbn, int quantity) {
    return Mono.just(buildRejectedOrder(isbn, quantity))
      .flatMap(this.bookOrderRepository::save);
  }

  private BookOrder buildRejectedOrder(String isbn, int quantity) {
    return BookOrder.of(isbn, null, null, quantity, OrderStatus.REJECTED);
  }
}
