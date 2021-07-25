package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 * Repository for manipulating orders data in database
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> getOrdersByUser_IdAndCostBetweenAndTimeOfPurchaseBetween(Long userId, Float minCost, Float maxCost,
                                                                         LocalDateTime minTime, LocalDateTime maxTime, Pageable pageable);
    Optional<Order> findOrderByUser_IdAndId(Long userId, Long orderId);
}
