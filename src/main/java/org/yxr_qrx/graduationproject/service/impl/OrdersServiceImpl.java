package org.yxr_qrx.graduationproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yxr_qrx.graduationproject.entity.Address;
import org.yxr_qrx.graduationproject.entity.Orders;
import org.yxr_qrx.graduationproject.entity.RepairType;
import org.yxr_qrx.graduationproject.entity.User;
import org.yxr_qrx.graduationproject.repository.OrdersRepository;
import org.yxr_qrx.graduationproject.service.OrdersService;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:OrderServiceImpl
 * @Author:41713
 * @Date 2021/11/10  18:49
 * @Version 1.0
 **/
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public Orders create(Orders orders){return ordersRepository.save(orders);}

    @Override
    public Page<Orders> getAllByCustomId(Long customId, Pageable pageable){return ordersRepository.findAllByCustomId(customId,pageable);}

    @Override
    public Page<Orders> getAllByCustomIdAndStatus(Long customId, int status, Pageable pageable){return ordersRepository.findAllByCustomIdAndStatus(customId,status,pageable);}

    @Override
    public void deleteById(Long id){ordersRepository.deleteById(id);}

    @Override
    public Orders getById(Long id){return ordersRepository.findById(id).get();}

    @Override
    public Page<Orders> getByStatus(int status, Pageable pageable){return ordersRepository.findAllByStatus(status,pageable);}

    @Override
    public Orders update(Orders order) {return ordersRepository.saveAndFlush(order);}

    @Override
    public int countAssignOrderById(Long repairmanId){return ordersRepository.countAllByRepairmanIdAndStatus(repairmanId,2);}

    @Override
    public Page<Orders> getAllByRepairmanIdAndStatus(Long repairmanId, int status, Pageable pageable){return ordersRepository.findAllByRepairmanIdAndStatus(repairmanId,status,pageable);}

    @Override
    public Page<Orders> getAllOrderByRequirements(String repairType, String province, String city,String area,Pageable pageable){
        Specification<Orders> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("status"),1));
            if (!repairType.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("repairType").as(String.class), repairType));
            }
            if (province != null&& !province.equals("00")) {
                predicates.add(criteriaBuilder.equal(root.get("province"), province));
                if (city != null&& !city.equals("00")) {
                    predicates.add(criteriaBuilder.equal(root.get("city"), city));
                    if (area != null&&!area.equals("00")) {
                        predicates.add(criteriaBuilder.equal(root.get("area"), area));
                    }
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return ordersRepository.findAll(specification,pageable);
    }
}
