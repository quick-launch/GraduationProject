package org.yxr_qrx.graduationproject.entity.VO;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yxr_qrx.graduationproject.entity.Role;

/**
 * @ClassName:UserVO
 * @Author:41713
 * @Date 2021/11/8  19:19
 * @Version 1.0
 **/
@Data
public class UserVO {
    private String account;
    private Long id;
    private String name;
    private String phone;
    private Role role;
}
