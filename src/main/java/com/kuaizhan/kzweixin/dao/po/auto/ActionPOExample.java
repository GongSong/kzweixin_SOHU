package com.kuaizhan.kzweixin.dao.po.auto;

import com.kuaizhan.kzweixin.enums.ActionType;
import com.kuaizhan.kzweixin.enums.BizCode;
import com.kuaizhan.kzweixin.enums.ResponseType;
import java.util.ArrayList;
import java.util.List;

public class ActionPOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ActionPOExample() {
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
        protected List<Criterion> bizCodeCriteria;

        protected List<Criterion> actionTypeCriteria;

        protected List<Criterion> responseTypeCriteria;

        protected List<Criterion> allCriteria;

        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
            bizCodeCriteria = new ArrayList<Criterion>();
            actionTypeCriteria = new ArrayList<Criterion>();
            responseTypeCriteria = new ArrayList<Criterion>();
        }

        public List<Criterion> getBizCodeCriteria() {
            return bizCodeCriteria;
        }

        protected void addBizCodeCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            bizCodeCriteria.add(new Criterion(condition, value, "com.kuaizhan.kzweixin.dao.typehandler.BizCodeTypeHandler"));
            allCriteria = null;
        }

        protected void addBizCodeCriterion(String condition, BizCode value1, BizCode value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            bizCodeCriteria.add(new Criterion(condition, value1, value2, "com.kuaizhan.kzweixin.dao.typehandler.BizCodeTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getActionTypeCriteria() {
            return actionTypeCriteria;
        }

        protected void addActionTypeCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            actionTypeCriteria.add(new Criterion(condition, value, "com.kuaizhan.kzweixin.dao.typehandler.ActionTypeHandler"));
            allCriteria = null;
        }

        protected void addActionTypeCriterion(String condition, ActionType value1, ActionType value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            actionTypeCriteria.add(new Criterion(condition, value1, value2, "com.kuaizhan.kzweixin.dao.typehandler.ActionTypeHandler"));
            allCriteria = null;
        }

        public List<Criterion> getResponseTypeCriteria() {
            return responseTypeCriteria;
        }

        protected void addResponseTypeCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            responseTypeCriteria.add(new Criterion(condition, value, "com.kuaizhan.kzweixin.dao.typehandler.ResponseTypeHandler"));
            allCriteria = null;
        }

        protected void addResponseTypeCriterion(String condition, ResponseType value1, ResponseType value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            responseTypeCriteria.add(new Criterion(condition, value1, value2, "com.kuaizhan.kzweixin.dao.typehandler.ResponseTypeHandler"));
            allCriteria = null;
        }

        public boolean isValid() {
            return criteria.size() > 0
                || bizCodeCriteria.size() > 0
                || actionTypeCriteria.size() > 0
                || responseTypeCriteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            if (allCriteria == null) {
                allCriteria = new ArrayList<Criterion>();
                allCriteria.addAll(criteria);
                allCriteria.addAll(bizCodeCriteria);
                allCriteria.addAll(actionTypeCriteria);
                allCriteria.addAll(responseTypeCriteria);
            }
            return allCriteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
            allCriteria = null;
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
            allCriteria = null;
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andBizCodeIsNull() {
            addCriterion("biz_code is null");
            return (Criteria) this;
        }

        public Criteria andBizCodeIsNotNull() {
            addCriterion("biz_code is not null");
            return (Criteria) this;
        }

        public Criteria andBizCodeEqualTo(BizCode value) {
            addBizCodeCriterion("biz_code =", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeNotEqualTo(BizCode value) {
            addBizCodeCriterion("biz_code <>", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeGreaterThan(BizCode value) {
            addBizCodeCriterion("biz_code >", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeGreaterThanOrEqualTo(BizCode value) {
            addBizCodeCriterion("biz_code >=", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeLessThan(BizCode value) {
            addBizCodeCriterion("biz_code <", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeLessThanOrEqualTo(BizCode value) {
            addBizCodeCriterion("biz_code <=", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeLike(BizCode value) {
            addBizCodeCriterion("biz_code like", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeNotLike(BizCode value) {
            addBizCodeCriterion("biz_code not like", value, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeIn(List<BizCode> values) {
            addBizCodeCriterion("biz_code in", values, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeNotIn(List<BizCode> values) {
            addBizCodeCriterion("biz_code not in", values, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeBetween(BizCode value1, BizCode value2) {
            addBizCodeCriterion("biz_code between", value1, value2, "bizCode");
            return (Criteria) this;
        }

        public Criteria andBizCodeNotBetween(BizCode value1, BizCode value2) {
            addBizCodeCriterion("biz_code not between", value1, value2, "bizCode");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNull() {
            addCriterion("keyword is null");
            return (Criteria) this;
        }

        public Criteria andKeywordIsNotNull() {
            addCriterion("keyword is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordEqualTo(String value) {
            addCriterion("keyword =", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotEqualTo(String value) {
            addCriterion("keyword <>", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThan(String value) {
            addCriterion("keyword >", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordGreaterThanOrEqualTo(String value) {
            addCriterion("keyword >=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThan(String value) {
            addCriterion("keyword <", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLessThanOrEqualTo(String value) {
            addCriterion("keyword <=", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordLike(String value) {
            addCriterion("keyword like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotLike(String value) {
            addCriterion("keyword not like", value, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordIn(List<String> values) {
            addCriterion("keyword in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotIn(List<String> values) {
            addCriterion("keyword not in", values, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordBetween(String value1, String value2) {
            addCriterion("keyword between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andKeywordNotBetween(String value1, String value2) {
            addCriterion("keyword not between", value1, value2, "keyword");
            return (Criteria) this;
        }

        public Criteria andActionTypeIsNull() {
            addCriterion("action_type is null");
            return (Criteria) this;
        }

        public Criteria andActionTypeIsNotNull() {
            addCriterion("action_type is not null");
            return (Criteria) this;
        }

        public Criteria andActionTypeEqualTo(ActionType value) {
            addActionTypeCriterion("action_type =", value, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeNotEqualTo(ActionType value) {
            addActionTypeCriterion("action_type <>", value, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeGreaterThan(ActionType value) {
            addActionTypeCriterion("action_type >", value, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeGreaterThanOrEqualTo(ActionType value) {
            addActionTypeCriterion("action_type >=", value, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeLessThan(ActionType value) {
            addActionTypeCriterion("action_type <", value, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeLessThanOrEqualTo(ActionType value) {
            addActionTypeCriterion("action_type <=", value, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeIn(List<ActionType> values) {
            addActionTypeCriterion("action_type in", values, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeNotIn(List<ActionType> values) {
            addActionTypeCriterion("action_type not in", values, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeBetween(ActionType value1, ActionType value2) {
            addActionTypeCriterion("action_type between", value1, value2, "actionType");
            return (Criteria) this;
        }

        public Criteria andActionTypeNotBetween(ActionType value1, ActionType value2) {
            addActionTypeCriterion("action_type not between", value1, value2, "actionType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeIsNull() {
            addCriterion("response_type is null");
            return (Criteria) this;
        }

        public Criteria andResponseTypeIsNotNull() {
            addCriterion("response_type is not null");
            return (Criteria) this;
        }

        public Criteria andResponseTypeEqualTo(ResponseType value) {
            addResponseTypeCriterion("response_type =", value, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeNotEqualTo(ResponseType value) {
            addResponseTypeCriterion("response_type <>", value, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeGreaterThan(ResponseType value) {
            addResponseTypeCriterion("response_type >", value, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeGreaterThanOrEqualTo(ResponseType value) {
            addResponseTypeCriterion("response_type >=", value, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeLessThan(ResponseType value) {
            addResponseTypeCriterion("response_type <", value, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeLessThanOrEqualTo(ResponseType value) {
            addResponseTypeCriterion("response_type <=", value, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeIn(List<ResponseType> values) {
            addResponseTypeCriterion("response_type in", values, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeNotIn(List<ResponseType> values) {
            addResponseTypeCriterion("response_type not in", values, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeBetween(ResponseType value1, ResponseType value2) {
            addResponseTypeCriterion("response_type between", value1, value2, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseTypeNotBetween(ResponseType value1, ResponseType value2) {
            addResponseTypeCriterion("response_type not between", value1, value2, "responseType");
            return (Criteria) this;
        }

        public Criteria andResponseJsonIsNull() {
            addCriterion("response_json is null");
            return (Criteria) this;
        }

        public Criteria andResponseJsonIsNotNull() {
            addCriterion("response_json is not null");
            return (Criteria) this;
        }

        public Criteria andResponseJsonEqualTo(String value) {
            addCriterion("response_json =", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonNotEqualTo(String value) {
            addCriterion("response_json <>", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonGreaterThan(String value) {
            addCriterion("response_json >", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonGreaterThanOrEqualTo(String value) {
            addCriterion("response_json >=", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonLessThan(String value) {
            addCriterion("response_json <", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonLessThanOrEqualTo(String value) {
            addCriterion("response_json <=", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonLike(String value) {
            addCriterion("response_json like", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonNotLike(String value) {
            addCriterion("response_json not like", value, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonIn(List<String> values) {
            addCriterion("response_json in", values, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonNotIn(List<String> values) {
            addCriterion("response_json not in", values, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonBetween(String value1, String value2) {
            addCriterion("response_json between", value1, value2, "responseJson");
            return (Criteria) this;
        }

        public Criteria andResponseJsonNotBetween(String value1, String value2) {
            addCriterion("response_json not between", value1, value2, "responseJson");
            return (Criteria) this;
        }

        public Criteria andExtIsNull() {
            addCriterion("ext is null");
            return (Criteria) this;
        }

        public Criteria andExtIsNotNull() {
            addCriterion("ext is not null");
            return (Criteria) this;
        }

        public Criteria andExtEqualTo(String value) {
            addCriterion("ext =", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotEqualTo(String value) {
            addCriterion("ext <>", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtGreaterThan(String value) {
            addCriterion("ext >", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtGreaterThanOrEqualTo(String value) {
            addCriterion("ext >=", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLessThan(String value) {
            addCriterion("ext <", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLessThanOrEqualTo(String value) {
            addCriterion("ext <=", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLike(String value) {
            addCriterion("ext like", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotLike(String value) {
            addCriterion("ext not like", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtIn(List<String> values) {
            addCriterion("ext in", values, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotIn(List<String> values) {
            addCriterion("ext not in", values, "ext");
            return (Criteria) this;
        }

        public Criteria andExtBetween(String value1, String value2) {
            addCriterion("ext between", value1, value2, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotBetween(String value1, String value2) {
            addCriterion("ext not between", value1, value2, "ext");
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

        public Criteria andStatusEqualTo(Boolean value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Boolean value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Boolean value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Boolean value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Boolean value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Boolean value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Boolean> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Boolean> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Boolean value1, Boolean value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Boolean value1, Boolean value2) {
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