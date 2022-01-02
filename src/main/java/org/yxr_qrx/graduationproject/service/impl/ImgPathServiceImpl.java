package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.ImgPath;
import org.yxr_qrx.graduationproject.repository.ImgPathRepository;
import org.yxr_qrx.graduationproject.service.ImgPathService;

import java.util.List;

/**
 * @ClassName:ImgPathServiceImpl
 * @Author:41713
 * @Date 2021/11/19  19:48
 * @Version 1.0
 **/
@Service
public class ImgPathServiceImpl implements ImgPathService {
    @Autowired
    private ImgPathRepository imgPathRepository;

    @Override
    public ImgPath create(ImgPath path){return imgPathRepository.save(path);}

    @Override
    public List<ImgPath> getAllByOrderId(Long orderId){return imgPathRepository.findAllByOrderId(orderId);}

    @Override
    public void deleteByOrderId(Long orderId){imgPathRepository.deleteAllByOrderId(orderId);}
}
