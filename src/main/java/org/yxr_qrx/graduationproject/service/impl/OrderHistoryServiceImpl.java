package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.OrderHistory;
import org.yxr_qrx.graduationproject.entity.Orders;
import org.yxr_qrx.graduationproject.repository.OrderHistoryRepository;
import org.yxr_qrx.graduationproject.service.OrderHistoryService;

import java.sql.Timestamp;

/**
 * @ClassName:OrderHistorySeviceImpl
 * @Author:41713
 * @Date 2021/11/19  21:52
 * @Version 1.0
 **/
@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Override
    public OrderHistory create(OrderHistory orderHistory) {return orderHistoryRepository.save(orderHistory);}

    @Override
    public void deleteByOrderId(Long orderId){orderHistoryRepository.deleteByOrderId(orderId);}

    @Override
    public OrderHistory getByOrderId(Long orderId){return orderHistoryRepository.findByOrderId(orderId);}

    @Override
    public OrderHistory update(OrderHistory orderHistory) {return orderHistoryRepository.saveAndFlush(orderHistory);}

}
