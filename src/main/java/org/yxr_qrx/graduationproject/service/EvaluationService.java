package org.yxr_qrx.graduationproject.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.yxr_qrx.graduationproject.entity.Evaluation;

/**
 * @InterfaceName:EvaluationService
 * @Author:41713
 * @Date 2021/11/12  14:01
 * @Version 1.0
 **/
public interface EvaluationService {
    Evaluation create(Evaluation evaluation);

    boolean isExistByOrderId(Long orderId);

    Page<Evaluation> getAllByCustomId(Long customId, Pageable pageable);

    void deleteByOrderId(Long orderId);

    void delete(Long id);

    boolean isExist(Long id);

    Evaluation getByOrderId(Long orderId);
}
