package org.yxr_qrx.graduationproject.service;

import org.yxr_qrx.graduationproject.entity.OrderHistory;

import java.sql.Timestamp;

/**
 * @InterfaceName:OrderHistoryService
 * @Author:41713
 * @Date 2021/11/19  21:52
 * @Version
 **/
public interface OrderHistoryService {
    OrderHistory create(OrderHistory orderHistory);

    void deleteByOrderId(Long orderId);

    OrderHistory getByOrderId(Long orderId);

    OrderHistory update(OrderHistory orderHistory);
}
