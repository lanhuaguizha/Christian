package com.christian.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Switches implements Parcelable {


    /**
     * net : {"name":"SwitchName.Launcher.ordinal()","type":"SwitchType.列表型.ordinal()","value":"SwitchValue.测网.ordinal()"}
     * ui : [{"name":"SwitchName.边界动效.ordinal()","type":"SwitchType.开关型.ordinal()","value":"SwitchValue.开.ordinal()"},{"name":"SwitchName.落焦动效.ordinal()","type":"SwitchType.开关型.ordinal()","value":"SwitchValue.开.ordinal()"},{"name":"SwitchName.呼吸动效.ordinal()","type":"SwitchType.开关型.ordinal()","value":"SwitchValue.开.ordinal()"},{"name":"SwitchName.首页视频窗视图状态.ordinal()","type":"SwitchType.列表型.ordinal()","value":"SwitchValue.落焦播放.ordinal()"},{"name":"SwitchName.信源小窗视图状态.ordinal()","type":"SwitchType.列表型.ordinal()","value":"SwitchValue.开.ordinal()"},{"name":"SwitchName.媒体中心缩略图.ordinal()","type":"SwitchType.列表型.ordinal()","value":"SwitchValue.视频.ordinal()"},{"name":"SwitchName.状态栏天气动画.ordinal()","type":"SwitchType.开关型.ordinal()","value":"SwitchValue.开.ordinal()"},{"name":"SwitchName.天气模板动态背景.ordinal()","type":"SwitchType.开关型.ordinal()","value":"SwitchValue.开.ordinal()"},{"name":"SwitchName.一键设置5s快速屏保时间.ordinal()","type":"SwitchType.开关型.ordinal()","value":"SwitchValue.开.ordinal()"}]
     * log : {"name":"SwitchName.Launcher.ordinal()","type":"SwitchType.列表型.ordinal()","value":"SwitchValue.E.ordinal()"}
     */

    private SwitchBean net;
    private SwitchBean log;
    private List<SwitchBean> ui;

    public static Switches objectFromData(String str) {

        return new Gson().fromJson(str, Switches.class);
    }

    public SwitchBean getNet() {
        return net;
    }

    public void setNet(SwitchBean net) {
        this.net = net;
    }

    public SwitchBean getLog() {
        return log;
    }

    public void setLog(SwitchBean log) {
        this.log = log;
    }

    public List<SwitchBean> getUi() {
        return ui;
    }

    public void setUi(List<SwitchBean> ui) {
        this.ui = ui;
    }

    public static class SwitchBean implements Parcelable {
        /**
         * name : SwitchName.Launcher.ordinal()
         * type : SwitchType.列表型.ordinal()
         * value : SwitchValue.测网.ordinal()
         */

        private String name;
        private String type;
        private String value;

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
        }

        public SwitchBean() {
        }

        protected SwitchBean(Parcel in) {
            this.name = in.readString();
            this.type = in.readString();
            this.value = in.readString();
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
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.net, flags);
        dest.writeParcelable(this.log, flags);
        dest.writeList(this.ui);
    }

    public Switches() {
    }

    protected Switches(Parcel in) {
        this.net = in.readParcelable(SwitchBean.class.getClassLoader());
        this.log = in.readParcelable(SwitchBean.class.getClassLoader());
        this.ui = new ArrayList<SwitchBean>();
        in.readList(this.ui, SwitchBean.class.getClassLoader());
    }

    public void readFromParcel(Parcel in) {
        this.net = in.readParcelable(SwitchBean.class.getClassLoader());
        this.log = in.readParcelable(SwitchBean.class.getClassLoader());
        this.ui = new ArrayList<SwitchBean>();
        in.readList(this.ui, SwitchBean.class.getClassLoader());
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
