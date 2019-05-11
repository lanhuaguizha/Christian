package com.christian.view;

import com.christian.data.Switch;

import java.util.List;

public class Test {

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
        开关型, 列表型, 输入型, 输出型
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
        Switch s = Switch.objectFromData(json);
//        Switch s = new Switch();

        Switch.SwitchBean logSwitch = s.getLog();
        System.out.println(logSwitch.getName());
        System.out.println(logSwitch.getType());
        System.out.println(logSwitch.getValue());

        Switch.SwitchBean netSwitch = s.getNet();
        System.out.println(netSwitch.getName());
        System.out.println(netSwitch.getType());
        System.out.println(netSwitch.getValue());

        List<Switch.SwitchBean> uiSwitchList = s.getUi();
        System.out.println(uiSwitchList.get(0));
    }

        /*
         *
         {
  "net": {
    "name": "SwitchName.Launcher.ordinal()",
    "type": "SwitchType.列表型.ordinal()",
    "value": "SwitchValue.测网.ordinal()"
  },
  "ui": [
    {
      "name": "SwitchName.边界动效.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    },
    {
      "name": "SwitchName.落焦动效.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    },
    {
      "name": "SwitchName.呼吸动效.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    },
    {
      "name": "SwitchName.首页视频窗视图状态.ordinal()",
      "type": "SwitchType.列表型.ordinal()",
      "value": "SwitchValue.落焦播放.ordinal()"
    },
    {
      "name": "SwitchName.信源小窗视图状态.ordinal()",
      "type": "SwitchType.列表型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    },
    {
      "name": "SwitchName.媒体中心缩略图.ordinal()",
      "type": "SwitchType.列表型.ordinal()",
      "value": "SwitchValue.视频.ordinal()"
    },
    {
      "name": "SwitchName.状态栏天气动画.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    },
    {
      "name": "SwitchName.天气模板动态背景.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    },
    {
      "name": "SwitchName.一键设置5s快速屏保时间.ordinal()",
      "type": "SwitchType.开关型.ordinal()",
      "value": "SwitchValue.开.ordinal()"
    }
  ],
  "log": {
    "name": "SwitchName.Launcher.ordinal()",
    "type": "SwitchType.列表型.ordinal()",
    "value": "SwitchValue.E.ordinal()"
  }
}
         *
         */
}
