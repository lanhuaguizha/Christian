package com.christian.data;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by paultao on 2018/3/9.
 */

public class dd {


    public static dd objectFromData(String str) {

        return new Gson().fromJson(str, dd.class);
    }

    public static class NavsBean {
        /**
         * subtitle : 马太福音
         * title : 贫穷的人有福了
         * detail : [{"audio":"http://www.kugou.com/song/1m8z7c8.html?frombaidu#hash=2BB958DDA5BCBB7D50D309A2933F4321&album_id=0","content":"When writing native applications, oftentimes we need to access certain functionality that is not included in the Kotlin standard library, such as making HTTP requests, reading and writing from disk, etc.","image":"url"},{"audio":"http://www.kugou.com/song/1m8z7c8.html?frombaidu#hash=2BB958DDA5BCBB7D50D309A2933F4321&album_id=0","content":"When writing native applications, oftentimes we need to access certain functionality that is not included in the Kotlin standard library, such as making HTTP requests, reading and writing from disk, etc.","image":"url"}]
         * relation : [{}]
         * author : 圣灵
         */

        private String subtitle;
        private String title;
        private String author;
        private List<DetailBean> detail;
        private List<RelationBean> relation;

        public static NavsBean objectFromData(String str) {

            return new Gson().fromJson(str, NavsBean.class);
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public List<DetailBean> getDetail() {
            return detail;
        }

        public void setDetail(List<DetailBean> detail) {
            this.detail = detail;
        }

        public List<RelationBean> getRelation() {
            return relation;
        }

        public void setRelation(List<RelationBean> relation) {
            this.relation = relation;
        }

        public static class DetailBean {
            /**
             * audio : http://www.kugou.com/song/1m8z7c8.html?frombaidu#hash=2BB958DDA5BCBB7D50D309A2933F4321&album_id=0
             * content : When writing native applications, oftentimes we need to access certain functionality that is not included in the Kotlin standard library, such as making HTTP requests, reading and writing from disk, etc.
             * image : url
             */

            private String audio;
            private String content;
            private String image;

            public static DetailBean objectFromData(String str) {

                return new Gson().fromJson(str, DetailBean.class);
            }

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }

        public static class RelationBean {
            public static RelationBean objectFromData(String str) {

                return new Gson().fromJson(str, RelationBean.class);
            }
        }
    }
}
