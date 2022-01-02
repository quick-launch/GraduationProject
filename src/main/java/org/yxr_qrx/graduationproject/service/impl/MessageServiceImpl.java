package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.Message;
import org.yxr_qrx.graduationproject.repository.MessageRepository;
import org.yxr_qrx.graduationproject.service.MessageService;

import java.util.List;

/**
 * @ClassName:MessageServiceImpl
 * @Author:41713
 * @Date 2021/11/19  17:23
 * @Version 1.0
 **/
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> saveAllMessages(List<Message> messages) {return messageRepository.saveAll(messages);}

    @Override
    public List<Message> getAllMessagesByUserIdAndMessageStatus(Long id) {return messageRepository.findAllByUserIdAndMessageStatus(id,false);}

    @Override
    public Message create(Message message){return  messageRepository.save(message);}

    @Override
    public int countAllUnreadByUserId(Long userId){return  messageRepository.countAllByUserIdAndMessageStatus(userId,false);}

    @Override
    public Message getById(Long id){return messageRepository.findById(id).get();}

    @Override
    public Message update(Message message){return messageRepository.saveAndFlush(message);}

    @Override
    public Page<Message> getAllByUserId(Long id, Pageable pageable){return messageRepository.findAllByUserId(id,pageable);}
}
