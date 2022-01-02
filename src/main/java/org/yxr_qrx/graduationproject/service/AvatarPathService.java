package org.yxr_qrx.graduationproject.service;

import org.yxr_qrx.graduationproject.entity.AvatarPath;

/**
 * @InterfaceName:AvatarPathService
 * @Author:41713
 * @Date 2021/11/22  19:58
 * @Version
 **/
public interface AvatarPathService {
    boolean isExistByUserId(Long userId);

    AvatarPath update(AvatarPath avatarPath);

    AvatarPath getByUserId(Long userId);

    boolean updatePathByUserId(Long userId, String path);
}
