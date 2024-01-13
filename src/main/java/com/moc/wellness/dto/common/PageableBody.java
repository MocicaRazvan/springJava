package com.moc.wellness.dto.common;

import com.moc.wellness.validations.SortingCriteria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "The request dto when the response will be a PageableResponse ")
public class PageableBody {
    @NotNull(message = "The page number should be present")
    @Min(value = 0, message = "Page is a non negative value.")
    @Schema(description = "The page value, 0 index based", minimum = "0")
    private int page = 0;

    @NotNull(message = "The page size should be present")
    @Min(value = 1, message = "The size should be at least 1.")
    @Schema(description = "The page size value", minimum = "1")
    private int size = 10;

    @SortingCriteria
    @Schema(description = """
            The sorting criteria. If the request is for the User Entity the allowed criteria are firstName, lastName, email.
            If the requests it's for an entity that belongs to an use always will be allowed the criteria user.firstName, user.lastName, user.email.
            If the requests it's for an order entity additionally will be allowed the criteria shippingAddress and payed.
            For any other entities will be added the criteria title and body.
            Null values are considered value.
            """)
    private Map<String, String> sortingCriteria = new HashMap<>();

}
