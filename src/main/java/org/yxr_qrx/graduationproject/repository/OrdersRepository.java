package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yxr_qrx.graduationproject.entity.Orders;

/**
 * @InterfaceName:OrderRepository
 * @Author:41713
 * @Date 2021/11/9  16:03
 * @Version 1.0
 **/
@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long>, JpaSpecificationExecutor<Orders> {
    Page<Orders> findAllByCustomId(Long customId, Pageable pageable);

    Page<Orders> findAllByCustomIdAndStatus(Long customId, int status, Pageable pageable);

    Page<Orders> findAllByStatus(int status, Pageable pageable);

    int countAllByRepairmanIdAndStatus(Long repairmanId, int status);

    Page<Orders> findAllByRepairmanIdAndStatus(Long repairmanId,int status,Pageable pageable);
}
