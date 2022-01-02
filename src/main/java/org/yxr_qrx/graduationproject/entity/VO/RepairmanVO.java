package org.yxr_qrx.graduationproject.entity.VO;

import lombok.Data;
import org.yxr_qrx.graduationproject.entity.RepairType;
import org.yxr_qrx.graduationproject.entity.Role;

/**
 * @ClassName:RepairmanVO
 * @Author:41713
 * @Date 2021/11/8  19:31
 * @Version 1.0
 **/
@Data
public class RepairmanVO {
    private String account;
    private Long id;
    private String name;
    private Role role;
    private String phone;
    private RepairType repairType;
}
