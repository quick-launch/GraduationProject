package org.yxr_qrx.graduationproject.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yxr_qrx.graduationproject.config.AliPayConfig;
import org.yxr_qrx.graduationproject.config.MultiRequestBody;
import org.yxr_qrx.graduationproject.entity.*;
import org.yxr_qrx.graduationproject.entity.VO.OrdersSearchVO;
import org.yxr_qrx.graduationproject.entity.VO.PageVO;
import org.yxr_qrx.graduationproject.entity.VO.OrderVO;
import org.yxr_qrx.graduationproject.service.MessageService;
import org.yxr_qrx.graduationproject.service.OrderHistoryService;
import org.yxr_qrx.graduationproject.service.OrdersService;
import org.yxr_qrx.graduationproject.service.UserService;
import org.yxr_qrx.graduationproject.utils.Entity2VO;
import org.yxr_qrx.graduationproject.utils.JsonResult;
import org.yxr_qrx.graduationproject.utils.QrCodeUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName:RepairmanController
 * @Author:41713
 * @Date 2021/11/10  15:18
 * @Version 1.0
 **/
@Controller
@RequestMapping("/repairman")
public class RepairmanController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrderHistoryService orderHistoryService;

    @ApiOperation(value = "修改维修专长")
    @ApiImplicitParam(name = "repairTypeId", value = "维修员专长id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/updateExpertise")
    @ResponseBody
    public JsonResult updateRepairType(Long repairTypeId) {
        try {
            User user= (User) StpUtil.getSession().get("user");
            user.getRepairType().setId(repairTypeId);
            userService.update(user);
            return JsonResult.ok("修改维修专长成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "浏览订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllOrder")
    @ResponseBody
    public JsonResult getAllOrder(PageVO pageVO) {
        try {
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Orders> list = ordersService.getByStatus(1, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "被指定订单数量通知")
    @GetMapping("/countAssignOrderById")
    @ResponseBody
    public JsonResult countAssignOrderById() {
        try {
            User repairman= (User) StpUtil.getSession().get("user");
            return JsonResult.ok("被" + ordersService.countAssignOrderById(repairman.getId()) + "条订单指定");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "浏览指定当前repairman订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAssignOrderById")
    @ResponseBody
    public JsonResult getAssignOrderById(PageVO pageVO) {
        try {
            User repairman= (User) StpUtil.getSession().get("user");
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Orders> list = ordersService.getAllByRepairmanIdAndStatus(repairman.getId(), 2, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "选择是否接手被指定订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pick", value = "是否接受，0-拒绝，1-接受", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/pickOrder")
    @ResponseBody
    public JsonResult pickOrder(@MultiRequestBody Long orderId, @MultiRequestBody boolean pick) {
        try {
            int status;
            Message message = new Message();
            Orders order = ordersService.getById(orderId);
            OrderHistory orderHistory = orderHistoryService.getByOrderId(orderId);
            if (pick) {
                status = 3;
                message.setContent("您订单编号为:" + orderId + " 的订单已被指定维修人员接手，请自行查看进度");
                Date date = new Date();
                orderHistory.setPickDate(new Timestamp(date.getTime()));
            } else {
                order.setRepairman(null);
                message.setContent("您订单编号为:" + orderId + " 的订单已被指定维修人员拒接，请耐心等待他人接手");
                status = 1;
            }
            order.setStatus(status);

            orderHistory.setStatus(status);
            orderHistoryService.update(orderHistory);

            ordersService.update(order);

            message.setType(1);
            message.setUser(userService.getById(order.getCustomId()));
            messageService.create(message);

            return JsonResult.ok("已对id为" + orderId + "的订单做出" + pick + "选择，请刷新页面");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "选择订单接手")
    @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "query")
    @PostMapping("/pickupOrder")
    @ResponseBody
    public JsonResult pickupOrder(@MultiRequestBody Long orderId) {
        Orders order = ordersService.getById(orderId);
        if (order.getRepairman() != null) {
            return JsonResult.errorMsg("该订单已被别的维修人员选中，非常抱歉");
        }
        OrderHistory orderHistory = orderHistoryService.getByOrderId(orderId);
        User repairman= (User) StpUtil.getSession().get("user");

        order.setRepairman(repairman);
        order.setStatus(3);
        ordersService.update(order);

        orderHistory.setStatus(3);
        Date date = new Date();
        orderHistory.setPickDate(new Timestamp(date.getTime()));
        orderHistoryService.update(orderHistory);

        Message message = new Message();
        message.setUser(userService.getById(order.getCustomId()));
        message.setType(1);
        message.setContent("您订单编号为:" + orderId + " 的订单已被指定维修人员接手，请自行查看进度");
        messageService.create(message);

        return JsonResult.ok("接手订单成功");
    }

    @ApiOperation(value = "根据状态浏览历史订单订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "订单状态，3-已接手，4-完成", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getHistoryOrders")
    @ResponseBody
    public JsonResult getHistoryOrders(int status, PageVO pageVO) {
        try {
            User repairman= (User) StpUtil.getSession().get("user");
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Orders> list = ordersService.getAllByRepairmanIdAndStatus(repairman.getId(), status, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "完成订单")
    @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "query")
    @PostMapping("/finishOrder")
    @ResponseBody
    public JsonResult finishOrder(@MultiRequestBody Long orderId) {
        try {
            Orders order = ordersService.getById(orderId);
            User repairman= (User) StpUtil.getSession().get("user");
            if (order.getStatus() != 3) {
                return JsonResult.errorMsg("当前订单还未接手，无法完成");
            }
            if (!Objects.equals(order.getRepairman().getId(), repairman.getId())) {
                return JsonResult.errorMsg("当前订单不属于你，无法完成");
            }
            order.setStatus(4);
            ordersService.update(order);

            OrderHistory orderHistory = orderHistoryService.getByOrderId(orderId);
            orderHistory.setStatus(4);
            Date date = new Date();
            orderHistory.setFinishDate(new Timestamp(date.getTime()));
            orderHistoryService.update(orderHistory);

            Message message = new Message();
            message.setUser(userService.getById(order.getCustomId()));
            message.setType(1);
            message.setContent("编号为：" + orderId + "的订单已完成");
            messageService.create(message);

            return JsonResult.ok("编号为：" + orderId + "的订单已完成");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("条件筛选订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "repairTypeId", value = "需求专长", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "城市", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "省份", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sequence", value = "升序-true/1 降序-false/0", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllOrderByRequirements")
    @ResponseBody
    public JsonResult getAllOrderByRequirements(OrdersSearchVO ordersSearchVO,
                                                PageVO pageVO) {
        try {
            Pageable pageable=null;
            if (ordersSearchVO.isSequence()) {
                pageable= PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC,"id");
            } else {
                pageable= PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.DESC,"id");
            }
            String province=null;
            String city=null;
            String area=null;
            String areaCode= ordersSearchVO.getAreaCode();
            if (!areaCode.isBlank()){
                province=areaCode.substring(0,2);
                city=areaCode.substring(2,4);
                area=areaCode.substring(4,6);
            }

            Page<Orders> list = ordersService.getAllOrderByRequirements(ordersSearchVO.getRepairType(),province,city,area, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("支付订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "total_amount", value = "金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order_id", value = "订单编号", dataType = "String", paramType = "query")
    })
    @PostMapping("/payForOrder")
    @ResponseBody
    public JsonResult pay(double total_amount,Long order_id) throws AlipayApiException {
        try {
            Orders order=ordersService.getById(order_id);
            if (order.getStatus()!=4){
                return JsonResult.errorMsg("该订单处于不可支付状态");
            }

            AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.gatewayUrl,
                    AliPayConfig.app_id,
                    AliPayConfig.merchant_private_key,
                    "json",
                    AliPayConfig.charset,
                    AliPayConfig.alipay_public_key,
                    AliPayConfig.sign_type);
            //设置请求参数
            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            alipayRequest.setNotifyUrl(AliPayConfig.notify_url);
            //订单标题
            String subject = "支付宝扫码测试";
            String body = "仅供测试使用";
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order_id);
            bizContent.put("total_amount", total_amount);
            bizContent.put("subject", "测试商品");
            alipayRequest.setBizContent(bizContent.toString());
            AlipayTradePrecreateResponse response = alipayClient.execute(alipayRequest);

            if(response.isSuccess()){
                order.setStatus(5);
                ordersService.update(order);
                return  JsonResult.ok(QrCodeUtil.createBase64QrCode(response.getQrCode()));
            } else {
                return JsonResult.errorMsg(response.toString());
            }
        } catch (AlipayApiException e) {
            return JsonResult.errorException(e.getMessage());
        }
    }


}
