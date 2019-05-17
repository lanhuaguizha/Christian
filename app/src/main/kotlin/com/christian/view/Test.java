package com.christian.view;

import android.os.Build;

import com.christian.data.Switches;

import java.util.List;

public class Test {

    private static List<Switches.SwitchBean> switchBeanList;

    enum SwitchCategory { //开关分类信息
        NET, // 网络切换
        UI, // UI
        LOG // LOG
    }

    enum SwitchName {
        // "name" property for "net" and "log" category
        Launcher, LocalLife, MicroHelper, PPTVTerminalManager, SnisService, UserCenter, PushService,
        // "name" property for "ui" category
        边界动效, 落焦动效, 呼吸动效, 首页视频窗视图状态, 信源小窗视图状态, 媒体中心缩略图, 状态栏天气动画, 天气模板动态背景, 一键设置5s快速屏保时间
    }

    enum SwitchType {
        列表型, 输入型, 输出型;
    }

    enum SwitchValue {
        // "value" property for "net" category
        现网, 测网, 预发布, PMS现网, PASSPORT现网, GBS现网,
        // "value" property for "ui" category
        开, 关, 图片, 落焦播放, 显示播放, 视频, 缩略图,
        // "value" property for "log" category
        V, D, I, W, E
    }

    public static void main(String[] args) {
        String json = "";
        Switches switches = Switches.objectFromData(json);
         switchBeanList = switches.getSwitchBeanList();

        if (switchBeanList.get(0).getValue().isEmpty()) {
            // 先前逻辑
        } else if (switchBeanList.get(0).getValue().equals(SwitchValue.现网.name())) {
            // 切现网
        }
    }

    void setSwitch(String categoryName, String switchName, String switchValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            switchBeanList.stream()
                    .filter(category -> category.equals(categoryName) )
                    .filter(name -> name.equals(switchName))
                    .forEach(value -> value.setValue(switchValue));
        }
    }

        /*
         *
         {
  "switches": [
    {
      "name": "SwitchName.边界动效.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()",
      "category": "net"
    }
  ]
}
         *
         */
}
