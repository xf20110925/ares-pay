package com.ptb.pay.api.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PaymentMethodEnum;
import com.ptb.common.enums.RechargeOrderInvoiceStatusEnum;
import com.ptb.common.errorcode.CommonErrorCode;
import com.ptb.common.vo.ResponseVo;
import com.ptb.pay.api.IRechargeOrderApi;
import com.ptb.pay.conf.payment.OfflinePaymentConfig;
import com.ptb.pay.enums.RechargeOrderLogActionTypeEnum;
import com.ptb.pay.mapper.impl.RechargeFailedLogMapper;
import com.ptb.pay.mapper.impl.RechargeOrderLogMapper;
import com.ptb.pay.mapper.impl.RechargeOrderMapper;
import com.ptb.pay.model.RechargeFailedLog;
import com.ptb.pay.model.RechargeFailedLogExample;
import com.ptb.pay.model.RechargeOrder;
import com.ptb.pay.model.RechargeOrderExample;
import com.ptb.pay.service.factory.RechargeOrderServiceFactory;
import com.ptb.pay.service.interfaces.*;
import com.ptb.pay.vo.recharge.*;
import com.ptb.service.api.IBaiduPushApi;
import com.ptb.utils.service.ReturnUtil;
import com.ptb.utils.tool.ChangeMoneyUtil;
import enums.MessageTypeEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import vo.param.PushMessageParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 充值订单API实现
 * All Rights Reserved.
 *
 * @version 1.0  2016-11-15 10:35 by wgh（guanhua.wang@pintuibao.cn）创建
 */
@Component("rechargeOrderApi")
public class RechargeOrderApiImpl implements IRechargeOrderApi {

    private static Logger logger = LoggerFactory.getLogger(RechargeOrderApiImpl.class);

    @Autowired
    private RechargeOrderMapper rechargeOrderMapper;
    @Autowired
    private IPaymentService paymentService;
    @Resource
    private IBaiduPushApi baiduPushApi;
    @Resource(name = "offlinePaymentService")
    private IOfflinePaymentService offlinePaymentService;
    @Autowired
    private IRechargeOrderLogService rechargeOrderLogService;
    @Autowired
    private RechargeFailedLogMapper rechargeFailedLogMapper;
    @Autowired
    private IFailedRechargeOrderService failedRechargeOrderService;

    @Override
    public ResponseVo<Map<String, Object>> createRechargeOrder(RechargeOrderParamsVO paramsVO) throws Exception {
        IRechargeOrderService rechargeOrderService = RechargeOrderServiceFactory.createService(paramsVO.getPayMethod());
        RechargeOrder rechargeOrder = rechargeOrderService.createRechargeOrder(paramsVO);
        if (rechargeOrder == null) {
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
        }
        if (PaymentMethodEnum.offline.getPaymentMethod() == paramsVO.getPayMethod()) {
            //推送消息
            try {
                PushMessageParam param = new PushMessageParam();
                param.setUserId(paramsVO.getUserId());
                param.setDeviceType(DeviceTypeEnum.getDeviceTypeEnum(paramsVO.getDeviceType()));
                param.setTitle("审核中（线下打款）");
                param.setMessage("提交成功，充值金额" + ChangeMoneyUtil.fromFenToYuan(rechargeOrder.getTotalAmount()) + "元，请尽快给平台账户进行打款，便于系统审核");
                param.setMessageType(MessageTypeEnum.OFFLINE_RECHARGE.getMessageType());
                Map<String, Object> keyMap = new HashMap<>();
                keyMap.put("id", rechargeOrder.getPtbRechargeOrderId());
                param.setContentParam(keyMap);
                param.setNeedPushMessage(false);
                param.setNeedSaveMessage(true);
                baiduPushApi.pushMessage(param);
            } catch (Exception e) {
                logger.error("线下打款消息推送失败。errorMsg:{}", e.getMessage());
            }
        }
        return ReturnUtil.success(rechargeOrderService.getReturnData(rechargeOrder));
    }

    @Override
    public ResponseVo<Object> getRechargeOrderListByPage(int pageNum, int pageSize, RechargeOrderQueryVO rechargeOrderQueryVO) throws Exception {
        return getRechargeOrderListByPage(pageNum, pageSize, rechargeOrderQueryVO, true);
    }

