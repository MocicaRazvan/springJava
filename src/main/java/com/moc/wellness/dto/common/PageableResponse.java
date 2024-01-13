package com.moc.wellness.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Schema(description = "The response for a page, when the request it's of type PageableBody")
public class PageableResponse<T> {
    @Schema(description = "The total elements for the provided criteria")
    private long totalElements;

    @Schema(description = "The total pages for the provided criteria")
    private long totalPages;

    @Schema(description = "The payload, usually a list of entities")
    private T payload;
}
