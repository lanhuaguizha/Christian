package com.christian.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Switches implements Parcelable {

    private List<SwitchBean> switchBeanList;

    public static Switches objectFromData(String str) {

        return new Gson().fromJson(str, Switches.class);
    }

    public List<SwitchBean> getSwitchBeanList() {
        return switchBeanList;
    }

    public void setSwitchBeanList(List<SwitchBean> switchBeanList) {
        this.switchBeanList = switchBeanList;
    }

    public static class SwitchBean implements Parcelable {

        private String name;
        private String type;
        private String value;
        private String category;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public static SwitchBean objectFromData(String str) {

            return new Gson().fromJson(str, SwitchBean.class);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.type);
            dest.writeString(this.value);
            dest.writeString(this.category);
        }

        public SwitchBean() {
        }

        protected SwitchBean(Parcel in) {
            this.name = in.readString();
            this.type = in.readString();
            this.value = in.readString();
            this.category = in.readString();
        }

        public static final Creator<SwitchBean> CREATOR = new Creator<SwitchBean>() {
            @Override
            public SwitchBean createFromParcel(Parcel source) {
                return new SwitchBean(source);
            }

            @Override
            public SwitchBean[] newArray(int size) {
                return new SwitchBean[size];
            }
        };

        public void readFromParcel(Parcel in) {
            this.name = in.readString();
            this.type = in.readString();
            this.value = in.readString();
            this.category = in.readString();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.switchBeanList);
    }

    public Switches() {
    }

    protected Switches(Parcel in) {
        this.switchBeanList = new ArrayList<SwitchBean>();
        in.readList(this.switchBeanList, SwitchBean.class.getClassLoader());
    }

    public void readFromParcel(Parcel in) {
        this.switchBeanList = new ArrayList<SwitchBean>();
        in.readList(this.switchBeanList, SwitchBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Switches> CREATOR = new Parcelable.Creator<Switches>() {
        @Override
        public Switches createFromParcel(Parcel source) {
            return new Switches(source);
        }

        @Override
        public Switches[] newArray(int size) {
            return new Switches[size];
        }
    };
}
