package com.ykbjson.app.wheelview;

import java.io.Serializable;

/**
 * 包名：com.ykbjson.app.wheelview
 * 描述：测试wheelview的item数据
 * 创建者：yankebin
 * 日期：2018/4/10
 */
public class WheelItem implements Serializable {
    private String name;
    private String avatarUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
