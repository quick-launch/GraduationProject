package org.yxr_qrx.graduationproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.yxr_qrx.graduationproject.entity.Evaluation;
import org.yxr_qrx.graduationproject.entity.Message;

/**
 * @InterfaceName:EvaluationRepository
 * @Author:41713
 * @Date 2021/11/12  14:00
 * @Version 1.0
 **/
@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    Evaluation findByOrderId(long orderId);

    @Query(value ="SELECT * FROM (evaluation) INNER JOIN orders ON evaluation.order_id = orders.id WHERE orders.custom_id=?1",
            countQuery ="SELECT count(*) FROM (evaluation)  INNER JOIN orders ON evaluation.order_id = orders.id WHERE orders.custom_id=?1",
            nativeQuery = true)
    Page<Evaluation> findAllByCustomId(Long customId,Pageable pageable);

    @Modifying
    @Transactional
    void deleteByOrderId(Long orderId);
}
