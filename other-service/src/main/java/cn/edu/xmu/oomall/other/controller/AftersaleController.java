package cn.edu.xmu.oomall.other.controller;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.oomall.other.model.vo.Aftersale.*;
import cn.edu.xmu.oomall.other.service.AftersaleService;
import cn.edu.xmu.oomall.other.util.PageInfoHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * @author XQChen
 * @version 创建时间：2020/12/10 上午10:07
 */
@RestController /*Restful的Controller对象*/
@RequestMapping(produces = "application/json;charset=UTF-8")
public class AftersaleController {
    private static final Logger logger = LoggerFactory.getLogger(AftersaleController.class);

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private AftersaleService aftersaleService;

    @ApiOperation(value = "获得售后单的所有状态", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "用户token", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @GetMapping("/aftersales/states")
    public Object getAftersaleAllStates() {
        logger.debug("getAfterSaleAllStates");

        return ResponseUtil.ok(aftersaleService.getAftersaleAllStates());
    }

    @ApiOperation(value = "买家提交售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",      name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",     name = "id",            value = "订单明细id",   required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleVo", name = "body",          value = "售后服务信息", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PostMapping("/orderitems/{id}/aftersales")
    public Object createAftersale(@LoginUser Long userId, @Validated @RequestBody AftersaleVo vo, BindingResult bindingResult, @PathVariable("id") Long orderItemId) {
        logger.debug("hereherheere");
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }

        Object retObject = aftersaleService.createAftersale(vo, orderItemId, userId);

        if(retObject.equals(ResponseCode.RESOURCE_ID_NOTEXIST))
        {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail((ResponseCode) retObject);
        }
        if(retObject.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)) {
            logger.debug("outscope");
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseUtil.fail((ResponseCode) object);
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_CREATED);
        if(retObject.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(retObject);
        }
    }

    @ApiOperation(value = "买家查询所有的售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "beginTime",     value = "开始时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "endTime",       value = "结束时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "type",          value = "售后类型"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "state",         value = "售后状态")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/aftersales")
    public Object getAllAftersale(
            @LoginUser Long userId,
            @RequestParam(required = false) String beginTimeStr,
            @RequestParam(required = false) String endTimeStr,
            @RequestParam(required = false, defaultValue = "1") @Min(1) Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(1) Integer pageSize,
            @RequestParam(required = false) @Min(0) @Max(2) Integer type,
            @RequestParam(required = false) @Min(0) @Max(9) Integer state) {
        LocalDateTime beginTime = null, endTime = null;
        try{
            logger.debug("beginTime:"+beginTimeStr+"endTime:"+endTimeStr);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss");
            if(beginTimeStr != null) beginTime = LocalDateTime.parse(beginTimeStr, dateTimeFormatter);
            if(endTimeStr != null) endTime = LocalDateTime.parse(endTimeStr,dateTimeFormatter);
        }
        catch (Exception ex){
            logger.debug("时间格式错误");
            PageInfo pg=new PageInfo(new ArrayList());
            pg.setPageSize(pageSize);
            pg.setPageNum(page);
            return ResponseUtil.ok(PageInfoHelper.process(pg));
        }

        return ResponseUtil.ok(PageInfoHelper.process(aftersaleService.getAllAftersales(userId, null, beginTime, endTime, page, pageSize, type, state)));
    }

