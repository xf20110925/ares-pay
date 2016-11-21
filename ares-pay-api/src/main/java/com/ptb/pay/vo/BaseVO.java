package com.ptb.pay.vo;

import com.ptb.common.enums.DeviceTypeEnum;
import com.ptb.common.enums.PlatformEnum;

/**
 * Created by MyThinkpad on 2016/11/21.
 */
public class BaseVO {
    private DeviceTypeEnum deviceTypeEnum;
    private PlatformEnum platformEnum;

    public DeviceTypeEnum getDeviceTypeEnum() {
        return deviceTypeEnum;
    }

    public void setDeviceTypeEnum(DeviceTypeEnum deviceTypeEnum) {
        this.deviceTypeEnum = deviceTypeEnum;
    }

    public PlatformEnum getPlatformEnum() {
        return platformEnum;
    }

    public void setPlatformEnum(PlatformEnum platformEnum) {
        this.platformEnum = platformEnum;
    }
}
