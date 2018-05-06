package com.example.yungui.zhifeiji.bean.douban;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yungui on 2017/3/17.
 */

public class DouBanMomentStory {
    //展示风格
    private int display_style;
    //内部短连接
    private String short_url;
    @SerializedName("abstract")
    //摘要
    private String abstractX;
    //APP页面应用的样式
    private int app_css;
    private int like_count;
    private String created_time;
    //文章ID
    private int id;
    private boolean is_editor_choice;
    private String original_url;
    //文章的内容
    private String content;
    //分享是连接
    private String share_pic_url;
    //文章的类型
    private String type;
    private boolean is_liked;
    private String published_time;
    //适配移动端的链接
    private String url;
    private AuthorBean author;
    //专栏类型
    private String column;
    private int comments_count;
    //文章的标题
    private String title;
    //文章的图片，及thumbs缩略图的集合
    private ArrayList<ThumbsBean> thumbs;
    private ArrayList<PhotosBean> photos;

    public int getDisplay_style() {
        return display_style;
    }

    public void setDisplay_style(int display_style) {
        this.display_style = display_style;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public int getApp_css() {
        return app_css;
    }

    public void setApp_css(int app_css) {
        this.app_css = app_css;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isIs_editor_choice() {
        return is_editor_choice;
    }

    public void setIs_editor_choice(boolean is_editor_choice) {
        this.is_editor_choice = is_editor_choice;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public boolean isIs_liked() {
        return is_liked;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public String getPublished_time() {
        return published_time;
    }

    public void setPublished_time(String published_time) {
        this.published_time = published_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AuthorBean getAuthor() {
        return author;
    }

    public void setAuthor(AuthorBean author) {
        this.author = author;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ThumbsBean> getThumbs() {
        return thumbs;
    }

    public void setThumbs(ArrayList<ThumbsBean> thumbs) {
        this.thumbs = thumbs;
    }

    public ArrayList<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<PhotosBean> photos) {
        this.photos = photos;
    }

    public static class AuthorBean {

        private boolean is_followed;
        private String editor_notes;
        private String uid;
        private String resume;
        private String url;
        private String avatar;
        private String name;
        private boolean is_special_user;
        private String last_post_time;
        private int n_posts;
        private String alt;
        private String large_avatar;
        private String id;
        private boolean is_auth_author;

        public boolean isIs_followed() {
            return is_followed;
        }

        public void setIs_followed(boolean is_followed) {
            this.is_followed = is_followed;
        }

        public String getEditor_notes() {
            return editor_notes;
        }

        public void setEditor_notes(String editor_notes) {
            this.editor_notes = editor_notes;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isIs_special_user() {
            return is_special_user;
        }

        public void setIs_special_user(boolean is_special_user) {
            this.is_special_user = is_special_user;
        }

        public String getLast_post_time() {
            return last_post_time;
        }

        public void setLast_post_time(String last_post_time) {
            this.last_post_time = last_post_time;
        }

        public int getN_posts() {
            return n_posts;
        }

        public void setN_posts(int n_posts) {
            this.n_posts = n_posts;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getLarge_avatar() {
            return large_avatar;
        }

        public void setLarge_avatar(String large_avatar) {
            this.large_avatar = large_avatar;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isIs_auth_author() {
            return is_auth_author;
        }

        public void setIs_auth_author(boolean is_auth_author) {
            this.is_auth_author = is_auth_author;
        }
    }

    public static class ThumbsBean {

        private MediumBean medium;
        private String description;
        private LargeBean large;
        private String tag_name;
        private SmallBean small;
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
             * url : https://img5.doubanio.com/view/presto/large/public/t117686.jpg
             * width : 600
             * height : 837
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
             * url : https://img5.doubanio.com/view/presto/small/public/t117686.jpg
             * width : 320
             * height : 446
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

    public static class PhotosBean {

        private MediumBeanX medium;
        private String description;
        private LargeBeanX large;
        private String tag_name;
        private SmallBeanX small;
        private int id;

        public MediumBeanX getMedium() {
            return medium;
        }

        public void setMedium(MediumBeanX medium) {
            this.medium = medium;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LargeBeanX getLarge() {
            return large;
        }

        public void setLarge(LargeBeanX large) {
            this.large = large;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public SmallBeanX getSmall() {
            return small;
        }

        public void setSmall(SmallBeanX small) {
            this.small = small;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public static class MediumBeanX {
            /**
             * url : https://img3.doubanio.com/view/presto/medium/public/363021.jpg
             * width : 600
             * height : 837
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

        public static class LargeBeanX {
            /**
             * url : https://img3.doubanio.com/view/presto/large/public/363021.jpg
             * width : 600
             * height : 837
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

        public static class SmallBeanX {
            /**
             * url : https://img3.doubanio.com/view/presto/small/public/363021.jpg
             * width : 320
             * height : 446
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
