package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 微信图文返回结果
 *
 * Created by lorin on 17-3-29.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {
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
            List<NewsItem> newsItems;

            /**
             * 微信图文
             *
             * Created by lorin on 17-3-29
             */
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class NewsItem {
                private String title;
                @JsonProperty("thumb_media_id")
                private String thumbMediaId;
                @JsonProperty("thumb_url")
                private String thumbUrl;
                @JsonProperty("show_cover_pic")
                private Short showCoverPic;
                private String author;
                private String digest;
                private String content;
                private String url;
                @JsonProperty("content_source_url")
                private String contentSourceUrl;
            }
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
