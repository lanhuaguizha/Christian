package com.christian.view;

import com.google.gson.Gson;

import java.util.List;

public class Test {
    /**
     * name : 陶永强
     * url : R.drawable.the_virgin
     * id : 0
     * nickName : 个性签名：基督徒软件开发者，致力于打造全球最优体验的传福音软件
     * address : {"street":"张江高科","city":"上海","country":"中国"}
     * settings : [{"name":"夜间模式","url":"ic_brightness_medium_black_24dp","desc":"黑暗模式"},{"name":"历史记录","url":"ic_history_black_24dp","desc":"阅读过的文章"},{"name":"我的文章","url":"ic_library_books_black_24dp","desc":"已收藏的文章"},{"name":"设置","url":"R.drawable.ic_settings_black_24dp","desc":"开源声明、关于我们、听众来信、写信给我们"}]
     */

    private String name;
    private String url;
    private int id;
    private String nickName;
    private AddressBean address;
    private List<SettingsBean> settings;

    public static Test objectFromData(String str) {

        return new Gson().fromJson(str, Test.class);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public List<SettingsBean> getSettings() {
        return settings;
    }

    public void setSettings(List<SettingsBean> settings) {
        this.settings = settings;
    }

    public static class AddressBean {
        /**
         * street : 张江高科
         * city : 上海
         * country : 中国
         */

        private String street;
        private String city;
        private String country;

        public static AddressBean objectFromData(String str) {

            return new Gson().fromJson(str, AddressBean.class);
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    public static class SettingsBean {
        /**
         * name : 夜间模式
         * url : ic_brightness_medium_black_24dp
         * desc : 黑暗模式
         */

        private String name;
        private String url;
        private String desc;

        public static SettingsBean objectFromData(String str) {

            return new Gson().fromJson(str, SettingsBean.class);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
