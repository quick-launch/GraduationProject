package org.yxr_qrx.graduationproject.service;

import org.yxr_qrx.graduationproject.entity.ImgPath;

import java.util.List;

/**
 * @InterfaceName:ImgPathService
 * @Author:41713
 * @Date 2021/11/19  19:49
 * @Version
 **/
public interface ImgPathService {
    ImgPath create(ImgPath path);

    List<ImgPath> getAllByOrderId(Long orderId);

    void deleteByOrderId(Long orderId);
}
