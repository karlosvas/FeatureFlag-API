package com.equipo01.featureflag.featureflag.util;

import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * Utility component for building query parameters.
 * <p>
 * This class provides methods to construct a map of query parameters
 * for filtering.
 * </p>
 */
@Component
public class QueryParamBuilder {

    /**
     * Builds a map of query parameters for filtering features.
     *
     * @param name             the name filter (can be null)
     * @param enabledByDefault the enabled by default filter (can be null)
     * @return a map containing the query parameters
     */
    public Map<String, String> buildQueryFeature(String name, Boolean enabledByDefault) {
        Map<String, String> queryParams = new java.util.HashMap<>();

        queryParams.put("name", name != null ? name : "");
        queryParams.put("enabled", enabledByDefault != null ? String.valueOf(enabledByDefault) : "");
        return queryParams;
    }
}