    @Override
    public ResponseVo<Object> getRechargeOrderListByPage(int pageNum, int pageSize, RechargeOrderQueryVO rechargeOrderQueryVO, boolean count) throws Exception {
        RechargeOrderExample example = new RechargeOrderExample();

        RechargeOrderExample.Criteria c = example.createCriteria();
        //充值订单号
        if (StringUtils.isNotBlank(rechargeOrderQueryVO.getRechargeOrderNo())) {
            c.andRechargeOrderNoEqualTo(rechargeOrderQueryVO.getRechargeOrderNo());
        }
        //验证码
        if (StringUtils.isNotBlank(rechargeOrderQueryVO.getVerificationCode())) {
            c.andVerificationCodeEqualTo(rechargeOrderQueryVO.getVerificationCode());
        }
        //充值类型
        if (rechargeOrderQueryVO.getPayMethod() != null) {
            c.andPayMethodEqualTo(rechargeOrderQueryVO.getPayMethod());
        }
        //用户ID
        if (rechargeOrderQueryVO.getUserId() != null) {
            c.andUserIdEqualTo(rechargeOrderQueryVO.getUserId());
        }
        //充值状态
        if (rechargeOrderQueryVO.getStatus() != null) {
            c.andStatusEqualTo(rechargeOrderQueryVO.getStatus());
        }
        if (!CollectionUtils.isEmpty(rechargeOrderQueryVO.getStatuss())) {
            c.andStatusIn(rechargeOrderQueryVO.getStatuss());
        }
        //发票状态
        if (rechargeOrderQueryVO.getInvoiceStatus() != null) {
            c.andInvoiceStatusEqualTo(rechargeOrderQueryVO.getInvoiceStatus());
        }
        //发票ID
        if (rechargeOrderQueryVO.getInvoiceId() != null) {
            c.andInvoiceIdEqualTo(rechargeOrderQueryVO.getInvoiceId());
        }
        //创建时间
        if (rechargeOrderQueryVO.getStartTime() != null) {
            c.andCreateTimeGreaterThanOrEqualTo(rechargeOrderQueryVO.getStartTime());
        }
        //创建时间
        if (rechargeOrderQueryVO.getEndTime() != null) {
            c.andCreateTimeLessThanOrEqualTo(rechargeOrderQueryVO.getEndTime());
        }
        //充值订单ID
        if (!CollectionUtils.isEmpty(rechargeOrderQueryVO.getRechargeOrderIds())) {
            c.andPtbRechargeOrderIdIn(rechargeOrderQueryVO.getRechargeOrderIds());
        }

        example.setOrderByClause("create_time desc");

        PageHelper.startPage(pageNum, pageSize, count); //开启分页查询，通过拦截器实现，紧接着执行的sql会被拦截
        List<RechargeOrder> orders = rechargeOrderMapper.selectByExample(example);
        List<RechargeOrderVO> returnData = new ArrayList<RechargeOrderVO>();
        PageInfo pageInfo = new PageInfo(orders);
        if (CollectionUtils.isEmpty(orders)) {
            return ReturnUtil.success(pageInfo);
        }
        for (RechargeOrder order : orders) {
            RechargeOrderVO orderVO = new RechargeOrderVO();
            orderVO.setPayTime(order.getPayTime());
            orderVO.setPayType(order.getPayType());
            orderVO.setCreateTime(order.getCreateTime());
            orderVO.setUserId(order.getUserId());
            orderVO.setDeviceType(order.getDeviceType());
            orderVO.setOrderId(order.getPtbRechargeOrderId());
            orderVO.setRechargeAmount(order.getTotalAmount());
            orderVO.setStatus(order.getStatus());
            orderVO.setVerificationCode(order.getVerificationCode());
            orderVO.setPayMethod(order.getPayMethod());
            orderVO.setRechargeOrderNo(order.getRechargeOrderNo());
            orderVO.setPtbRechargeOrderId(order.getPtbRechargeOrderId());
            orderVO.setInvoiceId(order.getInvoiceId());
            orderVO.setInvoiceStatus(order.getInvoiceStatus());
            returnData.add(orderVO);
        }
        pageInfo.setList(returnData);
        return ReturnUtil.success(pageInfo);
    }

