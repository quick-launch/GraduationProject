package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yxr_qrx.graduationproject.entity.RepairType;

/**
 * @InterfaceName:RepairTypeRepository
 * @Author:41713
 * @Date 2021/11/19  13:16
 * @Version 1.0
 **/
@Repository
public interface RepairTypeRepository extends JpaRepository<RepairType,Long> {
}
