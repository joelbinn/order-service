package com.polarbookshop.orderservice.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

/**
 * Configuration properties for the Polar Bookshop client.
 *
 * @param catalogServiceUri the URI of the catalog service to be used when retrieving book information
 */
@ConfigurationProperties(prefix = "polar")
public record ClientProperties(
  @NotNull URI catalogServiceUri
) {
}
