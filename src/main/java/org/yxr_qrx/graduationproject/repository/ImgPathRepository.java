package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yxr_qrx.graduationproject.entity.ImgPath;

import java.util.List;

/**
 * @InterfaceName:ImgPathRepository
 * @Author:41713
 * @Date 2021/11/19  19:46
 * @Version 1.0
 **/
@Repository
public interface ImgPathRepository extends JpaRepository<ImgPath, Long> {
    List<ImgPath> findAllByOrderId(Long orderId);

    @Transactional
    void deleteAllByOrderId(Long orderId);
}
