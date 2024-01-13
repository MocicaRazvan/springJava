package com.moc.wellness.utils;

import com.moc.wellness.dto.common.PageableBody;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PageableUtilsCustom {

    public Sort createSortFromMap(Map<String, String> sortCriteria) {
        return Sort.by(
                sortCriteria.entrySet().stream().filter(
                        entry -> entry.getValue().equals("asc") || entry.getValue().equals("desc")
                ).map(
                        entry -> new Sort.Order(
                                entry.getValue().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                                entry.getKey()
                        )
                ).collect(Collectors.toList())

        );
    }

    public PageRequest createPageRequest(PageableBody pageableBody) {
        return PageRequest.of(
                pageableBody.getPage(),
                pageableBody.getSize(),
                createSortFromMap(pageableBody.getSortingCriteria())
        );
    }

}
