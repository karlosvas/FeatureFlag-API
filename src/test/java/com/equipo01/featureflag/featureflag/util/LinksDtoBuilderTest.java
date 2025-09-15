package com.equipo01.featureflag.featureflag.util;

import static org.junit.jupiter.api.Assertions.*;

import com.equipo01.featureflag.featureflag.dto.LinksDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class LinksDtoBuilderTest {

  @InjectMocks private LinksDtoBuilder linksDtoBuilder;

  @Test
  public void testCreateLinksDtoWithEmptyPage() {
    Page<Object> emptyPage = Page.empty();
    String baseLink = "http://example.com/api/resource?param=value";
    LinksDto result = linksDtoBuilder.createLinksDto(emptyPage, baseLink);
    assertNotNull(result);
    assertNull(result.getFirst());
  }

  @Test
  public void testCreateLinksDtoWithSinglePage() {
    List<String> content = List.of("item1", "item2", "item3", "item4", "item5");
    Page<String> singlePage = new PageImpl<>(content, PageRequest.of(0, 10), content.size());
    String baseLink = "http://example.com/api/resource?param=value";
    LinksDto result = linksDtoBuilder.createLinksDto(singlePage, baseLink);
    assertNotNull(result);
    assertNotNull(result.getFirst());
    assertNotNull(result.getLast());
    assertNull(result.getNext());
    assertNull(result.getPrev());
    assertEquals("5", result.getCount());
  }

  @Test
  public void testCreateLinksDtoWithMultiplePages() {
    List<String> content = List.of("item1", "item2", "item3", "item4", "item5");
    Page<String> multiplePages = new PageImpl<>(content, PageRequest.of(1, 2), 5);
    String baseLink = "http://example.com/api/resource?param=value";
    LinksDto result = linksDtoBuilder.createLinksDto(multiplePages, baseLink);
    assertNotNull(result);
    assertNotNull(result.getFirst());
    assertNotNull(result.getLast());
    assertNotNull(result.getNext());
    assertNotNull(result.getPrev());
    assertEquals("5", result.getCount());
  }
}
