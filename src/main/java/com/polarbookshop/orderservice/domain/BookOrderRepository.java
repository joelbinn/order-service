package com.polarbookshop.orderservice.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BookOrderRepository extends ReactiveCrudRepository<BookOrder, Long> {
}
