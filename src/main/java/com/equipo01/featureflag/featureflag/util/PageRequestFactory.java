package com.equipo01.featureflag.featureflag.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * Factory class for creating PageRequest objects.
 * <p>
 * This class provides a method to create PageRequest instances
 * with default values if the provided parameters are null or invalid.
 * </p>
 */
@Component
public class PageRequestFactory {
    
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public PageRequest createPageRequest(Integer page, Integer size) {
        int pageNumber = (page != null && page >= 0) ? page : DEFAULT_PAGE_NUMBER;
        int pageSize = (size != null && size > 0) ? size : DEFAULT_PAGE_SIZE;
        return PageRequest.of(pageNumber, pageSize);
    }
}
