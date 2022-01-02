package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.AvatarPath;
import org.yxr_qrx.graduationproject.repository.AvatarPathRepository;
import org.yxr_qrx.graduationproject.service.AvatarPathService;

/**
 * @ClassName:AvatarPathServiceImpl
 * @Author:41713
 * @Date 2021/11/22  19:58
 * @Version 1.0
 **/
@Service
public class AvatarPathServiceImpl implements AvatarPathService {
    @Autowired
    private AvatarPathRepository avatarPathRepository;

    @Override
    public boolean isExistByUserId(Long userId) {return avatarPathRepository.findByUserId(userId)!=null;}

    @Override
    public AvatarPath update(AvatarPath avatarPath) {return avatarPathRepository.saveAndFlush(avatarPath);}

    @Override
    public AvatarPath getByUserId(Long userId) {return avatarPathRepository.findByUserId(userId);}

    @Override
    public boolean updatePathByUserId(Long userId,String path) {return avatarPathRepository.updatePathByUserId(userId,path)==1;}
}
