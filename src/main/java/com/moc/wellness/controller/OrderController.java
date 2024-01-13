package com.moc.wellness.controller;

import com.moc.wellness.controller.generic.ManyToOneUserController;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.order.OrderBody;
import com.moc.wellness.dto.order.OrderResponse;
import com.moc.wellness.dto.order.PriceDto;
import com.moc.wellness.enums.OrderType;
import com.moc.wellness.mapper.OrderMapper;
import com.moc.wellness.model.Order;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.OrderRepository;
import com.moc.wellness.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders Controller")
public class OrderController
        extends ManyToOneUserController<Order, OrderBody, OrderResponse,
        OrderRepository, OrderMapper, OrderService> {
    public OrderController(OrderService modelService) {
        super(modelService, "order");
    }

    @Operation(summary = "Get all the order types",
            description = "All authenticated users can access.", responses = {
            @ApiResponse(description = "The response will contain a set with all possible order types",
                    responseCode = "200", useReturnTypeSchema = true)
    })
    @GetMapping("/types")
    public ResponseEntity<Set<OrderType>> getOrderTypes() {
        return ResponseEntity.ok(Set.of(OrderType.ALL, OrderType.PAYED, OrderType.NOT_PAYED));
    }

    @Operation(summary = "Get all the orders by type",
            description = "Only admin can access", responses = {
            @ApiResponse(description = "The response will contain a pageable response with the payload as a list of entity response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {
            @Parameter(
                    name = "orderType",
                    description = "The type of orders to retrieve",
                    example = "PAYED"
            )
    })
    @PatchMapping("/admin")
    public ResponseEntity<PageableResponse<List<OrderResponse>>> getAllOrders(
            @Valid @RequestBody PageableBody pageableBody,
            @RequestParam(required = false) OrderType orderType

    ) {
        return ResponseEntity.ok(modelService.getAllModels(pageableBody, orderType));
    }

    @Operation(summary = "Pay an order",
            description = """
                    All authenticated users can access.
                    For the update to be successfully the authenticated user
                    needs to be the users that made the order and also the order
                    should not be already payed.
                    """, responses = {
            @ApiResponse(description = "The response will contain an entity response dto with the payed field set to true",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "id", description = "The id of the order",
            required = true, example = "1")})
    @PatchMapping("/pay/{id}")
    public ResponseEntity<OrderResponse> payOrder(@PathVariable Long id,
                                                  @Valid @RequestBody PriceDto priceDto) {

        return ResponseEntity.ok(modelService.payOrder(id, priceDto));
    }

    @Operation(summary = "Get the orders for a user",
            description = """
                    All authenticated users can access.
                    For the retrieve to be successfully the authenticated user
                    needs to be the users that made the order or the be an admin.
                    """, responses = {
            @ApiResponse(description = "The response will contain a pageable response with the payload as a list of entity response dto",
                    responseCode = "200", useReturnTypeSchema = true)
    }, parameters = {@Parameter(name = "userId", description = "The id of the user that made the orders",
            required = true, example = "1"),
            @Parameter(name = "orderType", description = "The type of orders to retrieve", example = "PAYED"
            )})
    @PatchMapping("/user/{userId}")
    public ResponseEntity<PageableResponse<List<OrderResponse>>> getOrderByUser(
            @PathVariable Long userId,
            @Valid @RequestBody PageableBody pageableBody,
            @RequestParam(required = false) OrderType orderType
    ) {
        return ResponseEntity.ok(modelService.getModelByUser(userId, pageableBody, orderType));
    }

    @Operation(summary = "Create an order",
            description = "All authenticated users can access.",
            responses = {
                    @ApiResponse(description = "The response will contain an entity response dto",
                            responseCode = "201", useReturnTypeSchema = true)
            })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderBody body) {
        OrderResponse orderResponse = modelService.createOrder(body);
        return ResponseEntity.created(URI.create(devUrl + "/" + modelName + "s/" + orderResponse.getId()))
                .body(orderResponse);
    }

    @Override
    @Operation(summary = "Get order by id",
            description = "All authenticated users can access. For the retrieve to be successfully the user should be the one that made the order or admin.",
            responses = {
                    @ApiResponse(description = "The response will contain an entity response dto",
                            responseCode = "200", useReturnTypeSchema = true)
            }, parameters = {@Parameter(name = "id", description = "The id of the entity",
            required = true, example = "1")})
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getModelById(
            @PathVariable Long id) {
        return ResponseEntity.ok(modelService.getModelById(id));
    }


}
