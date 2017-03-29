package com.kuaizhan.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 微信图文返回结果
 *
 * Created by lorin on 17-3-29.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {
    @JsonProperty("total_count")
    private int totalCount;
    @JsonProperty("item_count")
    private int itemCount;
    @JsonProperty("item")
    private List<Item> items;

    public PostDTO() {
        super();
    }

    public PostDTO(int totalCount, int itemCount, List<Item> items) {
        this.totalCount = totalCount;
        this.itemCount = itemCount;
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(
                "Post{" +
                "totalCount=" + totalCount +
                ", itemCount=" + itemCount
        );
        if (items != null) {
            sb.append(", items={");
            for (int i = 0, size = items.size(); i < size; ++i) {
                if (i != 0) sb.append(", ");
                sb.append(items.get(i).toString());
            }
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 微信图文元素
     *
     * Created by lorin on 17-3-29
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("media_id")
        private String mediaId;
        private Content content;
        @JsonProperty("update_time")
        private String updateTime;

        public Item() {
            super();
        }

        public Item(String mediaId, Content content, String updateTime) {
            this.mediaId = mediaId;
            this.content = content;
            this.updateTime = updateTime;
        }

        public String getMediaId() {
            return mediaId;
        }

        public void setMediaId(String mediaId) {
            this.mediaId = mediaId;
        }

        public Content getContent() {
            return content;
        }

        public void setContent(Content content) {
            this.content = content;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String toString() {
            return "Item{" +
                    "mediaId=" + mediaId +
                    ", content=" + content.toString() +
                    ", updateTime=" + updateTime +
                    "}";
        }

        /**
         * 微信图文元素内容
         *
         * Created by lorin on 17-3-29
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Content {
            @JsonProperty("news_item")
            List<NewsItem> newsItems;

            public Content() {
                super();
            }

            public Content(List<NewsItem> newsItems) {
                this.newsItems = newsItems;
            }

            public List<NewsItem> getNewsItems() {
                return newsItems;
            }

            public void setNewsItems(List<NewsItem> newsItems) {
                this.newsItems = newsItems;
            }

            public String toString() {
                StringBuilder sb = new StringBuilder("Content{");
                if (newsItems != null) {
                    sb.append("newsItems={");
                    for (int i = 0, size = newsItems.size(); i < size; ++i) {
                        if (i != 0) sb.append(", ");
                        sb.append(newsItems.get(i).toString());
                    }
                    sb.append("}");
                }
                sb.append("}");
                return sb.toString();
            }

            /**
             * 微信图文
             *
             * Created by lorin on 17-3-29
             */
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class NewsItem {
                private String title;
                @JsonProperty("thumb_media_id")
                private String thumbMediaId;
                @JsonProperty("show_cover_pic")
                private String showCoverPic;
                private String author;
                private String digest;
                private String content;
                private String url;
                @JsonProperty("content_source_url")
                private String contentSourceUrl;

                public NewsItem() {
                    super();
                }

                public NewsItem(String title, String thumbMediaId, String showCoverPic, String author, String digest, String content, String url, String contentSourceUrl) {
                    this.title = title;
                    this.thumbMediaId = thumbMediaId;
                    this.showCoverPic = showCoverPic;
                    this.author = author;
                    this.digest = digest;
                    this.content = content;
                    this.url = url;
                    this.contentSourceUrl = contentSourceUrl;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getThumbMediaId() {
                    return thumbMediaId;
                }

                public void setThumbMediaId(String thumbMediaId) {
                    this.thumbMediaId = thumbMediaId;
                }

                public String getShowCoverPic() {
                    return showCoverPic;
                }

                public void setShowCoverPic(String showCoverPic) {
                    this.showCoverPic = showCoverPic;
                }

                public String getAuthor() {
                    return author;
                }

                public void setAuthor(String author) {
                    this.author = author;
                }

                public String getDigest() {
                    return digest;
                }

                public void setDigest(String digest) {
                    this.digest = digest;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getContentSourceUrl() {
                    return contentSourceUrl;
                }

                public void setContentSourceUrl(String contentSourceUrl) {
                    this.contentSourceUrl = contentSourceUrl;
                }

                public String toString() {
                    return "NewsItem{" +
                            "title=" + title +
                            ", thumbMediaId=" + thumbMediaId +
                            ", showCoverPic=" + showCoverPic +
                            ", author=" + author +
                            ", digest=" + digest +
                            ", content=" + content +
                            ", url=" + url +
                            ", contentSourceUrl=" + contentSourceUrl +
                            "}";
                }
            }
        }
    }
}
