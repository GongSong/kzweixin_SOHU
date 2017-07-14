package com.kuaizhan.kzweixin.dao.po.auto;

import java.util.ArrayList;
import java.util.List;

public class CustomMassPOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CustomMassPOExample() {
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

        public Criteria andCustomMassIdIsNull() {
            addCriterion("custom_mass_id is null");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdIsNotNull() {
            addCriterion("custom_mass_id is not null");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdEqualTo(Long value) {
            addCriterion("custom_mass_id =", value, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdNotEqualTo(Long value) {
            addCriterion("custom_mass_id <>", value, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdGreaterThan(Long value) {
            addCriterion("custom_mass_id >", value, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdGreaterThanOrEqualTo(Long value) {
            addCriterion("custom_mass_id >=", value, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdLessThan(Long value) {
            addCriterion("custom_mass_id <", value, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdLessThanOrEqualTo(Long value) {
            addCriterion("custom_mass_id <=", value, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdIn(List<Long> values) {
            addCriterion("custom_mass_id in", values, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdNotIn(List<Long> values) {
            addCriterion("custom_mass_id not in", values, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdBetween(Long value1, Long value2) {
            addCriterion("custom_mass_id between", value1, value2, "customMassId");
            return (Criteria) this;
        }

        public Criteria andCustomMassIdNotBetween(Long value1, Long value2) {
            addCriterion("custom_mass_id not between", value1, value2, "customMassId");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidIsNull() {
            addCriterion("weixin_appid is null");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidIsNotNull() {
            addCriterion("weixin_appid is not null");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidEqualTo(Long value) {
            addCriterion("weixin_appid =", value, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidNotEqualTo(Long value) {
            addCriterion("weixin_appid <>", value, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidGreaterThan(Long value) {
            addCriterion("weixin_appid >", value, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidGreaterThanOrEqualTo(Long value) {
            addCriterion("weixin_appid >=", value, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidLessThan(Long value) {
            addCriterion("weixin_appid <", value, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidLessThanOrEqualTo(Long value) {
            addCriterion("weixin_appid <=", value, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidIn(List<Long> values) {
            addCriterion("weixin_appid in", values, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidNotIn(List<Long> values) {
            addCriterion("weixin_appid not in", values, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidBetween(Long value1, Long value2) {
            addCriterion("weixin_appid between", value1, value2, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andWeixinAppidNotBetween(Long value1, Long value2) {
            addCriterion("weixin_appid not between", value1, value2, "weixinAppid");
            return (Criteria) this;
        }

        public Criteria andTagIdIsNull() {
            addCriterion("tag_id is null");
            return (Criteria) this;
        }

        public Criteria andTagIdIsNotNull() {
            addCriterion("tag_id is not null");
            return (Criteria) this;
        }

        public Criteria andTagIdEqualTo(Integer value) {
            addCriterion("tag_id =", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotEqualTo(Integer value) {
            addCriterion("tag_id <>", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdGreaterThan(Integer value) {
            addCriterion("tag_id >", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("tag_id >=", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdLessThan(Integer value) {
            addCriterion("tag_id <", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdLessThanOrEqualTo(Integer value) {
            addCriterion("tag_id <=", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdIn(List<Integer> values) {
            addCriterion("tag_id in", values, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotIn(List<Integer> values) {
            addCriterion("tag_id not in", values, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdBetween(Integer value1, Integer value2) {
            addCriterion("tag_id between", value1, value2, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotBetween(Integer value1, Integer value2) {
            addCriterion("tag_id not between", value1, value2, "tagId");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNull() {
            addCriterion("msg_type is null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNotNull() {
            addCriterion("msg_type is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeEqualTo(Integer value) {
            addCriterion("msg_type =", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotEqualTo(Integer value) {
            addCriterion("msg_type <>", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThan(Integer value) {
            addCriterion("msg_type >", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("msg_type >=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThan(Integer value) {
            addCriterion("msg_type <", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThanOrEqualTo(Integer value) {
            addCriterion("msg_type <=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIn(List<Integer> values) {
            addCriterion("msg_type in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotIn(List<Integer> values) {
            addCriterion("msg_type not in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeBetween(Integer value1, Integer value2) {
            addCriterion("msg_type between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("msg_type not between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andTotalCountIsNull() {
            addCriterion("total_count is null");
            return (Criteria) this;
        }

        public Criteria andTotalCountIsNotNull() {
            addCriterion("total_count is not null");
            return (Criteria) this;
        }

        public Criteria andTotalCountEqualTo(Integer value) {
            addCriterion("total_count =", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountNotEqualTo(Integer value) {
            addCriterion("total_count <>", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountGreaterThan(Integer value) {
            addCriterion("total_count >", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_count >=", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountLessThan(Integer value) {
            addCriterion("total_count <", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountLessThanOrEqualTo(Integer value) {
            addCriterion("total_count <=", value, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountIn(List<Integer> values) {
            addCriterion("total_count in", values, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountNotIn(List<Integer> values) {
            addCriterion("total_count not in", values, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountBetween(Integer value1, Integer value2) {
            addCriterion("total_count between", value1, value2, "totalCount");
            return (Criteria) this;
        }

        public Criteria andTotalCountNotBetween(Integer value1, Integer value2) {
            addCriterion("total_count not between", value1, value2, "totalCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountIsNull() {
            addCriterion("success_count is null");
            return (Criteria) this;
        }

        public Criteria andSuccessCountIsNotNull() {
            addCriterion("success_count is not null");
            return (Criteria) this;
        }

        public Criteria andSuccessCountEqualTo(Integer value) {
            addCriterion("success_count =", value, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountNotEqualTo(Integer value) {
            addCriterion("success_count <>", value, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountGreaterThan(Integer value) {
            addCriterion("success_count >", value, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("success_count >=", value, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountLessThan(Integer value) {
            addCriterion("success_count <", value, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountLessThanOrEqualTo(Integer value) {
            addCriterion("success_count <=", value, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountIn(List<Integer> values) {
            addCriterion("success_count in", values, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountNotIn(List<Integer> values) {
            addCriterion("success_count not in", values, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountBetween(Integer value1, Integer value2) {
            addCriterion("success_count between", value1, value2, "successCount");
            return (Criteria) this;
        }

        public Criteria andSuccessCountNotBetween(Integer value1, Integer value2) {
            addCriterion("success_count not between", value1, value2, "successCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountIsNull() {
            addCriterion("reject_fail_count is null");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountIsNotNull() {
            addCriterion("reject_fail_count is not null");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountEqualTo(Integer value) {
            addCriterion("reject_fail_count =", value, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountNotEqualTo(Integer value) {
            addCriterion("reject_fail_count <>", value, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountGreaterThan(Integer value) {
            addCriterion("reject_fail_count >", value, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("reject_fail_count >=", value, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountLessThan(Integer value) {
            addCriterion("reject_fail_count <", value, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountLessThanOrEqualTo(Integer value) {
            addCriterion("reject_fail_count <=", value, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountIn(List<Integer> values) {
            addCriterion("reject_fail_count in", values, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountNotIn(List<Integer> values) {
            addCriterion("reject_fail_count not in", values, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountBetween(Integer value1, Integer value2) {
            addCriterion("reject_fail_count between", value1, value2, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andRejectFailCountNotBetween(Integer value1, Integer value2) {
            addCriterion("reject_fail_count not between", value1, value2, "rejectFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountIsNull() {
            addCriterion("other_fail_count is null");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountIsNotNull() {
            addCriterion("other_fail_count is not null");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountEqualTo(Integer value) {
            addCriterion("other_fail_count =", value, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountNotEqualTo(Integer value) {
            addCriterion("other_fail_count <>", value, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountGreaterThan(Integer value) {
            addCriterion("other_fail_count >", value, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("other_fail_count >=", value, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountLessThan(Integer value) {
            addCriterion("other_fail_count <", value, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountLessThanOrEqualTo(Integer value) {
            addCriterion("other_fail_count <=", value, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountIn(List<Integer> values) {
            addCriterion("other_fail_count in", values, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountNotIn(List<Integer> values) {
            addCriterion("other_fail_count not in", values, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountBetween(Integer value1, Integer value2) {
            addCriterion("other_fail_count between", value1, value2, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andOtherFailCountNotBetween(Integer value1, Integer value2) {
            addCriterion("other_fail_count not between", value1, value2, "otherFailCount");
            return (Criteria) this;
        }

        public Criteria andIsTimingIsNull() {
            addCriterion("is_timing is null");
            return (Criteria) this;
        }

        public Criteria andIsTimingIsNotNull() {
            addCriterion("is_timing is not null");
            return (Criteria) this;
        }

        public Criteria andIsTimingEqualTo(Integer value) {
            addCriterion("is_timing =", value, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingNotEqualTo(Integer value) {
            addCriterion("is_timing <>", value, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingGreaterThan(Integer value) {
            addCriterion("is_timing >", value, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_timing >=", value, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingLessThan(Integer value) {
            addCriterion("is_timing <", value, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingLessThanOrEqualTo(Integer value) {
            addCriterion("is_timing <=", value, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingIn(List<Integer> values) {
            addCriterion("is_timing in", values, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingNotIn(List<Integer> values) {
            addCriterion("is_timing not in", values, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingBetween(Integer value1, Integer value2) {
            addCriterion("is_timing between", value1, value2, "isTiming");
            return (Criteria) this;
        }

        public Criteria andIsTimingNotBetween(Integer value1, Integer value2) {
            addCriterion("is_timing not between", value1, value2, "isTiming");
            return (Criteria) this;
        }

        public Criteria andPublishTimeIsNull() {
            addCriterion("publish_time is null");
            return (Criteria) this;
        }

        public Criteria andPublishTimeIsNotNull() {
            addCriterion("publish_time is not null");
            return (Criteria) this;
        }

        public Criteria andPublishTimeEqualTo(Integer value) {
            addCriterion("publish_time =", value, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeNotEqualTo(Integer value) {
            addCriterion("publish_time <>", value, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeGreaterThan(Integer value) {
            addCriterion("publish_time >", value, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("publish_time >=", value, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeLessThan(Integer value) {
            addCriterion("publish_time <", value, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeLessThanOrEqualTo(Integer value) {
            addCriterion("publish_time <=", value, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeIn(List<Integer> values) {
            addCriterion("publish_time in", values, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeNotIn(List<Integer> values) {
            addCriterion("publish_time not in", values, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeBetween(Integer value1, Integer value2) {
            addCriterion("publish_time between", value1, value2, "publishTime");
            return (Criteria) this;
        }

        public Criteria andPublishTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("publish_time not between", value1, value2, "publishTime");
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

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Integer value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Integer value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Integer value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Integer value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Integer> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Integer> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Integer value1, Integer value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Integer value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Integer value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Integer value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Integer value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Integer value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Integer> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Integer> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Integer value1, Integer value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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