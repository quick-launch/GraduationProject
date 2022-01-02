package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.yxr_qrx.graduationproject.entity.Orders;

import java.sql.Timestamp;
import java.util.List;

/**
 * @InterfaceName:OrderService
 * @Author:41713
 * @Date 2021/11/10  18:47
 * @Version 1.0
 **/
public interface OrdersService {
    Orders create(Orders orders);

    Page<Orders> getAllByCustomId(Long customId, Pageable pageable);

    Page<Orders> getAllByCustomIdAndStatus(Long customId, int status, Pageable pageable);

    void deleteById(Long id);

    Orders getById(Long id);

    Page<Orders> getByStatus(int status, Pageable pageable);

    Orders update(Orders order);

    int countAssignOrderById(Long repairmanId);

    Page<Orders> getAllByRepairmanIdAndStatus(Long repairmanId, int status, Pageable pageable);

    Page<Orders> getAllOrderByRequirements(String repairType, String province, String city, String area, Pageable pageable);
}
