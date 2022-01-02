package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.Role;
import org.yxr_qrx.graduationproject.entity.User;
import org.yxr_qrx.graduationproject.repository.UserRepository;
import org.yxr_qrx.graduationproject.service.UserService;

import java.util.List;

/**
 * @ClassName:UserServiceImpl
 * @Author:41713
 * @Date 2021/11/1  16:54
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean exist(User user) {
        if (userRepository.findByAccount(user.getAccount())==null){
            if(userRepository.findByName(user.getName())==null){
                return userRepository.findByPhone(user.getPhone())==null;
            }
            return false;
        }
        return false;
    }

    @Override
    public User findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Override
    public User update(User user) {return userRepository.saveAndFlush(user);}

    @Override
    public Page<User> getAllByRole(Role role, Pageable pageable){
        return userRepository.findAllByRole(role,pageable);
    }

    @Override
    public List<User> getAllRepairmanByType(Long repairTypeId){return  userRepository.findAllByRoleAndRepairTypeId(Role.valueOf("REPAIRMAN"),repairTypeId);}

    @Override
    public User getById(Long id){return userRepository.findById(id).get();}


}
