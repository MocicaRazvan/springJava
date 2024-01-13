package com.moc.wellness.service.impl;

import com.moc.wellness.dto.common.PageableBody;
import com.moc.wellness.dto.common.PageableResponse;
import com.moc.wellness.dto.order.OrderBody;
import com.moc.wellness.dto.order.OrderResponse;
import com.moc.wellness.dto.order.PriceDto;
import com.moc.wellness.enums.OrderType;
import com.moc.wellness.exception.action.IllegalActionException;
import com.moc.wellness.exception.action.SubEntityNotOwner;
import com.moc.wellness.exception.notFound.NotFoundEntity;
import com.moc.wellness.mapper.OrderMapper;
import com.moc.wellness.model.Order;
import com.moc.wellness.model.Training;
import com.moc.wellness.model.user.UserCustom;
import com.moc.wellness.repository.OrderRepository;
import com.moc.wellness.repository.UserRepository;
import com.moc.wellness.service.OrderService;
import com.moc.wellness.service.impl.generics.ManyToOneUserServiceImpl;
import com.moc.wellness.utils.EntitiesUtils;
import com.moc.wellness.utils.PageableUtilsCustom;
import com.moc.wellness.utils.UserUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class OrderServiceImpl
        extends ManyToOneUserServiceImpl<Order, OrderBody, OrderResponse, OrderRepository, OrderMapper>
        implements OrderService {


    private final EntitiesUtils entitiesUtils;
    private final UserRepository userRepository;


    public OrderServiceImpl(OrderRepository modelRepository, OrderMapper modelMapper, PageableUtilsCustom pageableUtils, UserUtils userUtils, EntitiesUtils entitiesUtils, UserRepository userRepository) {
        super(modelRepository, modelMapper, pageableUtils, userUtils, "order");
        this.entitiesUtils = entitiesUtils;
        this.userRepository = userRepository;
    }

    public PageableResponse<List<OrderResponse>> getAllModels(PageableBody pageableBody, OrderType orderType) {


        PageRequest pageRequest = pageableUtils.createPageRequest(pageableBody);

        Page<Order> page;

        if (orderType == null || orderType == OrderType.ALL) {
            page = modelRepository.findAll(pageRequest);
        } else {
            boolean payed = orderType == OrderType.PAYED;
            page = modelRepository.findAllByPayed(payed, pageRequest);
        }


        return PageableResponse.<List<OrderResponse>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageableResponse<List<OrderResponse>> getAllModels(PageableBody pageableBody) {
        return getAllModels(pageableBody, OrderType.ALL);
    }

    @Override
    @Transactional
    public OrderResponse payOrder(Long id, PriceDto priceDto) {
        Order order = getModel(id);

        if (order.isPayed()) {
            throw new IllegalActionException("Order already payed!");
        }

        UserCustom authUser = userUtils.getPrincipal();

        if (!order.getUser().getId().equals(authUser.getId())) {
            throw new SubEntityNotOwner(authUser.getId(), order.getUser().getId(), order.getId());
        }

        double orderTotal = order.getTrainings().stream().mapToDouble(Training::getPrice).sum();
        if (orderTotal != priceDto.getPrice()) {
            throw new IllegalActionException("Expected " + orderTotal + " ,but got " + priceDto.getPrice());
        }
        order.setPayed(true);
        Order savedOrder = modelRepository.save(order);

        return modelMapper.fromModelToResponse(savedOrder);
    }


    @Override
    public PageableResponse<List<OrderResponse>> getModelByUser(Long userId, PageableBody pageableBody, OrderType orderType) {
        UserCustom authUser = userUtils.getPrincipal();

        if (!userRepository.existsById(userId)) {
            throw new NotFoundEntity("user", userId);
        }

        privateRoute(true, authUser, userId);

        PageRequest pageRequest = pageableUtils.createPageRequest(pageableBody);

        Page<Order> page;

        if (orderType == null || orderType == OrderType.ALL) {
            page = modelRepository.findAllByUserId(userId, pageRequest);
        } else {
            boolean payed = orderType == OrderType.PAYED;
            page = modelRepository.findAllByUserIdAndPayed(userId, payed, pageRequest);
        }


        return PageableResponse.<List<OrderResponse>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .payload(page.getContent().stream().map(modelMapper::fromModelToResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderBody body) {
        UserCustom authUser = userUtils.getPrincipal();
        Order order = modelMapper.fromBodyToModel(body);
        order.setUser(authUser);


        Order savedOrder = modelRepository.save(order);

        return modelMapper.fromModelToResponse(savedOrder);
    }


}
