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
import org.yxr_qrx.graduationproject.entity.VO.OrderUpdateVO;
import org.yxr_qrx.graduationproject.entity.VO.PageVO;
import org.yxr_qrx.graduationproject.entity.VO.ComplaintVO;
import org.yxr_qrx.graduationproject.entity.VO.OrderVO;
import org.yxr_qrx.graduationproject.entity.VO.RepairmanVO;
import org.yxr_qrx.graduationproject.service.*;
import org.yxr_qrx.graduationproject.utils.Entity2VO;
import org.yxr_qrx.graduationproject.utils.GiteeImgBedUtils;
import org.yxr_qrx.graduationproject.utils.JsonResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:CustomController
 * @Author:41713
 * @Date 2021/11/9  16:39
 * @Version 1.0
 **/
@Controller
@RequestMapping("/custom")
public class CustomController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private ImgPathService imgPathService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderHistoryService orderHistoryService;
    @Autowired
    private RepairTypeService repairTypeService;

    @ApiOperation(value = "添加地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detail", value = "具体地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "联系人电话", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "联系人姓名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "defaultStatus", value = "地址默认状态 true-默认，false-其他", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/createAddress")
    @ResponseBody
    public JsonResult createAddress(@MultiRequestBody Address address) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            address.setCustomId(custom.getId());
            if (address.isDefaultStatus()) {
                Address defaultAddress = addressService.getByCustomIdAndDefaultStatus(custom.getId(), true);
                if (defaultAddress != null) {
                    defaultAddress.setDefaultStatus(false);
                    addressService.update(defaultAddress);
                }
            }
            addressService.create(address);
            return JsonResult.ok("保存地址成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("浏览已保存地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getAllAddress")
    @ResponseBody
    public JsonResult getAllAddress(PageVO pageVO) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Address> list = addressService.getAllByCustomId(custom.getId(), pageable);
            List<Address> data = list.getContent();
            return JsonResult.ok(data);
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("修改地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "areaCode", value = "编码", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detail", value = "具体地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "联系人电话", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "联系人姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "defaultStatus", value = "地址默认状态 true-默认，false-其他", dataType = "String", paramType = "query")
    })
    @PostMapping("/updateAddress")
    @ResponseBody
    public JsonResult updateAddress(@RequestBody Address address) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            address.setCustomId(custom.getId());
            if (address.isDefaultStatus()) {
                Address defaultAddress = addressService.getByCustomIdAndDefaultStatus(custom.getId(), true);
                if (defaultAddress != null) {
                    defaultAddress.setDefaultStatus(false);
                    addressService.update(defaultAddress);
                }
            }
            addressService.update(address);
            return JsonResult.ok("修改成功");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("删除地址")
    @ApiImplicitParam(name = "id", value = "地址编号", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delAddress")
    @ResponseBody
    public JsonResult delAddress(@MultiRequestBody Long id) {
        try {
            if (addressService.isExist(id)) {
                addressService.delete(id);
                return JsonResult.ok("指定地址已删除");
            } else {
                return JsonResult.errorMsg("指定地址不存在");
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("获取地址")
    @ApiImplicitParam(name = "id", value = "地址编号", required = true, dataType = "String", paramType = "query")
    @PostMapping("/getAddress")
    @ResponseBody
    public JsonResult getAddress(@MultiRequestBody Long id) {
        try {
            return JsonResult.ok(addressService.getById(id));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

//    @ApiOperation("获取默认地址")
//    @GetMapping("/getDefaultAddress")
//    @ResponseBody
//    public JsonResult getDefaultAddress() {
//        try {
//            User custom = (User) StpUtil.getSession().get("user");
//            Address address = addressService.getByCustomIdAndDefaultStatus(custom.getId(), true);
//            return JsonResult.ok(address);
//        } catch (Exception e) {
//            return JsonResult.errorException(e.getMessage());
//        }
//    }

    @ApiOperation("/获取维修人员列表")
    @ApiImplicitParam(name = "id", value = "维修种类编号", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getRepairman")
    @ResponseBody
    public JsonResult getRepairmans(Long id) {
        try {
            List<User> repairmans = userService.getAllRepairmanByType(id);
            return JsonResult.ok(Entity2VO.entityList2VOList(repairmans, RepairmanVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "申请订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "地址", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairTypeId", value = "需求专长", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "problem", value = "故障具体描述", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairmanId", value = "维修人员编号", dataType = "String", paramType = "query")
    })
    @PostMapping("/createOrder")
    @ResponseBody
    public JsonResult createOrder(@RequestPart("json") OrderUpdateVO orderUpdateVO,
                                  @ApiParam(name = "images", value = "故障图片") @RequestParam(value = "images", required = false) MultipartFile[] images) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            Orders order = new Orders();
            order.setProblem(orderUpdateVO.getProblem());
            order.setCustomId(custom.getId());
            //地址填充
            Address address = addressService.getById(orderUpdateVO.getAddressId());
            order.setCustomName(address.getName());
            order.setAddress(address.toString());
            //areaCode分割
            String areaCode = address.getAreaCode();
            order.setProvince(areaCode.substring(0, 2));
            order.setCity(areaCode.substring(2, 4));
            order.setArea(areaCode.substring(4, 6));
            //获取维修专长种类
            order.setRepairType(repairTypeService.getById(orderUpdateVO.getRepairTypeId()).getName());
            if (orderUpdateVO.getRepairmanId() != null) {
                //获取指定维修方
                order.setRepairman(userService.getById(orderUpdateVO.getRepairmanId()));
            }

            order = ordersService.create(order);
            //新建对应订单的进程记录
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrderId(order.getId());
            orderHistory.setCreateDate(order.getCreateDate());
            orderHistory = orderHistoryService.create(orderHistory);
            order.setOrderhistory(orderHistory);

            ordersService.update(order);
            //判断是否需要存储文件
            if (images != null) {
                int num = 0;
                for (MultipartFile image : images) {
                    //原文件扩展名获取
                    String originalFilename = image.getOriginalFilename();
                    assert originalFilename != null;
                    String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());

                    //新文件名根据创建时间生成
                    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    Calendar calendar = Calendar.getInstance();
                    String newFileName = order.getId() + "_" + num + "_" + df.format(calendar.getTime()) + "." + substring;
                    //创建与更新新头像路径
                    JSONObject content = GiteeImgBedUtils.uploadImg("fault", newFileName, image);
                    if (content == null) {
                        return JsonResult.errorMsg("网络请求失败");
                    }
                    ImgPath imgPath = new ImgPath();
                    //创建imgPath存储
                    imgPath.setPath(String.valueOf(content.getObj("path")));
                    imgPath.setDownloadUrl(String.valueOf(content.getObj("download_url")));
                    imgPath.setOrderId(order.getId());
                    imgPathService.create(imgPath);
                    num++;
                }
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
        return JsonResult.ok("订单上传成功");
    }

//    @ApiOperation(value = "查看自己所有历史订单")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
//    })
//    @GetMapping("/getAllOrder")
//    @ResponseBody
//    public JsonResult getAllOrder(PageVO pageVO) {
//        try {
//            User custom = (User) StpUtil.getSession().get("user");
//            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
//            Page<Orders> list = ordersService.getAllByCustomId(custom.getId(), pageable);
//            List<Orders> data = list.getContent();
//            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
//        } catch (Exception e) {
//            return JsonResult.errorException(e.getMessage());
//        }
//    }

    @ApiOperation(value = "删除订单")
    @ApiImplicitParam(name = "id", value = "订单编号", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delOrder")
    @ResponseBody
    public JsonResult deleteOrder(@MultiRequestBody Long id) {
        try {
            Orders orders = ordersService.getById(id);
            //删除与该订单关联投诉
            if (complaintService.isExistByOrderId(orders.getId())) {
                complaintService.deleteByOrderId(orders.getId());
            }
            //删除与该订单关联评论
            if (evaluationService.isExistByOrderId(orders.getId())) {
                evaluationService.deleteByOrderId(orders.getId());
            }
            //删除该订单
            ordersService.deleteById(orders.getId());
            //删除与该订单关进程记录
            orderHistoryService.deleteByOrderId(orders.getId());
            //如果该订单存储图片不为空，删除图片与图片路径记录
            List<ImgPath> imgPaths = imgPathService.getAllByOrderId(orders.getId());
            if (imgPaths != null) {
                for (ImgPath imgPath : imgPaths) {
                    if (!GiteeImgBedUtils.delete(imgPath.getPath())) {
                        return JsonResult.errorMsg("删除文件失败");
                    }
                }
                imgPathService.deleteByOrderId(orders.getId());
            }
            return JsonResult.ok("订单已删除");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "查看该用户id的订单，根据状态筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "0-等待审核\n" +
                    "     1-通过审核等待接手或者被指定维修人员拒接\n" +
                    "     2-已通过审核待指定维修人员接手\n" +
                    "     3-已被维修人员接手\n" +
                    "     4-已完成订单\n" +
                    "     -1 -未通过审核", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getOrdersByStatus")
    @ResponseBody
    public JsonResult getOrdersByStatus(PageVO pageVO, int status) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Orders> list = ordersService.getAllByCustomIdAndStatus(custom.getId(), status, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }


    @ApiOperation(value = "评价已完成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "评价内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "评价等级，一共5级\n" +
                    "      1极差\n" +
                    "      2较差\n" +
                    "      3一般\n" +
                    "      4较好\n" +
                    "      5极好", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/createEvaluation")
    @ResponseBody
    public JsonResult createEvaluation(@MultiRequestBody Long orderId, @MultiRequestBody String content, @MultiRequestBody int level) {
        try {
            Orders orders = ordersService.getById(orderId);
            if (orders.getStatus() != 5) {
                return JsonResult.errorMsg("该订单尚未支付，请完成支付后再来评论");
            }
            if (evaluationService.isExistByOrderId(orderId)) {
                return JsonResult.errorMsg("该订单已被订单发起人评价，无法继续");
            }
            Evaluation evaluation = new Evaluation();
            evaluation.setOrder(orders);
            User custom = (User) StpUtil.getSession().get("user");
            if (!Objects.equals(custom.getId(), evaluation.getOrder().getCustomId())) {
                return JsonResult.errorMsg("你不是该订单的用户，无法评价");
            }
            evaluation.setContent(content);
            evaluation.setLevel(level);
            evaluationService.create(evaluation);
            return JsonResult.ok("订单评价完成");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "查看自己所有评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getHistoryEvaluation")
    @ResponseBody
    public JsonResult getHistoryEvaluation(PageVO pageVO) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "evaluation.id");
            Page<Evaluation> list = evaluationService.getAllByCustomId(custom.getId(), pageable);
            List<Evaluation> data = list.getContent();
            return JsonResult.ok(data);
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "删除评价")
    @ApiImplicitParam(name = "id", value = "评论编号", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delEvaluation")
    @ResponseBody
    public JsonResult delEvaluation(@MultiRequestBody Long id) {
        try {
            if (evaluationService.isExist(id)) {
                evaluationService.delete(id);
                return JsonResult.ok("指定评论已删除");
            } else {
                return JsonResult.errorMsg("指定评论不存在");
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("确认是否存在投诉")
    @GetMapping("/existComplaint")
    @ResponseBody
    public JsonResult existComplaint(Long id){
        try {
            return JsonResult.ok(complaintService.isExistByOrderId(id));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "订单投诉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "投诉单关联订单编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "投诉内容", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/createComplaint")
    @ResponseBody
    public JsonResult createComplaint(@MultiRequestBody Long orderId, @MultiRequestBody String content) {
        Orders order = ordersService.getById(orderId);
        if (complaintService.isExistByOrderId(orderId)) {
            return JsonResult.errorMsg("已经存在投诉，请耐心等待官方回复");
        }
        User custom = (User) StpUtil.getSession().get("user");
        if (Objects.equals(order.getCustomId(), custom.getId())) {
            Complaint complaint = new Complaint();
            complaint.setContent(content);
            complaint.setOrder(order);
            return JsonResult.ok(complaintService.create(complaint));
        } else {
            return JsonResult.errorMsg("您不属于该订单发布用户，无权进行相关操作");
        }

    }

    @ApiOperation(value = "撤回投诉")
    @ApiImplicitParam(name = "id", value = "投诉单编号", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delComplaint")
    @ResponseBody
    public JsonResult deleteComplaint(@MultiRequestBody Long id) {
        try {
            if (complaintService.isExist(id)) {
                complaintService.delete(id);
                return JsonResult.ok("撤回投诉成功");
            } else {
                return JsonResult.errorMsg("不存在需求撤回的投诉，请重新确认");
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "查看自己所有投诉")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getHistoryComplaint")
    @ResponseBody
    public JsonResult getHistoryComplaint(PageVO pageVO) {
        User custom = (User) StpUtil.getSession().get("user");
        Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "complaint.id");
        Page<Complaint> list = complaintService.getAllByCustomId(custom.getId(), pageable);
        List<Complaint> data = list.getContent();
        return JsonResult.ok(Entity2VO.entityList2VOList(data, ComplaintVO.class));
    }

    @ApiOperation("查看该维修人员历史订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "repairmanId", value = "维修人员编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "当前页(从1开始)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/getOrdersByRepairmanId")
    @ResponseBody
    public JsonResult getOrdersByRepairmanId(Long repairmanId, PageVO pageVO) {
        try {
            Pageable pageable = PageRequest.of(pageVO.getPage() - 1, pageVO.getPageSize(), Sort.Direction.ASC, "id");
            Page<Orders> list = ordersService.getAllByRepairmanIdAndStatus(repairmanId, 4, pageable);
            List<Orders> data = list.getContent();
            return JsonResult.ok(Entity2VO.entityList2VOList(data, OrderVO.class));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("查看该订单评价")
    @ApiImplicitParam(name = "orderId", value = "订单编号", required = true, dataType = "String", paramType = "query")
    @GetMapping("/getEvaluation")
    @ResponseBody
    public JsonResult getEvaluation(Long orderId) {
        try {
            return JsonResult.ok(evaluationService.getByOrderId(orderId));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }
}
