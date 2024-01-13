package com.moc.wellness.service;


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
import com.moc.wellness.service.generics.ManyToOneUserService;

import java.util.List;

public interface OrderService extends
        ManyToOneUserService<Order, OrderBody, OrderResponse, OrderRepository, OrderMapper> {

    PageableResponse<List<OrderResponse>> getAllModels(PageableBody pageableBody, OrderType orderType);

    OrderResponse payOrder(Long id, PriceDto priceDto);

    PageableResponse<List<OrderResponse>> getModelByUser(Long userId, PageableBody pageableBody, OrderType orderType);

    OrderResponse createOrder(OrderBody body);
}
