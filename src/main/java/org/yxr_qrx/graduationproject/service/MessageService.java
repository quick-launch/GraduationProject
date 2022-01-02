package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yxr_qrx.graduationproject.entity.Complaint;
import org.yxr_qrx.graduationproject.entity.Message;

import java.util.List;

/**
 * @InterfaceName:MessageService
 * @Author:41713
 * @Date 2021/11/19  17:24
 * @Version
 **/
public interface MessageService {
    List<Message> saveAllMessages(List<Message> messages);

    List<Message> getAllMessagesByUserIdAndMessageStatus(Long id);

    Message create(Message message);

    int countAllUnreadByUserId(Long userId);

    Message getById(Long id);

    Message update(Message message);

    Page<Message> getAllByUserId(Long id, Pageable pageable);
}
