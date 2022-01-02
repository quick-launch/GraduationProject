package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.Complaint;
import org.yxr_qrx.graduationproject.repository.ComplaintRepository;
import org.yxr_qrx.graduationproject.service.ComplaintService;

import java.util.List;

/**
 * @ClassName:ComplaintServiceImpl
 * @Author:41713
 * @Date 2021/11/12  19:24
 * @Version 1.0
 **/
@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public Complaint create(Complaint complaint){return complaintRepository.save(complaint);}

    @Override
    public boolean isExistByOrderId(Long orderId) {return complaintRepository.findAllByOrderId(orderId) != null;}

    @Override
    public void  deleteByOrderId(Long orderId) {complaintRepository.deleteByOrderId(orderId);}

    @Override
    public void delete(Long id) {complaintRepository.deleteById(id);}

    @Override
    public boolean isExist(Long id){return complaintRepository.findById(id).isPresent();}

    @Override
    public Page<Complaint> getAllByCustomId(Long id,Pageable pageable){return complaintRepository.findAllByCustomId(id,pageable);}

    @Override
    public int countAllByStatus_False(){return complaintRepository.countAllByStatus(false);}

    @Override
    public Page<Complaint> getAll(Pageable pageable){return complaintRepository.findAll(pageable);}

    @Override
    public Complaint getById(Long id){return complaintRepository.findById(id).get();}

    @Override
    public List<Complaint> saveAllComplaints(List<Complaint> complaints) {return complaintRepository.saveAll(complaints);}

    @Override
    public List<Complaint> getAllComplaintByUserIdAndComplaintStatus(Long id) {return complaintRepository.findAllByOrderIdAndStatus(id,false);}

}
