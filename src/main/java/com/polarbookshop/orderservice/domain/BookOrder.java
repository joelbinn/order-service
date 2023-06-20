package com.polarbookshop.orderservice.domain;

import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.math.BigDecimal;
import java.time.Instant;

@With
public record BookOrder(
  @Id Long id,
  String bookIsbn,
  String bookName,
  BigDecimal bookPrice,
  Integer quantity,
  OrderStatus status,
  // Metadata
  @Version
  int version,
  @CreatedDate
  Instant createdDate,
  @LastModifiedDate
  Instant lastModifiedDate
) {
  public static BookOrder of(String bookIsbn, String bookName, BigDecimal bookPrice, Integer quantity, OrderStatus status) {
    return new BookOrder(null, bookIsbn, bookName, bookPrice, quantity, status, 0, null, null);
  }
}
