package com.polarbookshop.orderservice.service.book;

import java.math.BigDecimal;

public record Book(
  String isbn,
  String title,
  String author,
  BigDecimal price
) {
}
