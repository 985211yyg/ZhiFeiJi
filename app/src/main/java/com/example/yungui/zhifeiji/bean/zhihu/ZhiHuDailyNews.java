package com.example.yungui.zhifeiji.bean.zhihu;

import java.util.ArrayList;


/**
 * Created by yungui on 2017/2/14.
 */

  /*
{
 1. "date": "20170121",

 2. "stories": [
    {
      "images" :  ["http://pic1.zhimg.com/ffcca2b2853f2af791310e6a6d694e80.jpg"],
      "type" :  0,
      "id" :  9165434,
      "ga_prefix" :  "012121",
      "title" :  "谁说普通人的生活就不能精彩有趣呢？"
    },
    ...
    ]
}
 */

public class ZhiHuDailyNews {
    private String date;
    private ArrayList<Question> stories;

    public ArrayList<Question> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Question> stories) {
        this.stories = stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public class Question {
        private ArrayList<String> images;
        private int type;
        private int id;
        private int ga_prefix;
        private String title;
        public ArrayList<String> getImages() {
            return images;
        }

        public void setImages(ArrayList<String> images) {
            this.images = images;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(int prefix) {
            this.ga_prefix = prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "Question{" +
                    "images=" + images +
                    ", type=" + type +
                    ", id=" + id +
                    ", ga_prefix=" + ga_prefix +
                    ", title='" + title + '\'' +
                    '}';
        }


    }

}
