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

    @ApiOperation(value = "??????????????????")
    @ApiImplicitParam(name = "repairTypeId", value = "???????????????id", required = true, dataType = "String", paramType = "query")
    @PostMapping("/updateExpertise")
    @ResponseBody
    public JsonResult updateRepairType(Long repairTypeId) {
        try {
            User user= (User) StpUtil.getSession().get("user");
            user.getRepairType().setId(repairTypeId);
            userService.update(user);
            return JsonResult.ok("????????????????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation(value = "???????????????????????????")
    @GetMapping("/countAssignOrderById")
    @ResponseBody
    public JsonResult countAssignOrderById() {
        try {
            User repairman= (User) StpUtil.getSession().get("user");
            return JsonResult.ok("???" + ordersService.countAssignOrderById(repairman.getId()) + "???????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "??????????????????repairman??????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation(value = "?????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pick", value = "???????????????0-?????????1-??????", required = true, dataType = "String", paramType = "query")
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
                message.setContent("??????????????????:" + orderId + " ???????????????????????????????????????????????????????????????");
                Date date = new Date();
                orderHistory.setPickDate(new Timestamp(date.getTime()));
            } else {
                order.setRepairman(null);
                message.setContent("??????????????????:" + orderId + " ?????????????????????????????????????????????????????????????????????");
                status = 1;
            }
            order.setStatus(status);

            orderHistory.setStatus(status);
            orderHistoryService.update(orderHistory);

            ordersService.update(order);

            message.setType(1);
            message.setUser(userService.getById(order.getCustomId()));
            messageService.create(message);

            return JsonResult.ok("??????id???" + orderId + "???????????????" + pick + "????????????????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "??????????????????")
    @ApiImplicitParam(name = "orderId", value = "????????????", required = true, dataType = "String", paramType = "query")
    @PostMapping("/pickupOrder")
    @ResponseBody
    public JsonResult pickupOrder(@MultiRequestBody Long orderId) {
        Orders order = ordersService.getById(orderId);
        if (order.getRepairman() != null) {
            return JsonResult.errorMsg("??????????????????????????????????????????????????????");
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
        message.setContent("??????????????????:" + orderId + " ???????????????????????????????????????????????????????????????");
        messageService.create(message);

        return JsonResult.ok("??????????????????");
    }

    @ApiOperation(value = "????????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "???????????????3-????????????4-??????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation(value = "????????????")
    @ApiImplicitParam(name = "orderId", value = "????????????", required = true, dataType = "String", paramType = "query")
    @PostMapping("/finishOrder")
    @ResponseBody
    public JsonResult finishOrder(@MultiRequestBody Long orderId) {
        try {
            Orders order = ordersService.getById(orderId);
            User repairman= (User) StpUtil.getSession().get("user");
            if (order.getStatus() != 3) {
                return JsonResult.errorMsg("???????????????????????????????????????");
            }
            if (!Objects.equals(order.getRepairman().getId(), repairman.getId())) {
                return JsonResult.errorMsg("???????????????????????????????????????");
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
            message.setContent("????????????" + orderId + "??????????????????");
            messageService.create(message);

            return JsonResult.ok("????????????" + orderId + "??????????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "repairTypeId", value = "????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "city", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "province", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sequence", value = "??????-true/1 ??????-false/0", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation("????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "total_amount", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "order_id", value = "????????????", dataType = "String", paramType = "query")
    })
    @PostMapping("/payForOrder")
    @ResponseBody
    public JsonResult pay(double total_amount,Long order_id) throws AlipayApiException {
        try {
            Orders order=ordersService.getById(order_id);
            if (order.getStatus()!=4){
                return JsonResult.errorMsg("?????????????????????????????????");
            }

            AlipayClient alipayClient = new DefaultAlipayClient(AliPayConfig.gatewayUrl,
                    AliPayConfig.app_id,
                    AliPayConfig.merchant_private_key,
                    "json",
                    AliPayConfig.charset,
                    AliPayConfig.alipay_public_key,
                    AliPayConfig.sign_type);
            //??????????????????
            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            alipayRequest.setNotifyUrl(AliPayConfig.notify_url);
            //????????????
            String subject = "?????????????????????";
            String body = "??????????????????";
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", order_id);
            bizContent.put("total_amount", total_amount);
            bizContent.put("subject", "????????????");
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
