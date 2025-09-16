package com.equipo01.featureflag.featureflag.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for pagination links in API responses.
 * 
 * This DTO represents the links section of paginated API responses, providing
 * navigation URLs for clients to traverse through large datasets. It follows
 * RESTful API pagination standards and provides a structured way to include
 * navigation metadata in JSON responses.
 * 
 * The links object typically accompanies paginated data to help clients:
 * - Navigate to the first page of results
 * - Navigate to the last page of results
 * - Navigate to the next page of results
 * - Navigate to the previous page of results
 * - Understand the total count of available items
 * 
 * This implementation supports common pagination patterns such as:
 * - Offset-based pagination
 * - Cursor-based pagination
 * - Page number-based pagination
 * 
 * JSON structure example:
 * <pre>
 * {
 *   "count": "150",
 *   "first": { "href": "/api/features?page=1" },
 *   "last": { "href": "/api/features?page=15" },
 *   "next": { "href": "/api/features?page=3" },
 *   "prev": { "href": "/api/features?page=1" }
 * }
 * </pre>
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinksDto implements Serializable {

  /**
   * Total count of items available across all pages.
   * 
   * Note: This is represented as a String to accommodate very large counts
   * and to maintain consistency with various pagination implementations.
   */
  @JsonProperty("count")
  private String count;

  /**
   * Link to the first page of results.
   */
  @JsonProperty("first")
  private LinkDto first;

  /**
   * Link to the last page of results.
   */
  @JsonProperty("last")
  private LinkDto last;

  /**
   * Link to the next page of results.
   * 
   * This field will be null when the current page is the last page,
   * indicating that no more data is available in the forward direction.
   * Clients should check for null before attempting to navigate to the next page.
   */
  @JsonProperty("next")
  private LinkDto next;

  /**
   * Link to the previous page of results.
   * 
   * This field will be null when the current page is the first page,
   * indicating that no previous data is available. Clients should check
   * for null before attempting to navigate to the previous page.
   */
  @JsonProperty("prev")
  private LinkDto prev;
}
