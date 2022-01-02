package org.yxr_qrx.graduationproject.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.yxr_qrx.graduationproject.config.MultiRequestBody;
import org.yxr_qrx.graduationproject.entity.*;
import org.yxr_qrx.graduationproject.entity.VO.*;
import org.yxr_qrx.graduationproject.service.*;
import org.yxr_qrx.graduationproject.utils.Entity2VO;
import org.yxr_qrx.graduationproject.utils.GiteeImgBedUtils;
import org.yxr_qrx.graduationproject.utils.JsonResult;
import org.yxr_qrx.graduationproject.utils.PasswordEncryptionUtils;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

/**
 * @ClassName:AdminController
 * @Author:41713
 * @Date 2021/11/7  22:43
 * @Version 1.0
 **/
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private RepairTypeService repairTypeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private AvatarPathService avatarPathService;

    @ApiOperation(value = "添加维修种类")
    @ApiImplicitParam(name = "name", value = "维修种类名称", required = true, dataType = "String", paramType = "query")
    @PostMapping("/createRepairType")
    @ResponseBody
    public JsonResult createRepairType(@MultiRequestBody String name) {
        try {
            RepairType repairType = new RepairType();
            repairType.setName(name);
            repairTypeService.create(repairType);
            return JsonResult.ok("添加成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "删除维修种类")
    @ApiImplicitParam(name = "id", value = "维修种类id", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delRepairType")
    @ResponseBody
    public JsonResult delRepairType(@MultiRequestBody Long id) {
        try {
            List<User> repairmans = userService.getAllRepairmanByType(id);
            List<Message> messages = new ArrayList<>();
            for (User repairman : repairmans) {
                Message message = new Message();
                message.setUser(repairman);
                message.setContent("您的维修专长种类因为系统更新需要进行修改，请及时进入个人信息页面进行修改");
                message.setType(1);
                messages.add(message);
            }
            messageService.saveAllMessages(messages);
            repairTypeService.deleteById(id);
            return JsonResult.ok("删除成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "查询所有user 根据权限查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleString", value = "权限", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllUserByRole")
    @ResponseBody
    public JsonResult getAllUserByRole(PageVO pageVO, String roleString) {
        try {
            Role role = Role.valueOf(roleString);
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<User> list = userService.getAllByRole(role, pageable);
            List<User> data = list.getContent();

            if (Objects.equals(role.toString(), "REPAIRMAN")) {
                return JsonResult.ok(Entity2VO.entityList2VOList(data, RepairmanVO.class));
            } else {
                return JsonResult.ok(Entity2VO.entityList2VOList(data, UserVO.class));
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("查看指定用户信息")
    @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getUserById")
    @ResponseBody
    public JsonResult getUserById(Long id){
        try {
            User user=userService.getById(id);
            UserInfoVO userInfoVO = new UserInfoVO(user, avatarPathService.getByUserId(user.getId()));
            return JsonResult.ok(userInfoVO);
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("修改指定用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "指定用户id",required = true,dataType = "String", paramType = "query"),
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
            User user=userService.getById(userUpdateVO.getId());
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

    @ApiOperation(value = "查看待审核订单申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getPendingOrders")
    @ResponseBody
    public JsonResult getPendingOrders(PageVO pageVO) {
        try {
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Orders> list = ordersService.getByStatus(0, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "订单审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pend", value = "是否接受，0-拒绝，1-接受", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/pendOrderById")
    @ResponseBody
    public JsonResult pendOrderById(@MultiRequestBody Long id, @MultiRequestBody boolean pend) {
        try {
            if (ordersService.getById(id).getStatus() > 0) {
                return JsonResult.errorMsg("该订单已通过审核，请勿重复操作");
            }
            Orders order = ordersService.getById(id);
            OrderHistory orderHistory = orderHistoryService.getByOrderId(order.getId());
            int status;
            //信息集合体
            List<Message> messages = new ArrayList<>();
            Message customMessage = new Message();
            customMessage.setUser(userService.getById(order.getCustomId()));
            customMessage.setType(1);
            if (pend) {
                Date date = new Date();
                orderHistory.setPendDate(new Timestamp(date.getTime()));
                //判定是否指定用户
                if (order.getRepairman() != null) {
                    status = 2;
                    //如果是用户指定维修方，给维修方也发送一条信息
                    Message repairmanMessage = new Message();
                    repairmanMessage.setUser(order.getRepairman());
                    repairmanMessage.setContent("亲爱的" + repairmanMessage.getUser().getName() + "师傅，你已经被 " + order.getCustomName() + " 客户指定派发订单，请注意查看，可自行选择是否接手");
                    repairmanMessage.setType(2);
                    messages.add(repairmanMessage);
                } else {
                    status = 1;
                }
                //创建用户通知
                customMessage.setContent("亲爱的" + customMessage.getUser().getName() + "您的订单编号为：" + id + " 的订单已通过审核，请自行关注进度");
            } else {
                //admin选择了不通过审核
                status = -1;
                customMessage.setContent("亲爱的" + customMessage.getUser().getName() + "，您的订单编号为：" + id + " 的订单未通过审核，请重新编辑");
            }
            order.setStatus(status);
            orderHistory.setStatus(status);
            ordersService.update(order);
            orderHistoryService.update(orderHistory);
            messages.add(customMessage);
            messageService.saveAllMessages(messages);
            return JsonResult.ok("对于订单编号为：" + id + " 的订单" + pend + "审核操作已通过");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "修改订单指定维修方人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairmanId", value = "维修方id", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/changeRepairman")
    @ResponseBody
    public JsonResult changeRepairman(@MultiRequestBody Long orderId, @MultiRequestBody Long repairmanId) {
        try {
            Orders order = ordersService.getById(orderId);
            if (Objects.equals(order.getRepairman().getId(), repairmanId)) {
                return JsonResult.errorMsg("不能选择与原有维修人员相同的选项");
            }
            User repairman = userService.getById(repairmanId);
            //通过审核才有必要通知维修人员，还在审核的时候可以辅助修改
            if (order.getStatus() > 0) {
                List<Message> messages = new ArrayList<>();
                //通知被新选定的维修人员
                Message repairmanMessage = new Message();
                repairmanMessage.setUser(repairman);
                repairmanMessage.setContent("亲爱的" + repairman.getName() + "师傅，你已经被 " +
                        order.getCustomName() + " 客户指定派发订单，请注意查看，可自行选择是否接手");
                repairmanMessage.setType(2);
                messages.add(repairmanMessage);
                //通知被更改的维修人员
                Message neoRepairmanMessage = new Message();
                neoRepairmanMessage.setUser(userService.getById(order.getRepairman().getId()));
                neoRepairmanMessage.setContent("亲爱的" + neoRepairmanMessage.getUser().getName() + "师傅，非常抱歉，应客户要求，你已经被客户取消订单编号为：" +
                        order.getId() + "的编号，为您造成的不便敬请谅解");
                neoRepairmanMessage.setType(1);
                messages.add(neoRepairmanMessage);
                messageService.saveAllMessages(messages);
            }
            order.setRepairman(repairman);
            ordersService.update(order);
            return JsonResult.ok("修改成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "统计未读投诉")
    @GetMapping("/countComplaintByStatus")
    @ResponseBody
    public JsonResult getCountComplaintByStatus() {
        try {
            return JsonResult.ok(complaintService.countAllByStatus_False());
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "查看所有投诉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllComplaint")
    @ResponseBody
    public JsonResult getAllComplaint(PageVO pageVO) {
        try {
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.DESC, "id");
            Page<Complaint> list = complaintService.getAll(pageable);
            List<Complaint> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, ComplaintVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("查看投诉详情")
    @ApiImplicitParam(name = "id", value = "投诉id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getComplaintById")
    @ResponseBody
    public JsonResult getComplaintById(Long id) {
        try {
            return JsonResult.ok(complaintService.getById(id));
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
            List<Complaint> complaints = complaintService.getAllComplaintByUserIdAndComplaintStatus(user.getId());
            for(Complaint complaint : complaints){
                if(!complaint.isStatus()){
                    complaint.setStatus(true);
                }
            }
            complaintService.saveAllComplaints(complaints);
            return JsonResult.ok("全部已读");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

}
