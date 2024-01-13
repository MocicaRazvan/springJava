package com.moc.wellness.utils;

import com.moc.wellness.dto.common.PageableBody;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class PageableUtilsTests {


    @Test
    @DisplayName("Make sorting criteria")
    public void makeSortingCriteria() {
        PageableUtilsCustom pageableUtils = new PageableUtilsCustom();

        Map<String, String> sort = new HashMap<>();
        sort.put("title", "asc");
        sort.put("body", "desc");
        sort.put("ceva", "altceva");
        sort.put("one", "");

        Sort expected = Sort.by(
                new Sort.Order(Sort.Direction.ASC, "title"),
                new Sort.Order(Sort.Direction.DESC, "body")
        );

        Sort made = pageableUtils.createSortFromMap(sort);

        Assertions.assertEquals(made, expected);

    }

    @Test
    @DisplayName("Create page request")
    public void createPageRequest() {
        PageableUtilsCustom pageableUtils = new PageableUtilsCustom();

        int page = 10;
        int size = 10;
        Map<String, String> sortingCriteria = new HashMap<>();

        PageableBody pageableBody = PageableBody.builder()
                .size(size)
                .page(page)
                .sortingCriteria(sortingCriteria)
                .build();

        PageRequest pg = pageableUtils.createPageRequest(pageableBody);

        Assertions.assertEquals(pg, PageRequest.of(page, size));
    }

}
