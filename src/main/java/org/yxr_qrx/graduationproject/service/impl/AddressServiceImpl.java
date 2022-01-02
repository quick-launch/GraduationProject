package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.Address;
import org.yxr_qrx.graduationproject.repository.AddressRepository;
import org.yxr_qrx.graduationproject.service.AddressService;

/**
 * @ClassName:AddressServiceImpl
 * @Author:41713
 * @Date 2021/11/19  20:12
 * @Version 1.0
 **/
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address create(Address address){return addressRepository.save(address);}

    @Override
    public Page<Address> getAllByCustomId(Long customId, Pageable pageable){return addressRepository.findAllByCustomId(customId,pageable);}

    @Override
    public Address update(Address address){return addressRepository.saveAndFlush(address);}

    @Override
    public Address getById(Long id){return addressRepository.findById(id).get();}

    @Override
    public Address getByCustomIdAndDefaultStatus(Long customId,boolean defaultStatus){return addressRepository.findByCustomIdAndDefaultStatus(customId,defaultStatus);}

    @Override
    public boolean isExist(Long id){return addressRepository.findById(id).isPresent();}

    @Override
    public  void delete(Long id){ addressRepository.deleteById(id);}
}
