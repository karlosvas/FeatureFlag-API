package com.equipo01.featureflag.featureflag.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PageRequestFactoryTest {

    @InjectMocks
    private PageRequestFactory pageRequestFactory;

    @Test
    public void testCreatePageRequestWithValidParameters() {
        var pageRequest = pageRequestFactory.createPageRequest(2, 20);
        assertEquals(pageRequest.getPageSize(), 20);
        assertEquals(pageRequest.getPageNumber(), 2);
    }

    @Test
    public void testCreatePageRequestWithNullParameters() {
        var pageRequest = pageRequestFactory.createPageRequest(null, null);
        assertEquals(pageRequest.getPageSize(), 10);
        assertEquals(pageRequest.getPageNumber(), 0);
    }

    @Test
    public void testCreatePageRequestWithNegativePage() {
        var pageRequest = pageRequestFactory.createPageRequest(-1, 15);
        assertEquals(pageRequest.getPageSize(), 15);
        assertEquals(pageRequest.getPageNumber(), 0);
    }

    @Test
    public void testCreatePageRequestWithZeroSize() {
        var pageRequest = pageRequestFactory.createPageRequest(1, 0);
        assertEquals(pageRequest.getPageSize(), 10);
        assertEquals(pageRequest.getPageNumber(), 1);
    }
}