    @Override
    public ResponseVo<RechargeOrderVO> getRechargeOrderDetail(Long rechargeOrderId, String rechargeOrderNo, Long userId) {
        RechargeOrderVO orderVO = new RechargeOrderVO();
        RechargeOrder order = rechargeOrderMapper.selectOne(rechargeOrderId, rechargeOrderNo, userId);
        if (null == order) {
            return ReturnUtil.success(null);
        }
        orderVO.setPayTime(order.getPayTime());
        orderVO.setPayType(order.getPayType());
        orderVO.setCreateTime(order.getCreateTime());
        orderVO.setUserId(order.getUserId());
        orderVO.setDeviceType(order.getDeviceType());
        orderVO.setOrderId(order.getPtbRechargeOrderId());
        orderVO.setRechargeAmount(order.getTotalAmount());
        orderVO.setStatus(order.getStatus());
        orderVO.setInvoiceStatus(order.getInvoiceStatus());
        orderVO.setVerificationCode(order.getVerificationCode());
        orderVO.setPayMethod(order.getPayMethod());
        orderVO.setRechargeOrderNo(order.getRechargeOrderNo());
        orderVO.setPtbRechargeOrderId(order.getPtbRechargeOrderId());
        orderVO.setInvoiceId(order.getInvoiceId());
        //线下充值展示收款行信息
        if (PaymentMethodEnum.offline.getPaymentMethod() == order.getPayMethod().intValue()) {
            OfflinePaymentConfig config = paymentService.getOfflinePaymentConfig();
            Map<String, Object> bankInfo = new HashMap<>();
            bankInfo.put("bankName", config.getBankName());
            bankInfo.put("openAccountBankName", config.getOpenAccountBankName());
            bankInfo.put("openAccountUserName", config.getOpenAccountUserName());
            bankInfo.put("openAccountUserNum", config.getOpenAccountUserNum());
            orderVO.setBankInfo(bankInfo);
        }
        return ReturnUtil.success(orderVO);
    }

