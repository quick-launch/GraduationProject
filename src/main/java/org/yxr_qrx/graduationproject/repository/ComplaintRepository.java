package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yxr_qrx.graduationproject.entity.Complaint;

import java.util.List;

/**
 * @InterfaceName:ComplaintRepository
 * @Author:41713
 * @Date 2021/11/12  19:23
 * @Version 1.0
 **/
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Complaint findAllByOrderId(Long orderId);

    @Modifying
    @Transactional
    void deleteByOrderId(Long orderId);

    @Query(value ="SELECT * FROM (complaint) INNER JOIN orders ON complaint.order_id = orders.id WHERE orders.custom_id=?1",
            countQuery ="SELECT count(*) FROM (complaint)  INNER JOIN orders ON complaint.order_id = orders.id WHERE orders.custom_id=?1",
            nativeQuery = true)
    Page<Complaint> findAllByCustomId(Long customId,Pageable pageable);

    int countAllByStatus(boolean status);

    List<Complaint> findAllByOrderIdAndStatus(Long id, boolean complaintStatus);
}
