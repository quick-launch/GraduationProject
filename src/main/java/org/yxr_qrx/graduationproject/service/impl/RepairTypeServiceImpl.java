package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.RepairType;
import org.yxr_qrx.graduationproject.repository.RepairTypeRepository;
import org.yxr_qrx.graduationproject.service.RepairTypeService;

/**
 * @ClassName:RepairTypeServiceImpl
 * @Author:41713
 * @Date 2021/11/19  13:06
 * @Version 1.0
 **/
@Service
public class RepairTypeServiceImpl implements RepairTypeService {
    @Autowired
    private RepairTypeRepository repairTypeRepository;

    @Override
    public RepairType create(RepairType repairType){return repairTypeRepository.save(repairType);}

    @Override
    public void deleteById(Long id){repairTypeRepository.deleteById(id);}

    @Override
    public RepairType getById(Long id){return repairTypeRepository.getById(id);}

    @Override
    public Page<RepairType> getAll(Pageable pageable) {return repairTypeRepository.findAll(pageable);}
}
