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

    @ApiOperation(value = "????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "??????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "??????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "??????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "role", value = "??????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairType.id", value = "?????????????????????", dataType = "String", paramType = "query")
    })
    @PostMapping("/register")
    @ResponseBody
    public JsonResult add(@RequestBody User user) {
        if (StringUtils.isBlank(user.getAccount())) {
            return JsonResult.errorMsg("??????????????????");
        }
        if (StringUtils.isBlank(user.getPassword())) {
            return JsonResult.errorMsg("??????????????????");
        }
        if (!Pattern.matches("^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{8,}$", user.getPassword())) {
            return JsonResult.errorMsg("password???8???????????????????????????????????????");
        }
        if (StringUtils.isBlank(user.getName())) {
            return JsonResult.errorMsg("??????????????????");
        }
        if (StringUtils.isBlank(user.getPhone())) {
            return JsonResult.errorMsg("??????????????????");
        }
        if (userService.exist(user)) {
            try {
                user.setPassword(PasswordEncryptionUtils.planTextToMD5Encrypt(user.getPassword()));
                userService.create(user);
                return JsonResult.ok("????????????");
            } catch (Exception e) {
                return JsonResult.errorException(e.getMessage());
            }
        } else {
            return JsonResult.errorMsg("???????????????/??????/?????????????????????????????????");
        }
    }

    @ApiOperation(value = "??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "??????", required = true, paramType = "body"),
            @ApiImplicitParam(name = "password", value = "??????", required = true, paramType = "body")
    })
    @PostMapping("/login")
    @ResponseBody
    public JsonResult login(@MultiRequestBody String account, @MultiRequestBody String password) {
        if (StringUtils.isBlank(password)) {
            return JsonResult.errorMsg("??????????????????");
        }
        User user = userService.findByAccount(account);
        if (user != null) {
            password = PasswordEncryptionUtils.planTextToMD5Encrypt(password);
            if (Objects.equals(password, user.getPassword())) {
                if (!StpUtil.isLogin()) {
                    StpUtil.login(user.getId(),false);
                    // ??????????????????user??????
                    StpUtil.getSession().set("user", user);
                    LOGGER.info(StpUtil.getTokenInfo().tokenValue);
                    return JsonResult.ok(StpUtil.getTokenInfo());
                } else {
                    return JsonResult.errorMsg("????????????????????????????????????");
                }
            } else {
                return JsonResult.errorMsg("????????????");
            }
        } else {
            return JsonResult.errorMsg("?????????????????????");
        }
    }

    @ApiOperation("??????")
    @PostMapping("/logout")
    @ResponseBody
    public JsonResult logout() {
        try {
            // ????????????????????????
            StpUtil.logout();
            return JsonResult.ok("????????????????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("??????????????????")
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

    @ApiOperation("??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "?????????",  dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairTypeId", value = "????????????id",  dataType = "String", paramType = "query")
    })
    @PostMapping("/updateUser")
    @ResponseBody
    public JsonResult updateUser(@RequestPart("json") UserUpdateVO userUpdateVO,
                                 @ApiParam(name = "avatar", value = "????????????") @RequestParam(value = "avatar",required = false) MultipartFile avatar){
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
                //?????????????????????????????????
                String originalFilename = avatar.getOriginalFilename();
                assert originalFilename != null;
                String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());
                String newFileName = user.getId() + "." + substring;
                AvatarPath avatarPath = new AvatarPath();

                //????????????????????????????????????
                if (avatarPathService.isExistByUserId(user.getId())) {
                    avatarPath = avatarPathService.getByUserId(user.getId());
                    if (!GiteeImgBedUtils.delete(avatarPath.getPath())) {
                        return JsonResult.errorMsg("??????????????????");
                    }
                }
                //??????????????????????????????
                JSONObject content=GiteeImgBedUtils.uploadImg("avatar", newFileName, avatar);
                if(content==null){
                    return JsonResult.errorMsg("??????????????????");
                }
                avatarPath.setUserId(user.getId());
                avatarPath.setPath(String.valueOf(content.getObj("path")));
                avatarPath.setDownloadUrl(String.valueOf(content.getObj("download_url")));
                avatarPathService.update(avatarPath);
            }
            return JsonResult.ok("????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("????????????")
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

    @ApiOperation("??????????????????")
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


    @ApiOperation(value = "??????????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation("????????????????????????")
    @ApiImplicitParam(name = "id", value = "??????id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getOrderHistory")
    @ResponseBody
    public JsonResult getOrderHistory(Long id) {
        try {
            return JsonResult.ok(orderHistoryService.getByOrderId(id));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("????????????")
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
            return JsonResult.ok("????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(("??????????????????"))
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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


    @ApiOperation("????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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
