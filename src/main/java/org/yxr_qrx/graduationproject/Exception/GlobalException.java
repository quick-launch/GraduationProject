package org.yxr_qrx.graduationproject.Exception;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.dev33.satoken.exception.DisableLoginException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import org.yxr_qrx.graduationproject.utils.JsonResult;
/**
 * @ClassName:GlobalException
 * @Author:41713
 * @Date 2021/11/27  22:23
 * @Version
 **/


/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalException {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalException.class);
    // 全局异常拦截（拦截项目中的所有异常）
    @ResponseBody
    @ExceptionHandler
    public JsonResult handlerException(Exception e, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // 打印堆栈，以供调试
        LOGGER.info("全局异常---------------");
        e.printStackTrace();

        // 不同异常返回不同状态码
        if (e instanceof NotLoginException) {	// 如果是未登录异常
            NotLoginException ee = (NotLoginException) e;
            return JsonResult.errorException(ee.getMessage());
        }
        else if(e instanceof NotRoleException) {		// 如果是角色异常
            NotRoleException ee = (NotRoleException) e;
            return JsonResult.errorException("当前用户不属于此角色：" + ee.getRole());
        }
        else if(e instanceof NotPermissionException) {	// 如果是权限异常
            NotPermissionException ee = (NotPermissionException) e;
            return JsonResult.errorException("无此权限：" + ee.getCode());
        }
        else if(e instanceof DisableLoginException) {	// 如果是被封禁异常
            DisableLoginException ee = (DisableLoginException) e;
            return JsonResult.errorException("账号被封禁：" + ee.getDisableTime() + "秒后解封");
        }
        else {	// 普通异常, 输出：500 + 异常信息
            return JsonResult.errorException(e.getMessage());
        }
    }

}