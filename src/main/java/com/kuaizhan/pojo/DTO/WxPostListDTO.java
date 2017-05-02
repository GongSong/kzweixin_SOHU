package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 微信图文列表返回结果
 * 接口文档: https://mp.weixin.qq.com/wiki/15/8386c11b7bc4cdd1499c572bfe2e95b3.html
 * Created by lorin on 17-3-29.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxPostListDTO {
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("item_count")
    private Integer itemCount;
    @JsonProperty("item")
    private List<Item> items;

    /**
     * 微信图文元素
     *
     * Created by lorin on 17-3-29
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("media_id")
        private String mediaId;
        private Content content;
        @JsonProperty("update_time")
        private Integer updateTime;

        /**
         * 微信图文元素内容
         *
         * Created by lorin on 17-3-29
         */
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Content {
            @JsonProperty("news_item")
            List<WxPostDTO> newsItems;
        }
    }

    public List<PostItem> toPostItemList(long weixinAppid) {
        List<PostItem> postItemList = new LinkedList<>();
        for (Item item: items) {
            PostItem postItem = new PostItem();
            postItem.setWeixinAppid(weixinAppid);
            postItem.setItem(item);
            postItemList.add(postItem);
        }
        return postItemList;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PostItem {
        @JsonProperty("weixin_appid")
        Long weixinAppid;
        Item item;
    }
}
