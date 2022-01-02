package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yxr_qrx.graduationproject.entity.RepairType;

/**
 * @InterfaceName:RepairTypeService
 * @Author:41713
 * @Date 2021/11/19  13:16
 * @Version
 **/
public interface RepairTypeService {

    RepairType create(RepairType repairType);

    void deleteById(Long id);

    RepairType getById(Long id);

    Page<RepairType> getAll(Pageable pageable);
}
