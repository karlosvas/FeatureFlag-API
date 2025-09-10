package com.equipo01.featureflag.featureflag.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.equipo01.featureflag.featureflag.dto.LinkDto;
import com.equipo01.featureflag.featureflag.dto.LinksDto;

/**
 * Utility component for building pagination links.
 * <p>
 * This class provides methods to create a LinksDto object containing
 * pagination links based on the provided Page data and base link.
 * </p>
 */
@Component
public class LinksDtoBuilder {

    /**
     * Creates a LinkDto object with the specified page and size parameters.
     *
     * @param baseLink the base link to which pagination parameters will be appended
     * @param page     the page number
     * @param size     the size of the page
     * @return a LinkDto object with the constructed href
     */
    private static LinkDto createLink(String baseLink, int page, int size) {
        String href = baseLink + "&page=" + page + "&size=" + size;
        return new LinkDto(href);
    }

    /**
     * Creates a LinksDto object containing pagination links.
     *
     * @param <T>      the type of the content in the Page
     * @param pageData the Page object containing pagination information
     * @param baseLink the base link to which pagination parameters will be appended
     * @return a LinksDto object with pagination links
     */
    public <T> LinksDto createLinksDto(Page<T> pageData, String baseLink) {
        if (pageData.getTotalElements() == 0) {
            return new LinksDto();
        }
        LinksDto linksDto = new LinksDto();
        linksDto.setCount(String.valueOf(pageData.getTotalElements()));

        Pageable pageable = pageData.getPageable();
        int currentPage = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int totalPages = pageData.getTotalPages();
        linksDto.setFirst(createLink(baseLink, 0, pageSize));
        linksDto.setLast(createLink(baseLink, totalPages - 1, pageSize));

        if (pageData.hasNext()) {
            linksDto.setNext(createLink(baseLink, currentPage + 1, pageSize));
        }
        if (pageData.hasPrevious()) {
            linksDto.setPrev(createLink(baseLink, currentPage - 1, pageSize));
        }
        return linksDto;
    }
}
