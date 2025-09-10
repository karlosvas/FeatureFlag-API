package com.equipo01.featureflag.featureflag.util;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.equipo01.featureflag.featureflag.exception.FeatureFlagException;
import com.equipo01.featureflag.featureflag.exception.enums.MessageError;

/**
 * Utility component for building base links with query parameters.
 * <p>
 * This class provides a method to create a base link string
 * with the given query parameters.
 * </p>
 */
@Component
public class BaseLinkBuilder {

    /**
     * Creates a base link string with the provided query parameters.
     *
     * @param queryParams a map of query parameters to include in the link
     * @return a string representing the base link with query parameters
     * @throws FeatureFlagException if the queryParams map is null or empty
     */
    public String createBaseLink(Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            throw new FeatureFlagException(
                    MessageError.MAP_QUERY_PARAMS_NOT_VALID.getStatus(),
                    MessageError.MAP_QUERY_PARAMS_NOT_VALID.getMessage(),
                    MessageError.MAP_QUERY_PARAMS_NOT_VALID.getDescription());
        }
        var uriBuilder = UriComponentsBuilder.newInstance();
        queryParams.forEach(uriBuilder::queryParam);
        return uriBuilder.build(false).toUriString();
    }
}
