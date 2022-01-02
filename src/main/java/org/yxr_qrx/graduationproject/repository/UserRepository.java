package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yxr_qrx.graduationproject.entity.Role;
import org.yxr_qrx.graduationproject.entity.User;
import org.yxr_qrx.graduationproject.service.UserService;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccount(String account);

    User findByName(String name);

    User findByPhone(String phone);

    Page<User> findAllByRole(Role role, Pageable pageable);

    List<User> findAllByRoleAndRepairTypeId(Role role, Long repairTypeId);
}
