package com.moc.wellness.mapper;

import com.moc.wellness.dto.order.OrderBody;
import com.moc.wellness.dto.order.OrderResponse;
import com.moc.wellness.mapper.generics.DtoMapper;
import com.moc.wellness.model.Order;
import com.moc.wellness.utils.EntitiesUtils;
import org.aspectj.weaver.ast.Or;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class OrderMapper extends DtoMapper<Order,OrderBody,OrderResponse> {

    @Autowired
    protected EntitiesUtils entitiesUtils;


    @Mapping(target = "trainings", expression = "java(entitiesUtils.getActualTraining(body.getTrainings()))")
    public abstract Order fromBodyToModel(OrderBody body);

    public abstract OrderResponse fromModelToResponse(Order order);

    @Override
    public void updateModelFromBody(OrderBody body,Order order){
        order.setPayed(body.isPayed());
        order.setShippingAddress(body.getShippingAddress());
        order.setTrainings(entitiesUtils.getActualTraining(body.getTrainings()));
    }

}
