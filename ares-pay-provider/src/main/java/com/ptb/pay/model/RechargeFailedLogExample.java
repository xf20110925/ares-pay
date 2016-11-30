package com.ptb.pay.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RechargeFailedLogExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RechargeFailedLogExample() {
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

        public Criteria andPtbRechargeFailedLogIdIsNull() {
            addCriterion("ptb_recharge_failed_log_id is null");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdIsNotNull() {
            addCriterion("ptb_recharge_failed_log_id is not null");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdEqualTo(Long value) {
            addCriterion("ptb_recharge_failed_log_id =", value, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdNotEqualTo(Long value) {
            addCriterion("ptb_recharge_failed_log_id <>", value, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdGreaterThan(Long value) {
            addCriterion("ptb_recharge_failed_log_id >", value, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ptb_recharge_failed_log_id >=", value, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdLessThan(Long value) {
            addCriterion("ptb_recharge_failed_log_id <", value, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdLessThanOrEqualTo(Long value) {
            addCriterion("ptb_recharge_failed_log_id <=", value, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdIn(List<Long> values) {
            addCriterion("ptb_recharge_failed_log_id in", values, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdNotIn(List<Long> values) {
            addCriterion("ptb_recharge_failed_log_id not in", values, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdBetween(Long value1, Long value2) {
            addCriterion("ptb_recharge_failed_log_id between", value1, value2, "ptbRechargeFailedLogId");
            return (Criteria) this;
        }

        public Criteria andPtbRechargeFailedLogIdNotBetween(Long value1, Long value2) {
            addCriterion("ptb_recharge_failed_log_id not between", value1, value2, "ptbRechargeFailedLogId");
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

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNull() {
            addCriterion("total_amount is null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIsNotNull() {
            addCriterion("total_amount is not null");
            return (Criteria) this;
        }

        public Criteria andTotalAmountEqualTo(Long value) {
            addCriterion("total_amount =", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotEqualTo(Long value) {
            addCriterion("total_amount <>", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThan(Long value) {
            addCriterion("total_amount >", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("total_amount >=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThan(Long value) {
            addCriterion("total_amount <", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountLessThanOrEqualTo(Long value) {
            addCriterion("total_amount <=", value, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountIn(List<Long> values) {
            addCriterion("total_amount in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotIn(List<Long> values) {
            addCriterion("total_amount not in", values, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountBetween(Long value1, Long value2) {
            addCriterion("total_amount between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andTotalAmountNotBetween(Long value1, Long value2) {
            addCriterion("total_amount not between", value1, value2, "totalAmount");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsIsNull() {
            addCriterion("recharge_params is null");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsIsNotNull() {
            addCriterion("recharge_params is not null");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsEqualTo(String value) {
            addCriterion("recharge_params =", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsNotEqualTo(String value) {
            addCriterion("recharge_params <>", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsGreaterThan(String value) {
            addCriterion("recharge_params >", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsGreaterThanOrEqualTo(String value) {
            addCriterion("recharge_params >=", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsLessThan(String value) {
            addCriterion("recharge_params <", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsLessThanOrEqualTo(String value) {
            addCriterion("recharge_params <=", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsLike(String value) {
            addCriterion("recharge_params like", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsNotLike(String value) {
            addCriterion("recharge_params not like", value, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsIn(List<String> values) {
            addCriterion("recharge_params in", values, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsNotIn(List<String> values) {
            addCriterion("recharge_params not in", values, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsBetween(String value1, String value2) {
            addCriterion("recharge_params between", value1, value2, "rechargeParams");
            return (Criteria) this;
        }

        public Criteria andRechargeParamsNotBetween(String value1, String value2) {
            addCriterion("recharge_params not between", value1, value2, "rechargeParams");
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