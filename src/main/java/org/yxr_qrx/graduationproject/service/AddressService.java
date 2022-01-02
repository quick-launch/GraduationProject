package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yxr_qrx.graduationproject.entity.Address;

/**
 * @InterfaceName:AddressService
 * @Author:41713
 * @Date 2021/11/19  20:13
 * @Version
 **/
public interface AddressService {
    Address create(Address address);

    Page<Address> getAllByCustomId(Long customId, Pageable pageable);

    Address update(Address address);

    Address getById(Long id);

    Address getByCustomIdAndDefaultStatus(Long customId, boolean defaultStatus);

    boolean isExist(Long id);

    void delete(Long id);
}
