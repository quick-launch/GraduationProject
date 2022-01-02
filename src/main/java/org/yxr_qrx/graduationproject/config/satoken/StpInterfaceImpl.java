package org.yxr_qrx.graduationproject.config.satoken;

/**
 * @ClassName:StpInterfaceImpl
 * @Author:41713
 * @Date 2021/11/27  11:49
 * @Version 1.0
 **/

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yxr_qrx.graduationproject.controller.UserController;
import org.yxr_qrx.graduationproject.entity.User;
import org.yxr_qrx.graduationproject.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 */
@Component    // 打开此注解，保证此类被springboot扫描，即可完成sa-token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(StpInterfaceImpl.class);
    @Autowired
    private UserService userService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
//        List<String> list = new ArrayList<String>();
//        list.add("ADMIN");
//        list.add("REPAIRMAN");
//        list.add("CUSTOM");
//        return list;
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();
        User user= (User) StpUtil.getSession().get("user");
        LOGGER.info("STP验证经过============");
        list.add(String.valueOf(userService.getById(user.getId()).getRole()));
        return list;
    }

}
