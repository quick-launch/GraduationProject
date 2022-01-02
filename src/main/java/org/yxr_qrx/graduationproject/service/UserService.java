package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yxr_qrx.graduationproject.entity.Role;
import org.yxr_qrx.graduationproject.entity.User;

import java.util.List;

/**
 * @InterfaceName:UserService
 * @Author:41713
 * @Date 2021/10/31  21:09
 * @Version 1.0
 **/
public interface UserService {

    User create(User user);

    boolean exist(User user);

    User findByAccount(String account);

    User update(User user);

    Page<User> getAllByRole(Role role, Pageable pageable);

    List<User> getAllRepairmanByType(Long repairtypeId);

    User getById(Long id);
}
