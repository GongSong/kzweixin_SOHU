package com.kuaizhan.kzweixin.dao.po.auto;

import java.util.ArrayList;
import java.util.List;

public class AccountPOExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AccountPOExample() {
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

        public Criteria andSiteIdIsNull() {
            addCriterion("site_id is null");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNotNull() {
            addCriterion("site_id is not null");
            return (Criteria) this;
        }

        public Criteria andSiteIdEqualTo(Long value) {
            addCriterion("site_id =", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotEqualTo(Long value) {
            addCriterion("site_id <>", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThan(Long value) {
            addCriterion("site_id >", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThanOrEqualTo(Long value) {
            addCriterion("site_id >=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThan(Long value) {
            addCriterion("site_id <", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThanOrEqualTo(Long value) {
            addCriterion("site_id <=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIn(List<Long> values) {
            addCriterion("site_id in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotIn(List<Long> values) {
            addCriterion("site_id not in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdBetween(Long value1, Long value2) {
            addCriterion("site_id between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotBetween(Long value1, Long value2) {
            addCriterion("site_id not between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andAccessTokenIsNull() {
            addCriterion("access_token is null");
            return (Criteria) this;
        }

        public Criteria andAccessTokenIsNotNull() {
            addCriterion("access_token is not null");
            return (Criteria) this;
        }

        public Criteria andAccessTokenEqualTo(String value) {
            addCriterion("access_token =", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenNotEqualTo(String value) {
            addCriterion("access_token <>", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenGreaterThan(String value) {
            addCriterion("access_token >", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenGreaterThanOrEqualTo(String value) {
            addCriterion("access_token >=", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenLessThan(String value) {
            addCriterion("access_token <", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenLessThanOrEqualTo(String value) {
            addCriterion("access_token <=", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenLike(String value) {
            addCriterion("access_token like", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenNotLike(String value) {
            addCriterion("access_token not like", value, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenIn(List<String> values) {
            addCriterion("access_token in", values, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenNotIn(List<String> values) {
            addCriterion("access_token not in", values, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenBetween(String value1, String value2) {
            addCriterion("access_token between", value1, value2, "accessToken");
            return (Criteria) this;
        }

        public Criteria andAccessTokenNotBetween(String value1, String value2) {
            addCriterion("access_token not between", value1, value2, "accessToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenIsNull() {
            addCriterion("refresh_token is null");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenIsNotNull() {
            addCriterion("refresh_token is not null");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenEqualTo(String value) {
            addCriterion("refresh_token =", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenNotEqualTo(String value) {
            addCriterion("refresh_token <>", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenGreaterThan(String value) {
            addCriterion("refresh_token >", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenGreaterThanOrEqualTo(String value) {
            addCriterion("refresh_token >=", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenLessThan(String value) {
            addCriterion("refresh_token <", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenLessThanOrEqualTo(String value) {
            addCriterion("refresh_token <=", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenLike(String value) {
            addCriterion("refresh_token like", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenNotLike(String value) {
            addCriterion("refresh_token not like", value, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenIn(List<String> values) {
            addCriterion("refresh_token in", values, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenNotIn(List<String> values) {
            addCriterion("refresh_token not in", values, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenBetween(String value1, String value2) {
            addCriterion("refresh_token between", value1, value2, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andRefreshTokenNotBetween(String value1, String value2) {
            addCriterion("refresh_token not between", value1, value2, "refreshToken");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeIsNull() {
            addCriterion("expires_time is null");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeIsNotNull() {
            addCriterion("expires_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeEqualTo(Integer value) {
            addCriterion("expires_time =", value, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeNotEqualTo(Integer value) {
            addCriterion("expires_time <>", value, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeGreaterThan(Integer value) {
            addCriterion("expires_time >", value, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("expires_time >=", value, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeLessThan(Integer value) {
            addCriterion("expires_time <", value, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeLessThanOrEqualTo(Integer value) {
            addCriterion("expires_time <=", value, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeIn(List<Integer> values) {
            addCriterion("expires_time in", values, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeNotIn(List<Integer> values) {
            addCriterion("expires_time not in", values, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeBetween(Integer value1, Integer value2) {
            addCriterion("expires_time between", value1, value2, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andExpiresTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("expires_time not between", value1, value2, "expiresTime");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNull() {
            addCriterion("app_id is null");
            return (Criteria) this;
        }

        public Criteria andAppIdIsNotNull() {
            addCriterion("app_id is not null");
            return (Criteria) this;
        }

        public Criteria andAppIdEqualTo(String value) {
            addCriterion("app_id =", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotEqualTo(String value) {
            addCriterion("app_id <>", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThan(String value) {
            addCriterion("app_id >", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdGreaterThanOrEqualTo(String value) {
            addCriterion("app_id >=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThan(String value) {
            addCriterion("app_id <", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLessThanOrEqualTo(String value) {
            addCriterion("app_id <=", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdLike(String value) {
            addCriterion("app_id like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotLike(String value) {
            addCriterion("app_id not like", value, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdIn(List<String> values) {
            addCriterion("app_id in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotIn(List<String> values) {
            addCriterion("app_id not in", values, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdBetween(String value1, String value2) {
            addCriterion("app_id between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppIdNotBetween(String value1, String value2) {
            addCriterion("app_id not between", value1, value2, "appId");
            return (Criteria) this;
        }

        public Criteria andAppSecretIsNull() {
            addCriterion("app_secret is null");
            return (Criteria) this;
        }

        public Criteria andAppSecretIsNotNull() {
            addCriterion("app_secret is not null");
            return (Criteria) this;
        }

        public Criteria andAppSecretEqualTo(String value) {
            addCriterion("app_secret =", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotEqualTo(String value) {
            addCriterion("app_secret <>", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretGreaterThan(String value) {
            addCriterion("app_secret >", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretGreaterThanOrEqualTo(String value) {
            addCriterion("app_secret >=", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretLessThan(String value) {
            addCriterion("app_secret <", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretLessThanOrEqualTo(String value) {
            addCriterion("app_secret <=", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretLike(String value) {
            addCriterion("app_secret like", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotLike(String value) {
            addCriterion("app_secret not like", value, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretIn(List<String> values) {
            addCriterion("app_secret in", values, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotIn(List<String> values) {
            addCriterion("app_secret not in", values, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretBetween(String value1, String value2) {
            addCriterion("app_secret between", value1, value2, "appSecret");
            return (Criteria) this;
        }

        public Criteria andAppSecretNotBetween(String value1, String value2) {
            addCriterion("app_secret not between", value1, value2, "appSecret");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonIsNull() {
            addCriterion("func_info_json is null");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonIsNotNull() {
            addCriterion("func_info_json is not null");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonEqualTo(String value) {
            addCriterion("func_info_json =", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonNotEqualTo(String value) {
            addCriterion("func_info_json <>", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonGreaterThan(String value) {
            addCriterion("func_info_json >", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonGreaterThanOrEqualTo(String value) {
            addCriterion("func_info_json >=", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonLessThan(String value) {
            addCriterion("func_info_json <", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonLessThanOrEqualTo(String value) {
            addCriterion("func_info_json <=", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonLike(String value) {
            addCriterion("func_info_json like", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonNotLike(String value) {
            addCriterion("func_info_json not like", value, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonIn(List<String> values) {
            addCriterion("func_info_json in", values, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonNotIn(List<String> values) {
            addCriterion("func_info_json not in", values, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonBetween(String value1, String value2) {
            addCriterion("func_info_json between", value1, value2, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andFuncInfoJsonNotBetween(String value1, String value2) {
            addCriterion("func_info_json not between", value1, value2, "funcInfoJson");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNull() {
            addCriterion("nick_name is null");
            return (Criteria) this;
        }

        public Criteria andNickNameIsNotNull() {
            addCriterion("nick_name is not null");
            return (Criteria) this;
        }

        public Criteria andNickNameEqualTo(String value) {
            addCriterion("nick_name =", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotEqualTo(String value) {
            addCriterion("nick_name <>", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThan(String value) {
            addCriterion("nick_name >", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameGreaterThanOrEqualTo(String value) {
            addCriterion("nick_name >=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThan(String value) {
            addCriterion("nick_name <", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLessThanOrEqualTo(String value) {
            addCriterion("nick_name <=", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameLike(String value) {
            addCriterion("nick_name like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotLike(String value) {
            addCriterion("nick_name not like", value, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameIn(List<String> values) {
            addCriterion("nick_name in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotIn(List<String> values) {
            addCriterion("nick_name not in", values, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameBetween(String value1, String value2) {
            addCriterion("nick_name between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andNickNameNotBetween(String value1, String value2) {
            addCriterion("nick_name not between", value1, value2, "nickName");
            return (Criteria) this;
        }

        public Criteria andHeadImgIsNull() {
            addCriterion("head_img is null");
            return (Criteria) this;
        }

        public Criteria andHeadImgIsNotNull() {
            addCriterion("head_img is not null");
            return (Criteria) this;
        }

        public Criteria andHeadImgEqualTo(String value) {
            addCriterion("head_img =", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotEqualTo(String value) {
            addCriterion("head_img <>", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgGreaterThan(String value) {
            addCriterion("head_img >", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgGreaterThanOrEqualTo(String value) {
            addCriterion("head_img >=", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgLessThan(String value) {
            addCriterion("head_img <", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgLessThanOrEqualTo(String value) {
            addCriterion("head_img <=", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgLike(String value) {
            addCriterion("head_img like", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotLike(String value) {
            addCriterion("head_img not like", value, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgIn(List<String> values) {
            addCriterion("head_img in", values, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotIn(List<String> values) {
            addCriterion("head_img not in", values, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgBetween(String value1, String value2) {
            addCriterion("head_img between", value1, value2, "headImg");
            return (Criteria) this;
        }

        public Criteria andHeadImgNotBetween(String value1, String value2) {
            addCriterion("head_img not between", value1, value2, "headImg");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIsNull() {
            addCriterion("service_type is null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIsNotNull() {
            addCriterion("service_type is not null");
            return (Criteria) this;
        }

        public Criteria andServiceTypeEqualTo(Integer value) {
            addCriterion("service_type =", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeNotEqualTo(Integer value) {
            addCriterion("service_type <>", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeGreaterThan(Integer value) {
            addCriterion("service_type >", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("service_type >=", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeLessThan(Integer value) {
            addCriterion("service_type <", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeLessThanOrEqualTo(Integer value) {
            addCriterion("service_type <=", value, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeIn(List<Integer> values) {
            addCriterion("service_type in", values, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeNotIn(List<Integer> values) {
            addCriterion("service_type not in", values, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeBetween(Integer value1, Integer value2) {
            addCriterion("service_type between", value1, value2, "serviceType");
            return (Criteria) this;
        }

        public Criteria andServiceTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("service_type not between", value1, value2, "serviceType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeIsNull() {
            addCriterion("verify_type is null");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeIsNotNull() {
            addCriterion("verify_type is not null");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeEqualTo(Integer value) {
            addCriterion("verify_type =", value, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeNotEqualTo(Integer value) {
            addCriterion("verify_type <>", value, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeGreaterThan(Integer value) {
            addCriterion("verify_type >", value, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("verify_type >=", value, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeLessThan(Integer value) {
            addCriterion("verify_type <", value, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeLessThanOrEqualTo(Integer value) {
            addCriterion("verify_type <=", value, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeIn(List<Integer> values) {
            addCriterion("verify_type in", values, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeNotIn(List<Integer> values) {
            addCriterion("verify_type not in", values, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeBetween(Integer value1, Integer value2) {
            addCriterion("verify_type between", value1, value2, "verifyType");
            return (Criteria) this;
        }

        public Criteria andVerifyTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("verify_type not between", value1, value2, "verifyType");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andAliasIsNull() {
            addCriterion("alias is null");
            return (Criteria) this;
        }

        public Criteria andAliasIsNotNull() {
            addCriterion("alias is not null");
            return (Criteria) this;
        }

        public Criteria andAliasEqualTo(String value) {
            addCriterion("alias =", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotEqualTo(String value) {
            addCriterion("alias <>", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasGreaterThan(String value) {
            addCriterion("alias >", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasGreaterThanOrEqualTo(String value) {
            addCriterion("alias >=", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasLessThan(String value) {
            addCriterion("alias <", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasLessThanOrEqualTo(String value) {
            addCriterion("alias <=", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasLike(String value) {
            addCriterion("alias like", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotLike(String value) {
            addCriterion("alias not like", value, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasIn(List<String> values) {
            addCriterion("alias in", values, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotIn(List<String> values) {
            addCriterion("alias not in", values, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasBetween(String value1, String value2) {
            addCriterion("alias between", value1, value2, "alias");
            return (Criteria) this;
        }

        public Criteria andAliasNotBetween(String value1, String value2) {
            addCriterion("alias not between", value1, value2, "alias");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonIsNull() {
            addCriterion("business_info_json is null");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonIsNotNull() {
            addCriterion("business_info_json is not null");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonEqualTo(String value) {
            addCriterion("business_info_json =", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonNotEqualTo(String value) {
            addCriterion("business_info_json <>", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonGreaterThan(String value) {
            addCriterion("business_info_json >", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonGreaterThanOrEqualTo(String value) {
            addCriterion("business_info_json >=", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonLessThan(String value) {
            addCriterion("business_info_json <", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonLessThanOrEqualTo(String value) {
            addCriterion("business_info_json <=", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonLike(String value) {
            addCriterion("business_info_json like", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonNotLike(String value) {
            addCriterion("business_info_json not like", value, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonIn(List<String> values) {
            addCriterion("business_info_json in", values, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonNotIn(List<String> values) {
            addCriterion("business_info_json not in", values, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonBetween(String value1, String value2) {
            addCriterion("business_info_json between", value1, value2, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andBusinessInfoJsonNotBetween(String value1, String value2) {
            addCriterion("business_info_json not between", value1, value2, "businessInfoJson");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlIsNull() {
            addCriterion("qrcode_url is null");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlIsNotNull() {
            addCriterion("qrcode_url is not null");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlEqualTo(String value) {
            addCriterion("qrcode_url =", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlNotEqualTo(String value) {
            addCriterion("qrcode_url <>", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlGreaterThan(String value) {
            addCriterion("qrcode_url >", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlGreaterThanOrEqualTo(String value) {
            addCriterion("qrcode_url >=", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlLessThan(String value) {
            addCriterion("qrcode_url <", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlLessThanOrEqualTo(String value) {
            addCriterion("qrcode_url <=", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlLike(String value) {
            addCriterion("qrcode_url like", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlNotLike(String value) {
            addCriterion("qrcode_url not like", value, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlIn(List<String> values) {
            addCriterion("qrcode_url in", values, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlNotIn(List<String> values) {
            addCriterion("qrcode_url not in", values, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlBetween(String value1, String value2) {
            addCriterion("qrcode_url between", value1, value2, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlNotBetween(String value1, String value2) {
            addCriterion("qrcode_url not between", value1, value2, "qrcodeUrl");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzIsNull() {
            addCriterion("qrcode_url_kz is null");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzIsNotNull() {
            addCriterion("qrcode_url_kz is not null");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzEqualTo(String value) {
            addCriterion("qrcode_url_kz =", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzNotEqualTo(String value) {
            addCriterion("qrcode_url_kz <>", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzGreaterThan(String value) {
            addCriterion("qrcode_url_kz >", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzGreaterThanOrEqualTo(String value) {
            addCriterion("qrcode_url_kz >=", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzLessThan(String value) {
            addCriterion("qrcode_url_kz <", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzLessThanOrEqualTo(String value) {
            addCriterion("qrcode_url_kz <=", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzLike(String value) {
            addCriterion("qrcode_url_kz like", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzNotLike(String value) {
            addCriterion("qrcode_url_kz not like", value, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzIn(List<String> values) {
            addCriterion("qrcode_url_kz in", values, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzNotIn(List<String> values) {
            addCriterion("qrcode_url_kz not in", values, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzBetween(String value1, String value2) {
            addCriterion("qrcode_url_kz between", value1, value2, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andQrcodeUrlKzNotBetween(String value1, String value2) {
            addCriterion("qrcode_url_kz not between", value1, value2, "qrcodeUrlKz");
            return (Criteria) this;
        }

        public Criteria andInterestJsonIsNull() {
            addCriterion("interest_json is null");
            return (Criteria) this;
        }

        public Criteria andInterestJsonIsNotNull() {
            addCriterion("interest_json is not null");
            return (Criteria) this;
        }

        public Criteria andInterestJsonEqualTo(String value) {
            addCriterion("interest_json =", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonNotEqualTo(String value) {
            addCriterion("interest_json <>", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonGreaterThan(String value) {
            addCriterion("interest_json >", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonGreaterThanOrEqualTo(String value) {
            addCriterion("interest_json >=", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonLessThan(String value) {
            addCriterion("interest_json <", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonLessThanOrEqualTo(String value) {
            addCriterion("interest_json <=", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonLike(String value) {
            addCriterion("interest_json like", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonNotLike(String value) {
            addCriterion("interest_json not like", value, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonIn(List<String> values) {
            addCriterion("interest_json in", values, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonNotIn(List<String> values) {
            addCriterion("interest_json not in", values, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonBetween(String value1, String value2) {
            addCriterion("interest_json between", value1, value2, "interestJson");
            return (Criteria) this;
        }

        public Criteria andInterestJsonNotBetween(String value1, String value2) {
            addCriterion("interest_json not between", value1, value2, "interestJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonIsNull() {
            addCriterion("advanced_func_info_json is null");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonIsNotNull() {
            addCriterion("advanced_func_info_json is not null");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonEqualTo(String value) {
            addCriterion("advanced_func_info_json =", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonNotEqualTo(String value) {
            addCriterion("advanced_func_info_json <>", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonGreaterThan(String value) {
            addCriterion("advanced_func_info_json >", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonGreaterThanOrEqualTo(String value) {
            addCriterion("advanced_func_info_json >=", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonLessThan(String value) {
            addCriterion("advanced_func_info_json <", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonLessThanOrEqualTo(String value) {
            addCriterion("advanced_func_info_json <=", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonLike(String value) {
            addCriterion("advanced_func_info_json like", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonNotLike(String value) {
            addCriterion("advanced_func_info_json not like", value, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonIn(List<String> values) {
            addCriterion("advanced_func_info_json in", values, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonNotIn(List<String> values) {
            addCriterion("advanced_func_info_json not in", values, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonBetween(String value1, String value2) {
            addCriterion("advanced_func_info_json between", value1, value2, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andAdvancedFuncInfoJsonNotBetween(String value1, String value2) {
            addCriterion("advanced_func_info_json not between", value1, value2, "advancedFuncInfoJson");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdIsNull() {
            addCriterion("preview_open_id is null");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdIsNotNull() {
            addCriterion("preview_open_id is not null");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdEqualTo(String value) {
            addCriterion("preview_open_id =", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdNotEqualTo(String value) {
            addCriterion("preview_open_id <>", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdGreaterThan(String value) {
            addCriterion("preview_open_id >", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdGreaterThanOrEqualTo(String value) {
            addCriterion("preview_open_id >=", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdLessThan(String value) {
            addCriterion("preview_open_id <", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdLessThanOrEqualTo(String value) {
            addCriterion("preview_open_id <=", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdLike(String value) {
            addCriterion("preview_open_id like", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdNotLike(String value) {
            addCriterion("preview_open_id not like", value, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdIn(List<String> values) {
            addCriterion("preview_open_id in", values, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdNotIn(List<String> values) {
            addCriterion("preview_open_id not in", values, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdBetween(String value1, String value2) {
            addCriterion("preview_open_id between", value1, value2, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andPreviewOpenIdNotBetween(String value1, String value2) {
            addCriterion("preview_open_id not between", value1, value2, "previewOpenId");
            return (Criteria) this;
        }

        public Criteria andBindTimeIsNull() {
            addCriterion("bind_time is null");
            return (Criteria) this;
        }

        public Criteria andBindTimeIsNotNull() {
            addCriterion("bind_time is not null");
            return (Criteria) this;
        }

        public Criteria andBindTimeEqualTo(Integer value) {
            addCriterion("bind_time =", value, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeNotEqualTo(Integer value) {
            addCriterion("bind_time <>", value, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeGreaterThan(Integer value) {
            addCriterion("bind_time >", value, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("bind_time >=", value, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeLessThan(Integer value) {
            addCriterion("bind_time <", value, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeLessThanOrEqualTo(Integer value) {
            addCriterion("bind_time <=", value, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeIn(List<Integer> values) {
            addCriterion("bind_time in", values, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeNotIn(List<Integer> values) {
            addCriterion("bind_time not in", values, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeBetween(Integer value1, Integer value2) {
            addCriterion("bind_time between", value1, value2, "bindTime");
            return (Criteria) this;
        }

        public Criteria andBindTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("bind_time not between", value1, value2, "bindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeIsNull() {
            addCriterion("unbind_time is null");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeIsNotNull() {
            addCriterion("unbind_time is not null");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeEqualTo(Integer value) {
            addCriterion("unbind_time =", value, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeNotEqualTo(Integer value) {
            addCriterion("unbind_time <>", value, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeGreaterThan(Integer value) {
            addCriterion("unbind_time >", value, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("unbind_time >=", value, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeLessThan(Integer value) {
            addCriterion("unbind_time <", value, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeLessThanOrEqualTo(Integer value) {
            addCriterion("unbind_time <=", value, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeIn(List<Integer> values) {
            addCriterion("unbind_time in", values, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeNotIn(List<Integer> values) {
            addCriterion("unbind_time not in", values, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeBetween(Integer value1, Integer value2) {
            addCriterion("unbind_time between", value1, value2, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andUnbindTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("unbind_time not between", value1, value2, "unbindTime");
            return (Criteria) this;
        }

        public Criteria andIsDelIsNull() {
            addCriterion("is_del is null");
            return (Criteria) this;
        }

        public Criteria andIsDelIsNotNull() {
            addCriterion("is_del is not null");
            return (Criteria) this;
        }

        public Criteria andIsDelEqualTo(Integer value) {
            addCriterion("is_del =", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelNotEqualTo(Integer value) {
            addCriterion("is_del <>", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelGreaterThan(Integer value) {
            addCriterion("is_del >", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelGreaterThanOrEqualTo(Integer value) {
            addCriterion("is_del >=", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelLessThan(Integer value) {
            addCriterion("is_del <", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelLessThanOrEqualTo(Integer value) {
            addCriterion("is_del <=", value, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelIn(List<Integer> values) {
            addCriterion("is_del in", values, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelNotIn(List<Integer> values) {
            addCriterion("is_del not in", values, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelBetween(Integer value1, Integer value2) {
            addCriterion("is_del between", value1, value2, "isDel");
            return (Criteria) this;
        }

        public Criteria andIsDelNotBetween(Integer value1, Integer value2) {
            addCriterion("is_del not between", value1, value2, "isDel");
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

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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