package com.equipo01.featureflag.featureflag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object representing a single navigation link in API responses.
 *
 * <p>This DTO encapsulates a URL reference that clients can use to navigate to related resources or
 * perform specific actions. It follows the HATEOAS (Hypermedia as the Engine of Application State)
 * principle by providing discoverable links within API responses, enabling clients to understand
 * available actions and navigation options dynamically.
 *
 * <p>The LinkDto is commonly used as a building block for more complex link structures, such as
 * pagination links, resource relationships, and action links. It provides a standardized way to
 * represent URLs in JSON responses while maintaining consistency across the API.
 *
 * <p>Key characteristics: - Simple, focused structure containing only the essential URL information
 * - Serializable for caching and persistence scenarios - Jackson integration for consistent JSON
 * marshalling/unmarshalling - Lombok integration for reduced boilerplate code
 *
 * <p>Common usage scenarios: - Pagination navigation (next, previous, first, last pages) - Resource
 * relationships (links to related entities) - Action links (edit, delete, activate operations) -
 * Self-referential links (current resource URL)
 *
 * <p>JSON structure example:
 *
 * <pre>
 * {
 *   "href": "https://api.example.com/features?page=2&size=10"
 * }
 * </pre>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto implements Serializable {

  /**
   * The hypertext reference (URL) that clients can use to navigate or perform actions.
   *
   * <p>This field should never be null when the LinkDto is part of an API response, as it
   * represents the essential navigation information that clients depend on.
   */
  @JsonProperty("href")
  private String href;
}
