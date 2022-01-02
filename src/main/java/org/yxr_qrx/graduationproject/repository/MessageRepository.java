package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yxr_qrx.graduationproject.entity.Message;

import java.util.List;

/**
 * @InterfaceName:MessageRepository
 * @Author:41713
 * @Date 2021/11/19  17:24
 * @Version 1.0
 **/
@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    int countAllByUserIdAndMessageStatus(Long userId,boolean messageStatus);

    Page<Message> findAllByUserId(Long userId, Pageable pageable);

    List<Message> findAllByUserIdAndMessageStatus(Long userId,boolean messageStatus);
}
