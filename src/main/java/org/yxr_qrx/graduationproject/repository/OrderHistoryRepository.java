package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yxr_qrx.graduationproject.entity.OrderHistory;

import java.sql.Timestamp;

/**
 * @InterfaceName:OrderHistoryRepository
 * @Author:41713
 * @Date 2021/11/19  21:50
 * @Version 1.0
 **/
@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    @Modifying
    @Transactional
    void deleteByOrderId(Long orderId);

    OrderHistory findByOrderId(Long orderId);
}
