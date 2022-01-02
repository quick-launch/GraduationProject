package org.yxr_qrx.graduationproject.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yxr_qrx.graduationproject.config.MultiRequestBody;
import org.yxr_qrx.graduationproject.entity.AvatarPath;
import org.yxr_qrx.graduationproject.entity.RepairType;
import org.yxr_qrx.graduationproject.entity.VO.*;
import org.yxr_qrx.graduationproject.entity.Message;
import org.yxr_qrx.graduationproject.entity.User;
import org.yxr_qrx.graduationproject.service.*;
import org.yxr_qrx.graduationproject.utils.Entity2VO;
import org.yxr_qrx.graduationproject.utils.GiteeImgBedUtils;
import org.yxr_qrx.graduationproject.utils.JsonResult;
import org.yxr_qrx.graduationproject.utils.PasswordEncryptionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @ClassName:AdminController
 * @Author:41713
 * @Date 2021/10/31  21:13
 * @Version 1.0
 **/
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private AvatarPathService avatarPathService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private ImgPathService imgPathService;
    @Autowired
    private RepairTypeService repairTypeService;

    @ApiOperation(value = "添加账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名字", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "role", value = "权限", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "联系方式", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairType.id", value = "维修员专长编号", dataType = "String", paramType = "query")
    })
    @PostMapping("/register")
    @ResponseBody
    public JsonResult add(@RequestBody User user) {
        if (StringUtils.isBlank(user.getAccount())) {
            return JsonResult.errorMsg("账号不能为空");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return JsonResult.errorMsg("密码不能为空");
        }
        if (!Pattern.matches("^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$", user.getPassword())) {
            return JsonResult.errorMsg("password为8位数以上，包含大小写、数字");
        }
        if (StringUtils.isBlank(user.getName())) {
            return JsonResult.errorMsg("名字不能为空");
        }
        if (StringUtils.isBlank(user.getPhone())) {
            return JsonResult.errorMsg("电话不能为空");
        }
        if (userService.exist(user)) {
            try {
                user.setPassword(PasswordEncryptionUtils.planTextToMD5Encrypt(user.getPassword()));
                userService.create(user);
                return JsonResult.ok("注册成功");
            } catch (Exception e) {
                return JsonResult.errorException(e.getMessage());
            }
        } else {
            return JsonResult.errorMsg("你的用户名/名字/手机号已存在，注册失败");
        }
    }

    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "账号", required = true, paramType = "body"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "body")
    })
    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(@MultiRequestBody String account, @MultiRequestBody String password) {
        if (StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("密码不能为空");
        }
        User user = userService.findByAccount(account);
        if (user != null) {
            password = PasswordEncryptionUtils.planTextToMD5Encrypt(password);
            if (Objects.equals(password, user.getPassword())) {
                if (!StpUtil.isLogin()) {
                    StpUtil.login(user.getId(),false);
                    // 在登录时缓存user对象
                    StpUtil.getSession().set("user", user);
                    LOGGER.info(StpUtil.getTokenInfo().tokenValue);
                    return JsonResult.ok(StpUtil.getTokenInfo());
                } else {
                    return JsonResult.errorMsg("已登录账号，请勿重复登录");
                }
            } else {
                return JsonResult.errorMsg("密码错误");
            }
        } else {
            return JsonResult.errorMsg("当前账号不存在");
        }
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    @ResponseBody
    public JsonResult logout() {
        try {
            // 当前会话注销登录
            StpUtil.logout();
            return JsonResult.ok("已经注销当前登录");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("获取个人信息")
    @GetMapping("/getUserInfo")
    @ResponseBody
    public JsonResult getUserInfo() {
        try {
            User user = (User) StpUtil.getSession().get("user");
            UserInfoVO userInfoVO = new UserInfoVO(user, avatarPathService.getByUserId(user.getId()));
            return JsonResult.ok(userInfoVO);
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名字", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairTypeId", value = "维修种类id",  dataType = "String", paramType = "query")
    })
    @PostMapping("/updateUser")
    @ResponseBody
    public JsonResult updateUser(@RequestPart("json") UserUpdateVO userUpdateVO,
                                 @ApiParam(name = "avatar", value = "用户头像") @RequestParam(value = "avatar",required = false) MultipartFile avatar){
        try {
            User user = (User) StpUtil.getSession().get("user");
            if (userUpdateVO.getPassword()!=null){
                user.setPassword(PasswordEncryptionUtils.planTextToMD5Encrypt(userUpdateVO.getPassword()));
            }
            if (userUpdateVO.getPhone()!=null){
                user.setPhone(userUpdateVO.getPhone());
            }
            if (userUpdateVO.getName()!=null){
                user.setName(userUpdateVO.getName());
            }
            if (userUpdateVO.getRepairTypeId()!=null){
                user.setRepairType(repairTypeService.getById(userUpdateVO.getRepairTypeId()));
            }
            userService.update(user);
            if(avatar!=null){
                //源文件获取并创建新名称
                String originalFilename = avatar.getOriginalFilename();
                assert originalFilename != null;
                String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
                String newFileName = user.getId() + "." + substring;
                AvatarPath avatarPath = new AvatarPath();

                //若已存在路径，则将其删除
                if (avatarPathService.isExistByUserId(user.getId())) {
                    avatarPath = avatarPathService.getByUserId(user.getId());
                    if (!GiteeImgBedUtils.delete(avatarPath.getPath())) {
                        return JsonResult.errorMsg("删除文件失败");
                    }
                }
                //创建与更新新头像路径
                JSONObject content=GiteeImgBedUtils.uploadImg("avatar", newFileName, avatar);
                if(content==null){
                    return JsonResult.errorMsg("网络请求失败");
                }
                avatarPath.setUserId(user.getId());
                avatarPath.setPath(String.valueOf(content.getObj("path")));
                avatarPath.setDownloadUrl(String.valueOf(content.getObj("download_url")));
                avatarPathService.update(avatarPath);
            }
            return JsonResult.ok("更新成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("获取头像")
    @GetMapping("/getAvatarPath")
    @ResponseBody
    public JsonResult getAvatarPath() {
        try {
            User user = (User) StpUtil.getSession().get("user");
            AvatarPath avatarPath = avatarPathService.getByUserId(user.getId());
            return JsonResult.ok(avatarPath.getDownloadUrl());
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("未读信息统计")
    @GetMapping("/countUnreadMessage")
    @ResponseBody
    public JsonResult countUnreadMessage() {
        try {
            User user = (User) StpUtil.getSession().get("user");
            return JsonResult.ok(messageService.countAllUnreadByUserId(user.getId()));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }


    @ApiOperation(value = "查看订单详情")
    @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getOrderById")
    @ResponseBody
    public JsonResult getOrderById(Long id) {
        try {
            OrderInfoVO orderInfoVO = new OrderInfoVO(ordersService.getById(id), imgPathService.getAllByOrderId(id));
            return JsonResult.ok(orderInfoVO);
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("查看订单进程记录")
    @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getOrderHistory")
    @ResponseBody
    public JsonResult getOrderHistory(Long id) {
        try {
            return JsonResult.ok(orderHistoryService.getByOrderId(id));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("一键已读")
    @PostMapping("/allRead")
    @ResponseBody
    public JsonResult allRead(){
        try {
            User user=(User) StpUtil.getSession().get("user");
            List<Message> messages = messageService.getAllMessagesByUserIdAndMessageStatus(user.getId());
            for(Message message : messages){
                if(!message.isMessageStatus()){
                    message.setMessageStatus(true);
                }
            }
            messageService.saveAllMessages(messages);
            return JsonResult.ok("全部已读");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(("浏览所有消息"))
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllMessages")
    @ResponseBody
    public JsonResult getAllMessages(PageVO pageVO) {
        try {
            User user = (User) StpUtil.getSession().get("user");
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.DESC, "id");
            Page<Message> list = messageService.getAllByUserId(user.getId(), pageable);
            List<Message> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, MessageVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }


    @ApiOperation("浏览维修种类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllRepairType")
    @ResponseBody
    public JsonResult getAllRepairType(PageVO pageVO){
        try {
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<RepairType> list = repairTypeService.getAll(pageable);
            List<RepairType> data = list.getContent();
            return JsonResult.ok(data);
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }


}
