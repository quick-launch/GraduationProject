package org.yxr_qrx.graduationproject.entity.VO;

import lombok.Data;

/**
 * @ClassName:UserUpdateVO
 * @Author:41713
 * @Date 2021/12/7  20:02
 * @Version 1.0
 **/
@Data
public class UserUpdateVO {
    private Long id;
    private String password;
    private String phone;
    private String name;
    private Long repairTypeId;
}
