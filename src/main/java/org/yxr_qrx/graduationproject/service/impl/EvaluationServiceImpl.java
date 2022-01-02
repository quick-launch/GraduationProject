package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.Evaluation;
import org.yxr_qrx.graduationproject.repository.EvaluationRepository;
import org.yxr_qrx.graduationproject.service.EvaluationService;

/**
 * @ClassName:EvaluationServiceImpl
 * @Author:41713
 * @Date 2021/11/12  14:02
 * @Version 1.0
 **/
@Service
public class EvaluationServiceImpl implements EvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    @Override
    public Evaluation create(Evaluation evaluation) {return evaluationRepository.save(evaluation);}

    @Override
    public boolean isExistByOrderId(Long orderId){return evaluationRepository.findByOrderId(orderId) != null;}

    @Override
    public Page<Evaluation> getAllByCustomId(Long customId, Pageable pageable){return evaluationRepository.findAllByCustomId(customId,pageable);}

    @Override
    public void deleteByOrderId(Long orderId){evaluationRepository.deleteByOrderId(orderId);}

    @Override
    public void delete(Long id){evaluationRepository.deleteById(id);}

    @Override
    public boolean isExist(Long id){return evaluationRepository.findById(id).isPresent();}

    @Override
    public Evaluation getByOrderId(Long orderId){return evaluationRepository.findByOrderId(orderId);}

}
