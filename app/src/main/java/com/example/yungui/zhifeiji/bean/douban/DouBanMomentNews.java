package com.example.yungui.zhifeiji.bean.douban;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yungui on 2017/3/20.
 */

public class DouBanMomentNews {

    private int count;
    private int offset;
    private String date;
    private int total;
    private List<PostsBean> posts;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PostsBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostsBean> posts) {
        this.posts = posts;
    }

    /*
    POSTBean
     */

    public static class PostsBean {
        //展示风格
        @Override
        public String toString() {
            return "PostsBean{" +
                    "display_style=" + display_style +
                    ", is_editor_choice=" + is_editor_choice +
                    ", published_time='" + published_time + '\'' +
                    ", original_url='" + original_url + '\'' +
                    ", url='" + url + '\'' +
                    ", short_url='" + short_url + '\'' +
                    ", is_liked=" + is_liked +
                    ", column='" + column + '\'' +
                    ", app_css=" + app_css +
                    ", abstractX='" + abstractX + '\'' +
                    ", date='" + date + '\'' +
                    ", like_count=" + like_count +
                    ", comments_count=" + comments_count +
                    ", created_time='" + created_time + '\'' +
                    ", title='" + title + '\'' +
                    ", share_pic_url='" + share_pic_url + '\'' +
                    ", type='" + type + '\'' +
                    ", id=" + id +
                    ", thumbs=" + thumbs +
                    '}';
        }

        private int display_style;
        private boolean is_editor_choice;
        private String published_time;
        //原链接
        private String original_url;
        //移动端连接
        private String url;
        //内部的短连接
        private String short_url;
        private boolean is_liked;
        //专栏类型
        private String column;
        //页面使用的css样式
        private int app_css;
        @SerializedName("abstract")
        //摘要
        private String abstractX;
        //腹部日期
        private String date;
        private int like_count;
        private int comments_count;
        private String created_time;
        //文章标题
        private String title;
        //以图片形式分享时的连接
        private String share_pic_url;
        //文章类型
        private String type;
        //文章ID
        private int id;
        //图片集合
        private List<ThumbsBean> thumbs;


        public int getDisplay_style() {
            return display_style;
        }

        public void setDisplay_style(int display_style) {
            this.display_style = display_style;
        }

        public boolean isIs_editor_choice() {
            return is_editor_choice;
        }

        public void setIs_editor_choice(boolean is_editor_choice) {
            this.is_editor_choice = is_editor_choice;
        }

        public String getPublished_time() {
            return published_time;
        }

        public void setPublished_time(String published_time) {
            this.published_time = published_time;
        }

        public String getOriginal_url() {
            return original_url;
        }

        public void setOriginal_url(String original_url) {
            this.original_url = original_url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getShort_url() {
            return short_url;
        }

        public void setShort_url(String short_url) {
            this.short_url = short_url;
        }

        public boolean isIs_liked() {
            return is_liked;
        }

        public void setIs_liked(boolean is_liked) {
            this.is_liked = is_liked;
        }

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public int getApp_css() {
            return app_css;
        }

        public void setApp_css(int app_css) {
            this.app_css = app_css;
        }

        public String getAbstractX() {
            return abstractX;
        }

        public void setAbstractX(String abstractX) {
            this.abstractX = abstractX;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getLike_count() {
            return like_count;
        }

        public void setLike_count(int like_count) {
            this.like_count = like_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShare_pic_url() {
            return share_pic_url;
        }

        public void setShare_pic_url(String share_pic_url) {
            this.share_pic_url = share_pic_url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public List<ThumbsBean> getThumbs() {
            return thumbs;
        }

        public void setThumbs(List<ThumbsBean> thumbs) {
            this.thumbs = thumbs;
        }

        /*-----------------------------图片结合-------------------------------*/
        public static class ThumbsBean {
            @Override
            public String toString() {


                return "ThumbsBean{" +
                        "medium=" + medium +
                        ", description='" + description + '\'' +
                        ", large=" + large +
                        ", tag_name='" + tag_name + '\'' +
                        ", small=" + small +
                        ", id=" + id +
                        '}';
            }

            private MediumBean medium;
            private String description;
            private LargeBean large;
            //图片的标签
            private String tag_name;
            private SmallBean small;
            //图片的ID
            private int id;

            public MediumBean getMedium() {
                return medium;
            }

            public void setMedium(MediumBean medium) {
                this.medium = medium;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public LargeBean getLarge() {
                return large;
            }

            public void setLarge(LargeBean large) {
                this.large = large;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }

            public SmallBean getSmall() {
                return small;
            }

            public void setSmall(SmallBean small) {
                this.small = small;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public static class MediumBean {
                @Override
                public String toString() {
                    return "MediumBean{" +
                            "url='" + url + '\'' +
                            ", width=" + width +
                            ", height=" + height +
                            '}';
                }

                /**
                 * url : https://img1.doubanio.com/view/presto/medium/public/t118439.jpg
                 * width : 460
                 * height : 689
                 */

                private String url;
                private int width;
                private int height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }

            public static class LargeBean {
                /**
                 * url : https://img1.doubanio.com/view/presto/large/public/t118439.jpg
                 * width : 460
                 * height : 689
                 */

                private String url;
                private int width;
                private int height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }

            public static class SmallBean {
                /**
                 * url : https://img1.doubanio.com/view/presto/small/public/t118439.jpg
                 * width : 320
                 * height : 479
                 */

                private String url;
                private int width;
                private int height;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }
            }
        }
    }
}