    @ApiOperation(value = "管理员查询所有的售后单", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "beginTime",     value = "开始时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "String",  name = "endTime",       value = "结束时间"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "page",          value = "页码"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "pageSize",      value = "每页数目"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "type",          value = "售后类型"),
            @ApiImplicitParam(paramType = "query",  dataType = "Integer", name = "state",         value = "售后状态")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{id}/aftersales")
    public Object adminGetAllAftersale(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("id") Long shopId,
            @RequestParam(required = false) String beginTimeStr,
            @RequestParam(required = false) String endTimeStr,
            @RequestParam(required = false, defaultValue = "1") @Min(1) @NotNull Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(1) @NotNull Integer pageSize,
            @RequestParam(required = false) @Min(0) @Max(2) @NotNull Integer type,
            @RequestParam(required = false) @Min(0) @Max(2) @NotNull Integer state) {
        LocalDateTime beginTime=null,endTime=null;
        try{
            logger.debug("beginTime:"+beginTimeStr+"endTime:"+endTimeStr);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            if(beginTimeStr!=null) beginTime = LocalDateTime.parse(beginTimeStr, dateTimeFormatter);
            if(endTimeStr!=null) endTime = LocalDateTime.parse(endTimeStr,dateTimeFormatter);
        }
        catch (Exception ex){
            logger.debug("时间格式错误");
            PageInfo pg=new PageInfo(new ArrayList());
            pg.setPageSize(pageSize);
            pg.setPageNum(page);
            return ResponseUtil.ok(PageInfoHelper.process(pg));
        }

        return ResponseUtil.ok(PageInfoHelper.process(aftersaleService.getAllAftersales(userId, shopId, beginTime, endTime, page, pageSize, type, state)));
    }

    @ApiOperation(value = "买家根据售后单id查询售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",  name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer", name = "id",         value = "售后单id")
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/aftersales/{id}")
    public Object getAftersaleById(@LoginUser Long userId, @PathVariable("id") Long aftersaleId) {
        logger.debug("getAftersaleById");
        Object object = aftersaleService.getAftersaleById(userId, aftersaleId);

        logger.debug("object: " + object);

        if(object.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail((ResponseCode) object);
        }
        if(object.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)) {
            logger.debug("outscope");
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseUtil.fail((ResponseCode) object);
        }
        if(object.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(object);
        }
    }

    @ApiOperation(value = "买家修改售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",            name = "authorization", value = "用户token",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",           name = "id",            value = "售后单id",     required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleModifyVo", name = "body", value = "买家可修改的信息：地址，售后商品的数量，申请售后的原因，联系人以及联系电话", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}")
    public Object modifyAftersaleById(@LoginUser Long userId, @PathVariable("id") Long aftersaleId, @Validated @RequestBody AftersaleModifyVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }

        logger.debug(vo.toString());

        ResponseCode responseCode = aftersaleService.modifyAftersaleById(userId, aftersaleId, vo);

        logger.debug(responseCode.getMessage());

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(responseCode);
        }
    }

    @ApiOperation(value = "买家填写售后的运单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",              name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",             name = "id",            value = "售后单id",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @DeleteMapping("/aftersales/{id}")
    public Object deleteAftersaleById(@LoginUser Long userId, @PathVariable("id") Long id) {
        ResponseCode responseCode = aftersaleService.deleteAftersaleById(userId, id);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(responseCode);
        }
    }

    @ApiOperation(value = "买家填写售后的运单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",              name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",             name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleSendbackVo", name = "body",          value = "运单号",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}/sendback")
    public Object addWayBillNumber(@LoginUser Long userId, @PathVariable("id") Long id, @Validated @RequestBody AftersaleSendbackVo vo, BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }

        ResponseCode responseCode = aftersaleService.addWayBillNumber(userId, id, vo.getLogSn());

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(responseCode);
        }
    }

    @ApiOperation(value = "买家确认售后单结束", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/aftersales/{id}/confirm")
    public Object confirmAftersaleEnd(@LoginUser Long userId, @PathVariable("id") Long id) {
        ResponseCode responseCode = aftersaleService.confirmAftersaleEnd(userId, id);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.fail(responseCode);
        }
    }

    @ApiOperation(value = "管理员根据售后单id查询售后单信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @GetMapping("/shops/{shopId}/aftersales/{id}")
    public Object adminGetAftersaleById(@LoginUser Long userId, @Depart Long did, @PathVariable("shopId") Long shopId, @PathVariable("id") Long id) {
        Object object = aftersaleService.adminGetAftersaleById(shopId, id);

        logger.debug("object: " + object);

        if(object.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail((ResponseCode) object);
        }
        if(object.equals(ResponseCode.RESOURCE_ID_OUTSCOPE)) {
            logger.debug("outscope");
            httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return ResponseUtil.fail((ResponseCode) object);
        }
        if(object.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(object);
        }
    }

    @ApiOperation(value = "管理员同意/不同意（退款，换货，维修）", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleConfirmVo", name = "body",          value = "处理意见",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/confirm")
    public Object adminConfirmAftersale(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @RequestBody AftersaleConfirmVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }

        ResponseCode responseCode = aftersaleService.adminConfirm(id, shopId, vo);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(responseCode);
        }
    }

    @ApiOperation(value = "店家确认收到买家的退（换）货", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleReceiveVo", name = "body",          value = "处理意见",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/receive")
    public Object adminReceive(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Validated @RequestBody AftersaleReceiveVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }

        ResponseCode responseCode = aftersaleService.adminReceive(id, shopId, vo);

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.ok(responseCode);
        }
    }

    @ApiOperation(value = "店家寄出维修好的货物", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String",             name = "authorization", value = "用户token", required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "shopId",        value = "店铺id",    required = true),
            @ApiImplicitParam(paramType = "path",   dataType = "Integer",            name = "id",            value = "售后单id",  required = true),
            @ApiImplicitParam(paramType = "body",   dataType = "AftersaleDeliverVo", name = "body",          value = "运单号",    required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0,   message = "成功")
    })
    @Audit
    @PutMapping("/shops/{shopId}/aftersales/{id}/deliver")
    public Object adminDeliver(
            @LoginUser Long userId,
            @Depart Long did,
            @PathVariable("shopId") Long shopId,
            @PathVariable("id") Long id,
            @Validated @RequestBody(required = false) AftersaleDeliverVo vo,
            BindingResult bindingResult) {
        Object object = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(null != object) {
            logger.debug("Validate failed");
            logger.debug("UserSignUpVo:" + vo);

            return ResponseUtil.fail(ResponseCode.FIELD_NOTVALID);
        }

        ResponseCode responseCode = aftersaleService.adminDeliver(id, shopId, vo == null ? "" : vo.getShopLogSn());

        if(responseCode.equals(ResponseCode.RESOURCE_ID_NOTEXIST)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ResponseUtil.fail(responseCode);
        }
        if(responseCode.equals(ResponseCode.OK)) {
            return ResponseUtil.ok();
        }
        else {
            return ResponseUtil.fail(responseCode);
        }
    }

    @GetMapping("/test")
    public void test() {
        aftersaleService.test();
    }
}
