package com.moc.wellness.services;

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
import com.moc.wellness.exception.notFound.PrivateRouteException;
import com.moc.wellness.mapper.OrderMapper;
import com.moc.wellness.model.Exercise;
import com.moc.wellness.model.Order;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.OrderRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.impl.OrderServiceImpl;
import com.moc.wellness.setup.OrderSetup;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTests extends OrderSetup {

    @Mock
    private EntitiesUtils entitiesUtils;
    @Mock
    private UserRepository userRepository;

    @Mock
    private PageableUtilsCustom pageableUtils;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private OrderServiceImpl orderService;


    @Test
    @DisplayName("Find all orders success, type ALL or null")
    public void findAllOrdersSuccess() {
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAll(pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);

        List<OrderType> types = new ArrayList<>();
        types.add(null);
        types.add(OrderType.ALL);

        types.forEach(t -> {

            PageableResponse<List<OrderResponse>> made =
                    orderService.getAllModels(pageableBody, t);

            Assertions.assertEquals(made, pageableResponse);

        });

        verify(pageableUtils, times(2)).createPageRequest(pageableBody);
        verify(orderRepository, times(2)).findAll(pageRequest);
        verify(orderRepository, times(0)).findAllByPayed(anyBoolean(), any());
        verify(orderMapper, times(2)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Find all orders success NOT_PAYED")
    public void findAllOrdersSuccessNOT_PAYED() {
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAllByPayed(false, pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);

        PageableResponse<List<OrderResponse>> made =
                orderService.getAllModels(pageableBody, OrderType.NOT_PAYED);

        Assertions.assertEquals(made, pageableResponse);

        verify(pageableUtils, times(1)).createPageRequest(pageableBody);
        verify(orderRepository, times(0)).findAll(pageRequest);
        verify(orderRepository, times(1)).findAllByPayed(false, pageRequest);
        verify(orderMapper, times(1)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Find all orders success PAYED")
    public void findAllOrdersSuccessPAYED() {
        order.setPayed(true);
        orderResponse.setPayed(true);
        pageableResponse.getPayload().get(0).setPayed(true);
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAllByPayed(true, pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);

        PageableResponse<List<OrderResponse>> made =
                orderService.getAllModels(pageableBody, OrderType.PAYED);

        Assertions.assertEquals(made, pageableResponse);

        verify(pageableUtils, times(1)).createPageRequest(pageableBody);
        verify(orderRepository, times(0)).findAll(pageRequest);
        verify(orderRepository, times(1)).findAllByPayed(true, pageRequest);
        verify(orderMapper, times(1)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Find all orders success (overloaded function)")
    public void findAllOrdersSuccessOverloaded() {
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAll(pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);


        PageableResponse<List<OrderResponse>> made =
                orderService.getAllModels(pageableBody);

        Assertions.assertEquals(made, pageableResponse);

        verify(pageableUtils).createPageRequest(pageableBody);
        verify(orderRepository).findAll(pageRequest);
        verify(orderRepository, times(0)).findAllByPayed(anyBoolean(), any());
        verify(orderMapper).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Pay order success")
    public void payOrderSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).then(invocation -> {
            Order arg = invocation.getArgument(0);
            arg.setPayed(true);
            return arg;
        });
        when(orderMapper.fromModelToResponse(order)).then(invocation -> {
            Order arg = invocation.getArgument(0);
            orderResponse.setPayed(arg.isPayed());
            return orderResponse;
        });

        OrderResponse made = orderService.payOrder(orderId, priceDto);

        Assertions.assertEquals(made, orderResponse);

        verify(userUtils).getPrincipal();
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
        verify(orderMapper).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Pay order not found")
    public void payOrderNotFound() {
        when(orderRepository.findById(2L)).thenThrow(new NotFoundEntity("order", 2L));

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> orderService.payOrder(2L, priceDto));

        Assertions.assertEquals(ex.getName(), "order");
        Assertions.assertEquals(ex.getId(), 2L);

        verify(userUtils, times(0)).getPrincipal();
        verify(orderRepository).findById(2L);
        verify(orderRepository, times(0)).save(order);
        verify(orderMapper, times(0)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Pay order already payed")
    public void payOrderAlreadyPayed() {
        order.setPayed(true);
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));

        IllegalActionException ex =
                Assertions.assertThrows(IllegalActionException.class,
                        () -> orderService.payOrder(orderId, priceDto));

        Assertions.assertEquals(ex.getMessage(), "Order already payed!");

        verify(orderRepository).findById(orderId);
        verify(userUtils, times(0)).getPrincipal();
        verify(orderRepository, times(0)).save(order);
        verify(orderMapper, times(0)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Pay order not owner")
    public void payOrderNotOwner() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        when(userUtils.getPrincipal()).thenReturn(userAdmin);

        SubEntityNotOwner ex =
                Assertions.assertThrows(SubEntityNotOwner.class,
                        () -> orderService.payOrder(orderId, priceDto));

        Assertions.assertEquals(ex.getAuthId(), userAdmin.getId());
        Assertions.assertEquals(ex.getEntityUserId(), userUser.getId());
        Assertions.assertEquals(ex.getEntityId(), orderId);

        verify(orderRepository).findById(orderId);
        verify(userUtils).getPrincipal();
        verify(orderRepository, times(0)).save(order);
        verify(orderMapper, times(0)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Pay order price doesn't match")
    public void payOrderPriceNoMatch() {
        PriceDto wrong = PriceDto.builder().price(11).build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        when(userUtils.getPrincipal()).thenReturn(userUser);


        IllegalActionException ex =
                Assertions.assertThrows(IllegalActionException.class,
                        () -> orderService.payOrder(orderId, wrong));

        Assertions.assertEquals(ex.getMessage(), "Expected " + priceDto.getPrice() + " ,but got " + wrong.getPrice());

        verify(orderRepository).findById(orderId);
        verify(userUtils).getPrincipal();
        verify(orderRepository, times(0)).save(order);
        verify(orderMapper, times(0)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Get all orders by user success owner")
    public void getOrdersByUserSuccessOwner() {
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userUtils.hasPermissionToModifyEntity(userUser, userId)).thenReturn(true);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAllByUserId(userId, pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);

        List<OrderType> types = new ArrayList<>();
        types.add(null);
        types.add(OrderType.ALL);

        Assertions.assertEquals(userUser.getId(), userId);

        types.forEach(t -> {

            PageableResponse<List<OrderResponse>> made =
                    orderService.getModelByUser(userId, pageableBody, t);

            Assertions.assertEquals(made, pageableResponse);

        });

        verify(userUtils, times(2)).getPrincipal();
        verify(userRepository, times(2)).existsById(userId);
        verify(userUtils, times(2)).hasPermissionToModifyEntity(userUser, userId);
        verify(pageableUtils, times(2)).createPageRequest(pageableBody);
        verify(orderRepository, times(2)).findAllByUserId(userId, pageRequest);
        verify(orderRepository, times(0)).findAllByUserIdAndPayed(any(), anyBoolean(), any());
        verify(orderMapper, times(2)).fromModelToResponse(order);
    }


    @Test
    @DisplayName("Get all orders by user success admin")
    public void getOrdersByUserSuccessAdmin() {
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(userUtils.getPrincipal()).thenReturn(userAdmin);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userUtils.hasPermissionToModifyEntity(userAdmin, userId)).thenReturn(true);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAllByUserId(userId, pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);

        List<OrderType> types = new ArrayList<>();
        types.add(null);
        types.add(OrderType.ALL);

        Assertions.assertEquals(userUser.getId(), userId);

        types.forEach(t -> {

            PageableResponse<List<OrderResponse>> made =
                    orderService.getModelByUser(userId, pageableBody, t);

            Assertions.assertEquals(made, pageableResponse);

        });

        verify(userUtils, times(2)).getPrincipal();
        verify(userRepository, times(2)).existsById(userId);
        verify(userUtils, times(2)).hasPermissionToModifyEntity(userAdmin, userId);
        verify(pageableUtils, times(2)).createPageRequest(pageableBody);
        verify(orderRepository, times(2)).findAllByUserId(userId, pageRequest);
        verify(orderRepository, times(0)).findAllByUserIdAndPayed(any(), anyBoolean(), any());
        verify(orderMapper, times(2)).fromModelToResponse(order);
    }

    @Test
    @DisplayName("Get payed orders by user success owner")
    public void getPayedOrdersByUserSuccessOwner() {
        order.setPayed(true);
        orderResponse.setPayed(true);
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageRequest, 1);
        when(userUtils.getPrincipal()).thenReturn(userUser);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userUtils.hasPermissionToModifyEntity(userUser, userId)).thenReturn(true);
        when(pageableUtils.createPageRequest(pageableBody)).thenReturn(pageRequest);
        when(orderRepository.findAllByUserIdAndPayed(userId, true, pageRequest)).thenReturn(mockPage);
        when(orderMapper.fromModelToResponse(order)).thenReturn(orderResponse);

        PageableResponse<List<OrderResponse>> made =
                orderService.getModelByUser(userId, pageableBody, OrderType.PAYED);

        Assertions.assertEquals(made, pageableResponse);

        verify(userUtils).getPrincipal();
        verify(userRepository).existsById(userId);
        verify(userUtils).hasPermissionToModifyEntity(userUser, userId);
        verify(pageableUtils).createPageRequest(pageableBody);
        verify(orderRepository, times(0)).findAllByUserId(any(), any());
        verify(orderRepository).findAllByUserIdAndPayed(userId, true, pageRequest);
        verify(orderMapper).fromModelToResponse(order);
    }


    @Test
    @DisplayName("Get all orders by user not found user")
    public void getOrdersByUserNotFoundUser() {
        when(userUtils.getPrincipal()).thenReturn(userAdmin);
        when(userRepository.existsById(4L)).thenReturn(false);

        NotFoundEntity ex =
                Assertions.assertThrows(NotFoundEntity.class,
                        () -> orderService.getModelByUser(4L, pageableBody, null));

        Assertions.assertEquals(ex.getName(), "user");
        Assertions.assertEquals(ex.getId(), 4L);

        verify(userUtils).getPrincipal();
        verify(userRepository).existsById(4L);
        verify(userUtils, times(0)).hasPermissionToModifyEntity(any(), any());
        verify(pageableUtils, times(0)).createPageRequest(any());
        verify(orderRepository, times(0)).findAllByUserId(any(), any());
        verify(orderRepository, times(0)).findAllByUserIdAndPayed(any(), anyBoolean(), any());
        verify(orderMapper, times(0)).fromModelToResponse(any());
    }

    @Test
    @DisplayName("Get all orders by user private route")
    public void getOrdersByUserPrivateRoute() {
        when(userUtils.getPrincipal()).thenReturn(userTrainer);
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userUtils.hasPermissionToModifyEntity(userTrainer, userId)).thenReturn(false);

        PrivateRouteException ex =
                Assertions.assertThrows(PrivateRouteException.class,
                        () -> orderService.getModelByUser(userId, pageableBody, null));

        Assertions.assertEquals(ex.getMessage(), "Not allowed!");


        verify(userUtils).getPrincipal();
        verify(userRepository).existsById(userId);
        verify(userUtils).hasPermissionToModifyEntity(userTrainer, userId);
        verify(pageableUtils, times(0)).createPageRequest(any());
        verify(orderRepository, times(0)).findAllByUserId(any(), any());
        verify(orderRepository, times(0)).findAllByUserIdAndPayed(any(), anyBoolean(), any());
        verify(orderMapper, times(0)).fromModelToResponse(any());
    }

    @Test
    @DisplayName("Create order success")
    public void createOrderSuccess() {
        when(userUtils.getPrincipal()).thenReturn(userUser);
        order.setUser(null);
        when(orderMapper.fromBodyToModel(orderBody)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.fromModelToResponse(order)).then(invocation -> {
            Order arg = invocation.getArgument(0);
            Assertions.assertEquals(arg.getUser(), userUser);
            return orderResponse;
        });

        OrderResponse made = orderService.createOrder(orderBody);

        Assertions.assertEquals(made, orderResponse);

        verify(userUtils).getPrincipal();
        verify(orderMapper).fromBodyToModel(orderBody);
        verify(orderRepository).save(order);
        verify(orderMapper).fromModelToResponse(order);
    }

}
