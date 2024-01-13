package com.moc.wellness.repository;

import com.moc.wellness.model.Order;
import com.moc.wellness.repository.generic.ManyToOneUserRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends ManyToOneUserRepository<Order> {

    Page<Order> findAllByPayed(boolean payed, PageRequest request);

    Page<Order> findAllByUserIdAndPayed(Long userId,boolean payed, PageRequest request);



}
