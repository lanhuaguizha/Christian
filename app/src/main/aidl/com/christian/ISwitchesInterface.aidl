package com.christian;

import com.christian.data.Switches;

interface ISwitchesInterface {

    Switches getSwitchList();

    void setSwitch(String categoryName, String switchName, String switchValue);
}
