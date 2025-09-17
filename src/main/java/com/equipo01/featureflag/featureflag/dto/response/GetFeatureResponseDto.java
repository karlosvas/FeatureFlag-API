package com.equipo01.featureflag.featureflag.dto.response;

import com.equipo01.featureflag.featureflag.dto.LinksDto;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for paginated feature flag response data.
 *
 * <p>This DTO represents a paginated collection of feature flags along with navigation links,
 * providing a structured response for API endpoints that return multiple feature flags. It combines
 * the actual data with pagination metadata to enable efficient navigation through large datasets.
 *
 * <p>The DTO follows RESTful API pagination standards by including: - The requested page of feature
 * flag data - Navigation links for traversing the complete dataset - Metadata for understanding
 * pagination context
 *
 * <p>This structure is particularly useful for: - Feature flag listing endpoints with pagination -
 * Search results that may span multiple pages - Administrative interfaces that display feature
 * inventories - API clients that need to process large numbers of features
 *
 * <p>The pagination approach helps maintain API performance by limiting response sizes while
 * providing clients with the tools needed to access the complete dataset progressively.
 *
 * <p>JSON structure example:
 *
 * <pre>
 * {
 *   "features": [
 *     {
 *       "id": "123e4567-e89b-12d3-a456-426614174000",
 *       "name": "dark_mode",
 *       "description": "Enables dark mode theme",
 *       "enabledByDefault": true
 *     }
 *   ],
 *   "links": {
 *     "count": "150",
 *     "first": { "href": "/api/features?page=1" },
 *     "last": { "href": "/api/features?page=15" },
 *     "next": { "href": "/api/features?page=3" },
 *     "prev": { "href": "/api/features?page=1" }
 *   }
 * }
 * </pre>
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetFeatureResponseDto implements Serializable {

  /**
   * List of feature flags for the current page.
   *
   * <p>Each feature in the list contains complete information including ID, name, description, and
   * default enablement status, providing clients with all necessary data for feature evaluation and
   * management decisions.
   */
  private List<FeatureResponseDto> features;

  /**
   * Navigation links for pagination control.
   *
   * <p>The links enable clients to: - Navigate to adjacent pages (next/previous) - Jump to boundary
   * pages (first/last) - Understand the total scope of available data - Implement user-friendly
   * pagination controls
   */
  private LinksDto links;
}
