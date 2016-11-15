package com.ptb.pay.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ThirdPaymentNotifyLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ThirdPaymentNotifyLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andPthThirdPaymentNotifyLogIdIsNull() {
            addCriterion("pth_third_payment_notify_log_id is null");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdIsNotNull() {
            addCriterion("pth_third_payment_notify_log_id is not null");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdEqualTo(Long value) {
            addCriterion("pth_third_payment_notify_log_id =", value, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdNotEqualTo(Long value) {
            addCriterion("pth_third_payment_notify_log_id <>", value, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdGreaterThan(Long value) {
            addCriterion("pth_third_payment_notify_log_id >", value, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdGreaterThanOrEqualTo(Long value) {
            addCriterion("pth_third_payment_notify_log_id >=", value, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdLessThan(Long value) {
            addCriterion("pth_third_payment_notify_log_id <", value, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdLessThanOrEqualTo(Long value) {
            addCriterion("pth_third_payment_notify_log_id <=", value, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdIn(List<Long> values) {
            addCriterion("pth_third_payment_notify_log_id in", values, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdNotIn(List<Long> values) {
            addCriterion("pth_third_payment_notify_log_id not in", values, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdBetween(Long value1, Long value2) {
            addCriterion("pth_third_payment_notify_log_id between", value1, value2, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andPthThirdPaymentNotifyLogIdNotBetween(Long value1, Long value2) {
            addCriterion("pth_third_payment_notify_log_id not between", value1, value2, "pthThirdPaymentNotifyLogId");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoIsNull() {
            addCriterion("recharge_order_no is null");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoIsNotNull() {
            addCriterion("recharge_order_no is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoEqualTo(String value) {
            addCriterion("recharge_order_no =", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoNotEqualTo(String value) {
            addCriterion("recharge_order_no <>", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoGreaterThan(String value) {
            addCriterion("recharge_order_no >", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoGreaterThanOrEqualTo(String value) {
            addCriterion("recharge_order_no >=", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoLessThan(String value) {
            addCriterion("recharge_order_no <", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoLessThanOrEqualTo(String value) {
            addCriterion("recharge_order_no <=", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoLike(String value) {
            addCriterion("recharge_order_no like", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoNotLike(String value) {
            addCriterion("recharge_order_no not like", value, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoIn(List<String> values) {
            addCriterion("recharge_order_no in", values, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoNotIn(List<String> values) {
            addCriterion("recharge_order_no not in", values, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoBetween(String value1, String value2) {
            addCriterion("recharge_order_no between", value1, value2, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andRechargeOrderNoNotBetween(String value1, String value2) {
            addCriterion("recharge_order_no not between", value1, value2, "rechargeOrderNo");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNull() {
            addCriterion("pay_type is null");
            return (Criteria) this;
        }

        public Criteria andPayTypeIsNotNull() {
            addCriterion("pay_type is not null");
            return (Criteria) this;
        }

        public Criteria andPayTypeEqualTo(Byte value) {
            addCriterion("pay_type =", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotEqualTo(Byte value) {
            addCriterion("pay_type <>", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThan(Byte value) {
            addCriterion("pay_type >", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("pay_type >=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThan(Byte value) {
            addCriterion("pay_type <", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeLessThanOrEqualTo(Byte value) {
            addCriterion("pay_type <=", value, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeIn(List<Byte> values) {
            addCriterion("pay_type in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotIn(List<Byte> values) {
            addCriterion("pay_type not in", values, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeBetween(Byte value1, Byte value2) {
            addCriterion("pay_type between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andPayTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("pay_type not between", value1, value2, "payType");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeIsNull() {
            addCriterion("notify_time is null");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeIsNotNull() {
            addCriterion("notify_time is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeEqualTo(Date value) {
            addCriterion("notify_time =", value, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeNotEqualTo(Date value) {
            addCriterion("notify_time <>", value, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeGreaterThan(Date value) {
            addCriterion("notify_time >", value, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("notify_time >=", value, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeLessThan(Date value) {
            addCriterion("notify_time <", value, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("notify_time <=", value, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeIn(List<Date> values) {
            addCriterion("notify_time in", values, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeNotIn(List<Date> values) {
            addCriterion("notify_time not in", values, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeBetween(Date value1, Date value2) {
            addCriterion("notify_time between", value1, value2, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andNotifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("notify_time not between", value1, value2, "notifyTime");
            return (Criteria) this;
        }

        public Criteria andTradeStatusIsNull() {
            addCriterion("trade_status is null");
            return (Criteria) this;
        }

        public Criteria andTradeStatusIsNotNull() {
            addCriterion("trade_status is not null");
            return (Criteria) this;
        }

        public Criteria andTradeStatusEqualTo(String value) {
            addCriterion("trade_status =", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotEqualTo(String value) {
            addCriterion("trade_status <>", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusGreaterThan(String value) {
            addCriterion("trade_status >", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusGreaterThanOrEqualTo(String value) {
            addCriterion("trade_status >=", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusLessThan(String value) {
            addCriterion("trade_status <", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusLessThanOrEqualTo(String value) {
            addCriterion("trade_status <=", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusLike(String value) {
            addCriterion("trade_status like", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotLike(String value) {
            addCriterion("trade_status not like", value, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusIn(List<String> values) {
            addCriterion("trade_status in", values, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotIn(List<String> values) {
            addCriterion("trade_status not in", values, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusBetween(String value1, String value2) {
            addCriterion("trade_status between", value1, value2, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andTradeStatusNotBetween(String value1, String value2) {
            addCriterion("trade_status not between", value1, value2, "tradeStatus");
            return (Criteria) this;
        }

        public Criteria andNotifyContentIsNull() {
            addCriterion("notify_content is null");
            return (Criteria) this;
        }

        public Criteria andNotifyContentIsNotNull() {
            addCriterion("notify_content is not null");
            return (Criteria) this;
        }

        public Criteria andNotifyContentEqualTo(String value) {
            addCriterion("notify_content =", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentNotEqualTo(String value) {
            addCriterion("notify_content <>", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentGreaterThan(String value) {
            addCriterion("notify_content >", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentGreaterThanOrEqualTo(String value) {
            addCriterion("notify_content >=", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentLessThan(String value) {
            addCriterion("notify_content <", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentLessThanOrEqualTo(String value) {
            addCriterion("notify_content <=", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentLike(String value) {
            addCriterion("notify_content like", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentNotLike(String value) {
            addCriterion("notify_content not like", value, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentIn(List<String> values) {
            addCriterion("notify_content in", values, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentNotIn(List<String> values) {
            addCriterion("notify_content not in", values, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentBetween(String value1, String value2) {
            addCriterion("notify_content between", value1, value2, "notifyContent");
            return (Criteria) this;
        }

        public Criteria andNotifyContentNotBetween(String value1, String value2) {
            addCriterion("notify_content not between", value1, value2, "notifyContent");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}