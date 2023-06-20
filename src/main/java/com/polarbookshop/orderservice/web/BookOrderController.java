package com.polarbookshop.orderservice.web;

import com.polarbookshop.orderservice.domain.BookOrder;
import com.polarbookshop.orderservice.service.BookOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class BookOrderController {
  private final BookOrderService bookOrderService;

  @GetMapping
  public Flux<BookOrder> getAllOrders() {
    return this.bookOrderService.getAllOrders();
  }

  @PostMapping
  public Mono<BookOrder> submitOrder(@Valid @RequestBody BookOrderRequest bookOrderRequest) {
    return bookOrderService.submitOrder(bookOrderRequest.isbn(), bookOrderRequest.quantity());
  }
}