    @Override
    public ResponseVo<Object> updateInvoiceStatus(RechargeOrderVO rechargeOrderVO, List<Long> rechargeOrderIds, Long invoiceId, Long adminId) throws Exception {
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setInvoiceStatus(rechargeOrderVO.getInvoiceStatus());
        rechargeOrder.setInvoiceId(rechargeOrderVO.getInvoiceId());

        RechargeOrderExample example = new RechargeOrderExample();
        RechargeOrderExample.Criteria c = example.createCriteria();
        if (!CollectionUtils.isEmpty(rechargeOrderIds)) {
            c.andPtbRechargeOrderIdIn(rechargeOrderIds);
        }
        if (invoiceId != null) {
            c.andInvoiceIdEqualTo(invoiceId);
        }
        int result = rechargeOrderMapper.updateByExampleSelective(rechargeOrder, example);
        if (result > 0) {
            List<RechargeOrder> rechargeOrders = rechargeOrderMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(rechargeOrders)) {
                for (RechargeOrder order : rechargeOrders) {
                    if (RechargeOrderInvoiceStatusEnum.waiting.getRechargeOrderInvoiceStatus() == rechargeOrderVO.getInvoiceStatus()) { //用户提交发票
                        rechargeOrderLogService.saveUserOpLog(order.getRechargeOrderNo(),
                                RechargeOrderLogActionTypeEnum.SUBMIT_INVOICE.getActionType(), null, order.getUserId());
                    } else if (RechargeOrderInvoiceStatusEnum.opened.getRechargeOrderInvoiceStatus() == rechargeOrderVO.getInvoiceStatus()) { //管理员确认发票
                        rechargeOrderLogService.saveAdminOpLog(order.getRechargeOrderNo(),
                                RechargeOrderLogActionTypeEnum.CONFIRM_INVOICE.getActionType(), null, adminId);
                    }
                }
            }
            return ReturnUtil.success();
        }
        return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_OPTERROR.getCode(),
                CommonErrorCode.COMMMON_ERROR_OPTERROR.getMessage());
    }

    @Override
    public ResponseVo offlineRecharge(Long rechargeOrderId, Long rechargeAmount, Long adminId) throws Exception {
        RechargeOrder rechargeOrder = rechargeOrderMapper.selectByPrimaryKey(rechargeOrderId);
        if (rechargeOrder == null) {
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(),
                    CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
        }

        boolean result = offlinePaymentService.recharge(rechargeOrder, adminId, rechargeAmount);
        if (result) {
            return ReturnUtil.success();
        }
        return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_INERERROR.getCode(),
                CommonErrorCode.COMMMON_ERROR_INERERROR.getMessage());
    }

    @Override
    public ResponseVo<Object> getFailedRechargeOrderListByPage(int pageNum, int pageSize, FailedRechargeOrderQueryVO failedRechargeOrderQueryVO) throws Exception {
        RechargeFailedLogExample example = new RechargeFailedLogExample();

        RechargeFailedLogExample.Criteria c = example.createCriteria();
        //充值订单号
        if (StringUtils.isNotBlank(failedRechargeOrderQueryVO.getRechargeOrderNo())) {
            c.andRechargeOrderNoEqualTo(failedRechargeOrderQueryVO.getRechargeOrderNo());
        }
        //充值状态
        if (failedRechargeOrderQueryVO.getStatus() != null) {
            c.andStatusEqualTo(failedRechargeOrderQueryVO.getStatus());
        }
        //创建时间
        if (failedRechargeOrderQueryVO.getStartTime() != null) {
            c.andCreateTimeGreaterThanOrEqualTo(failedRechargeOrderQueryVO.getStartTime());
        }
        //创建时间
        if (failedRechargeOrderQueryVO.getEndTime() != null) {
            c.andCreateTimeLessThanOrEqualTo(failedRechargeOrderQueryVO.getEndTime());
        }

        example.setOrderByClause("create_time desc");

        PageHelper.startPage(pageNum, pageSize); //开启分页查询，通过拦截器实现，紧接着执行的sql会被拦截
        List<RechargeFailedLog> failedOrders = rechargeFailedLogMapper.selectByExample(example);
        List<RechargeFailedLogVO> returnData = new ArrayList<RechargeFailedLogVO>();
        PageInfo pageInfo = new PageInfo(failedOrders);
        if (CollectionUtils.isEmpty(failedOrders)) {
            return ReturnUtil.success(pageInfo);
        }
        for (RechargeFailedLog failedOrder : failedOrders) {
            RechargeFailedLogVO rechargeFailedLogVO = new RechargeFailedLogVO();
            rechargeFailedLogVO.setTotalAmount(failedOrder.getTotalAmount());
            rechargeFailedLogVO.setCreateTime(failedOrder.getCreateTime());
            rechargeFailedLogVO.setPtbRechargeFailedLogId(failedOrder.getPtbRechargeFailedLogId());
            rechargeFailedLogVO.setRechargeOrderNo(failedOrder.getRechargeOrderNo());
            rechargeFailedLogVO.setStatus(failedOrder.getStatus());
            returnData.add(rechargeFailedLogVO);
        }
        pageInfo.setList(returnData);
        return ReturnUtil.success(pageInfo);
    }

    @Override
    public ResponseVo<RechargeFailedLogVO> getFailedRechargeOrder(Long failedRechargeOrderId) {
        RechargeFailedLog log = rechargeFailedLogMapper.selectByPrimaryKey(failedRechargeOrderId);
        if (log != null) {
            RechargeFailedLogVO rechargeFailedLogVO = new RechargeFailedLogVO();
            rechargeFailedLogVO.setStatus(log.getStatus());
            rechargeFailedLogVO.setRechargeOrderNo(log.getRechargeOrderNo());
            rechargeFailedLogVO.setPtbRechargeFailedLogId(log.getPtbRechargeFailedLogId());
            rechargeFailedLogVO.setCreateTime(log.getCreateTime());
            rechargeFailedLogVO.setTotalAmount(log.getTotalAmount());
            return ReturnUtil.success(rechargeFailedLogVO);
        }
        return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(), CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
    }

    @Override
    public ResponseVo rechargeFailedOrder(Long failedRechargeOrderId, Long adminId) {
        RechargeFailedLog log = rechargeFailedLogMapper.selectByPrimaryKey(failedRechargeOrderId);
        if (log == null) {
            return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_ARGSERROR.getCode(), CommonErrorCode.COMMMON_ERROR_ARGSERROR.getMessage());
        }
        try {
            boolean result = failedRechargeOrderService.recharge(log, adminId);
            if (result) {
                return ReturnUtil.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnUtil.error(CommonErrorCode.COMMMON_ERROR_INERERROR.getCode(), CommonErrorCode.COMMMON_ERROR_INERERROR.getMessage());
    }

    @Override
    public ResponseVo batchUpdatePayFee(List<Map<String, Object>> payFee) {
        if ( !CollectionUtils.isEmpty( payFee)){
            //批量更新手续费
            rechargeOrderMapper.batchUpdateFee( payFee);
            //批量保存日志
            rechargeOrderLogService.batchSaveLog( payFee);
        }
        return ReturnUtil.success();
    }
}
