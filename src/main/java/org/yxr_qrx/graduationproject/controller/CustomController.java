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

    @ApiOperation(value = "????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaCode", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detail", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "???????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "???????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "defaultStatus", value = "?????????????????? true-?????????false-??????", required = true, dataType = "String", paramType = "query")
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
            return JsonResult.ok("??????????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation("????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "areaCode", value = "??????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "detail", value = "????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "???????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "???????????????", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "defaultStatus", value = "?????????????????? true-?????????false-??????", dataType = "String", paramType = "query")
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
            return JsonResult.ok("????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("????????????")
    @ApiImplicitParam(name = "id", value = "????????????", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delAddress")
    @ResponseBody
    public JsonResult delAddress(@MultiRequestBody Long id) {
        try {
            if (addressService.isExist(id)) {
                addressService.delete(id);
                return JsonResult.ok("?????????????????????");
            } else {
                return JsonResult.errorMsg("?????????????????????");
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("????????????")
    @ApiImplicitParam(name = "id", value = "????????????", required = true, dataType = "String", paramType = "query")
    @PostMapping("/getAddress")
    @ResponseBody
    public JsonResult getAddress(@MultiRequestBody Long id) {
        try {
            return JsonResult.ok(addressService.getById(id));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

//    @ApiOperation("??????????????????")
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

    @ApiOperation("/????????????????????????")
    @ApiImplicitParam(name = "id", value = "??????????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation(value = "????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "??????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairTypeId", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "problem", value = "??????????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "repairmanId", value = "??????????????????", dataType = "String", paramType = "query")
    })
    @PostMapping("/createOrder")
    @ResponseBody
    public JsonResult createOrder(@RequestPart("json") OrderUpdateVO orderUpdateVO,
                                  @ApiParam(name = "images", value = "????????????") @RequestParam(value = "images", required = false) MultipartFile[] images) {
        try {
            User custom = (User) StpUtil.getSession().get("user");
            Orders order = new Orders();
            order.setProblem(orderUpdateVO.getProblem());
            order.setCustomId(custom.getId());
            //????????????
            Address address = addressService.getById(orderUpdateVO.getAddressId());
            order.setCustomName(address.getName());
            order.setAddress(address.toString());
            //areaCode??????
            String areaCode = address.getAreaCode();
            order.setProvince(areaCode.substring(0, 2));
            order.setCity(areaCode.substring(2, 4));
            order.setArea(areaCode.substring(4, 6));
            //????????????????????????
            order.setRepairType(repairTypeService.getById(orderUpdateVO.getRepairTypeId()).getName());
            if (orderUpdateVO.getRepairmanId() != null) {
                //?????????????????????
                order.setRepairman(userService.getById(orderUpdateVO.getRepairmanId()));
            }

            order = ordersService.create(order);
            //?????????????????????????????????
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setOrderId(order.getId());
            orderHistory.setCreateDate(order.getCreateDate());
            orderHistory = orderHistoryService.create(orderHistory);
            order.setOrderhistory(orderHistory);

            ordersService.update(order);
            //??????????????????????????????
            if (images != null) {
                int num = 0;
                for (MultipartFile image : images) {
                    //????????????????????????
                    String originalFilename = image.getOriginalFilename();
                    assert originalFilename != null;
                    String substring = originalFilename.substring(originalFilename.lastIndexOf(".") + 1, originalFilename.length());

                    //????????????????????????????????????
                    DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    Calendar calendar = Calendar.getInstance();
                    String newFileName = order.getId() + "_" + num + "_" + df.format(calendar.getTime()) + "." + substring;
                    //??????????????????????????????
                    JSONObject content = GiteeImgBedUtils.uploadImg("fault", newFileName, image);
                    if (content == null) {
                        return JsonResult.errorMsg("??????????????????");
                    }
                    ImgPath imgPath = new ImgPath();
                    //??????imgPath??????
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
        return JsonResult.ok("??????????????????");
    }

//    @ApiOperation(value = "??????????????????????????????")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation(value = "????????????")
    @ApiImplicitParam(name = "id", value = "????????????", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delOrder")
    @ResponseBody
    public JsonResult deleteOrder(@MultiRequestBody Long id) {
        try {
            Orders orders = ordersService.getById(id);
            //??????????????????????????????
            if (complaintService.isExistByOrderId(orders.getId())) {
                complaintService.deleteByOrderId(orders.getId());
            }
            //??????????????????????????????
            if (evaluationService.isExistByOrderId(orders.getId())) {
                evaluationService.deleteByOrderId(orders.getId());
            }
            //???????????????
            ordersService.deleteById(orders.getId());
            //?????????????????????????????????
            orderHistoryService.deleteByOrderId(orders.getId());
            //????????????????????????????????????????????????????????????????????????
            List<ImgPath> imgPaths = imgPathService.getAllByOrderId(orders.getId());
            if (imgPaths != null) {
                for (ImgPath imgPath : imgPaths) {
                    if (!GiteeImgBedUtils.delete(imgPath.getPath())) {
                        return JsonResult.errorMsg("??????????????????");
                    }
                }
                imgPathService.deleteByOrderId(orders.getId());
            }
            return JsonResult.ok("???????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "???????????????id??????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "0-????????????\n" +
                    "     1-?????????????????????????????????????????????????????????\n" +
                    "     2-??????????????????????????????????????????\n" +
                    "     3-????????????????????????\n" +
                    "     4-???????????????\n" +
                    "     -1 -???????????????", required = true, dataType = "String", paramType = "query")
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


    @ApiOperation(value = "?????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "?????????????????????5???\n" +
                    "      1??????\n" +
                    "      2??????\n" +
                    "      3??????\n" +
                    "      4??????\n" +
                    "      5??????", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/createEvaluation")
    @ResponseBody
    public JsonResult createEvaluation(@MultiRequestBody Long orderId, @MultiRequestBody String content, @MultiRequestBody int level) {
        try {
            Orders orders = ordersService.getById(orderId);
            if (orders.getStatus() != 5) {
                return JsonResult.errorMsg("??????????????????????????????????????????????????????");
            }
            if (evaluationService.isExistByOrderId(orderId)) {
                return JsonResult.errorMsg("???????????????????????????????????????????????????");
            }
            Evaluation evaluation = new Evaluation();
            evaluation.setOrder(orders);
            User custom = (User) StpUtil.getSession().get("user");
            if (!Objects.equals(custom.getId(), evaluation.getOrder().getCustomId())) {
                return JsonResult.errorMsg("??????????????????????????????????????????");
            }
            evaluation.setContent(content);
            evaluation.setLevel(level);
            evaluationService.create(evaluation);
            return JsonResult.ok("??????????????????");
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation(value = "????????????")
    @ApiImplicitParam(name = "id", value = "????????????", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delEvaluation")
    @ResponseBody
    public JsonResult delEvaluation(@MultiRequestBody Long id) {
        try {
            if (evaluationService.isExist(id)) {
                evaluationService.delete(id);
                return JsonResult.ok("?????????????????????");
            } else {
                return JsonResult.errorMsg("?????????????????????");
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation("????????????????????????")
    @GetMapping("/existComplaint")
    @ResponseBody
    public JsonResult existComplaint(Long id){
        try {
            return JsonResult.ok(complaintService.isExistByOrderId(id));
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "???????????????????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "????????????", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/createComplaint")
    @ResponseBody
    public JsonResult createComplaint(@MultiRequestBody Long orderId, @MultiRequestBody String content) {
        Orders order = ordersService.getById(orderId);
        if (complaintService.isExistByOrderId(orderId)) {
            return JsonResult.errorMsg("????????????????????????????????????????????????");
        }
        User custom = (User) StpUtil.getSession().get("user");
        if (Objects.equals(order.getCustomId(), custom.getId())) {
            Complaint complaint = new Complaint();
            complaint.setContent(content);
            complaint.setOrder(order);
            return JsonResult.ok(complaintService.create(complaint));
        } else {
            return JsonResult.errorMsg("????????????????????????????????????????????????????????????");
        }

    }

    @ApiOperation(value = "????????????")
    @ApiImplicitParam(name = "id", value = "???????????????", required = true, dataType = "String", paramType = "query")
    @DeleteMapping("/delComplaint")
    @ResponseBody
    public JsonResult deleteComplaint(@MultiRequestBody Long id) {
        try {
            if (complaintService.isExist(id)) {
                complaintService.delete(id);
                return JsonResult.ok("??????????????????");
            } else {
                return JsonResult.errorMsg("????????????????????????????????????????????????");
            }
        } catch (Exception e) {
            return JsonResult.errorException(e.getMessage());
        }
    }

    @ApiOperation(value = "????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation("?????????????????????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "repairmanId", value = "??????????????????", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "?????????(???1??????)", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "????????????", required = true, dataType = "String", paramType = "query")
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

    @ApiOperation("?????????????????????")
    @ApiImplicitParam(name = "orderId", value = "????????????", required = true, dataType = "String", paramType = "query")
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
