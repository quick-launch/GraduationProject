package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yxr_qrx.graduationproject.entity.Address;

/**
 * @InterfaceName:AddressRepository
 * @Author:41713
 * @Date 2021/11/19  20:13
 * @Version 1.0
 **/
@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    Page<Address> findAllByCustomId(Long customId,Pageable pageable);

    Address findByCustomIdAndDefaultStatus(Long customId,boolean defaultStatus);
}
