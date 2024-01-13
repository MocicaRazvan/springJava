package com.moc.wellness.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moc.wellness.controller.OrderController;
import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.common.UserDto;
import com.moc.wellness.dto.exercise.ExerciseResponse;
import com.moc.wellness.dto.order.OrderBody;
import com.moc.wellness.dto.order.OrderResponse;
import com.moc.wellness.dto.order.PriceDto;
import com.moc.wellness.dto.training.TrainingResponse;
import com.moc.wellness.enums.OrderType;
import com.moc.wellness.enums.Role;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.action.SubEntityNotOwner;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.filter.AuthFilter;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.Order;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.service.OrderService;
import com.moc.wellness.setup.OrderSetup;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@WebMvcTest(controllers = OrderController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrderControllersTest extends OrderSetup {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Get order types")
    public void getOrderTypes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(
                                        Set.of(OrderType.ALL, OrderType.PAYED, OrderType.NOT_PAYED)
                                )
                        ));

    }

    @Test
    @DisplayName("Get all orders success")
    public void getAllOrdersSuccess() throws Exception {
        when(orderService.getAllModels(pageableBody, OrderType.ALL))
                .thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableBody))
                        .param("orderType", OrderType.ALL.name()))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));

        verify(orderService).getAllModels(pageableBody, OrderType.ALL);
    }

    @Test
    @DisplayName("Get all orders 2 arguments of body not valid")
    public void getAllOrders2ArgsOfBodyNotValid() throws Exception {
        PageableBody notValid = PageableBody.builder()
                .page(-1)
                .size(-1)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(2)));

        verify(orderService, times(0)).getAllModels(any(), any());
    }

    @Test
    @DisplayName("Pay order success")
    public void payOrderSuccess() throws Exception {
        orderResponse.setPayed(true);
        when(orderService.payOrder(orderId, priceDto)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/pay/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDto)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(orderResponse)
                        ));

        verify(orderService).payOrder(orderId, priceDto);
    }

    @Test
    @DisplayName("Pay order price dto not valid")
    public void payOrderPriceDtoNotValid() throws Exception {
        PriceDto notValid = PriceDto.builder()
                .price(-1)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/pay/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(1)));

        verify(orderService, times(0)).payOrder(any(), any());
    }

    @Test
    @DisplayName("Pay order order not found")
    public void payOrderNotFound() throws Exception {

        when(orderService.payOrder(2L, priceDto))
                .thenThrow(new NotFoundEntity("order", 2L));

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/pay/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDto)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.name",
                                CoreMatchers.is("order")),
                        MockMvcResultMatchers.jsonPath("$.id",
                                CoreMatchers.is(2)));

        verify(orderService).payOrder(2L, priceDto);
    }

    @Test
    @DisplayName("Pay order already payed")
    public void payOrderAlreadyPayed() throws Exception {

        when(orderService.payOrder(2L, priceDto))
                .thenThrow(new IllegalActionException("Order already payed!"));

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/pay/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDto)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.message",
                                CoreMatchers.is("Order already payed!")));

        verify(orderService).payOrder(2L, priceDto);
    }

    @Test
    @DisplayName("Pay order not entity owner")
    public void payOrderNotEntityOwner() throws Exception {

        when(orderService.payOrder(orderId, priceDto))
                .thenThrow(new SubEntityNotOwner(trainerId, order.getUser().getId(), order.getId()));

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/pay/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(priceDto)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.expectedUserId",
                                CoreMatchers.is(order.getUser().getId().intValue())),
                        MockMvcResultMatchers.jsonPath("$.receivedUserId",
                                CoreMatchers.is(trainerId.intValue())),
                        MockMvcResultMatchers.jsonPath("$.entityId",
                                CoreMatchers.is(order.getId().intValue()))
                );

        verify(orderService).payOrder(orderId, priceDto);
    }

    @Test
    @DisplayName("Pay order price not match")
    public void payOrderPriceNotMatch() throws Exception {
        PriceDto notValid = PriceDto.builder()
                .price(11)
                .build();

        when(orderService.payOrder(orderId, notValid))
                .thenThrow(new IllegalActionException("Expected " + price + " ,but got " + notValid.getPrice()));

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/pay/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notValid)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.message",
                                CoreMatchers.is("Expected " + price + " ,but got " + notValid.getPrice()))
                );

        verify(orderService).payOrder(orderId, notValid);
    }


    @Test
    @DisplayName("Get all orders by user success")
    public void getAllOrdersByUserSuccess() throws Exception {
        when(orderService.getModelByUser(userId, pageableBody, OrderType.ALL))
                .thenReturn(pageableResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/user/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("orderType", OrderType.ALL.name())
                        .content(objectMapper.writeValueAsString(pageableBody)))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(pageableResponse)
                        ));


        verify(orderService).getModelByUser(userId, pageableBody, OrderType.ALL);
    }

    @Test
    @DisplayName("Get all orders by user user not found")
    public void getAllOrdersByUserUserNotFound() throws Exception {
        when(orderService.getModelByUser(userId, pageableBody, OrderType.ALL))
                .thenThrow(new NotFoundEntity("user", userId));

        mockMvc.perform(MockMvcRequestBuilders.patch("/orders/user/" + userId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("orderType", OrderType.ALL.name())
                        .content(objectMapper.writeValueAsString(pageableBody)))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.name",
                                CoreMatchers.is("user")),
                        MockMvcResultMatchers.jsonPath("$.id",
                                CoreMatchers.is(userId.intValue())));


        verify(orderService).getModelByUser(userId, pageableBody, OrderType.ALL);
    }

    @Test
    @DisplayName("Create order success")
    public void createOrderSuccess() throws Exception {
        when(orderService.createOrder(orderBody)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .content(objectMapper.writeValueAsString(orderBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(orderResponse)
                        ),
                        MockMvcResultMatchers.header().string("location",
                                "http://localhost:8080/orders/" + orderId));

        verify(orderService).createOrder(orderBody);
    }

    @Test
    @DisplayName("Create all order body not valid")
    public void createAllOrderBodyNotValid() throws Exception {
        OrderBody notValid = OrderBody.builder()
                .shippingAddress("")
                .trainings(null)
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .content(objectMapper.writeValueAsString(notValid))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status",
                                CoreMatchers.is(400)),
                        MockMvcResultMatchers.jsonPath("$.reasons.size()",
                                CoreMatchers.is(2)));

        verify(orderService, times(0)).createOrder(any());
    }

    @Test
    @DisplayName("Get order by id success")
    public void getOrderByIdSuccess() throws Exception {
        when(orderService.getModelById(orderId)).thenReturn(orderResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().json(
                                objectMapper.writeValueAsString(orderResponse)
                        ));

        verify(orderService).getModelById(orderId);
    }
}
