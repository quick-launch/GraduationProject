package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yxr_qrx.graduationproject.entity.Complaint;

import java.util.List;

/**
 * @InterfaceName:ComplaintService
 * @Author:41713
 * @Date 2021/11/12  19:25
 * @Version 1.0
 **/
public interface ComplaintService {
    Complaint create(Complaint complaint);

    boolean isExistByOrderId(Long orderId);

    void  deleteByOrderId(Long orderId);

    void delete(Long id);

    boolean isExist(Long id);

    Page<Complaint> getAllByCustomId(Long id, Pageable pageable);

    int countAllByStatus_False();

    Page<Complaint> getAll(Pageable pageable);

    Complaint getById(Long id);

    List<Complaint> saveAllComplaints(List<Complaint> complaints);

    List<Complaint> getAllComplaintByUserIdAndComplaintStatus(Long id);
}
