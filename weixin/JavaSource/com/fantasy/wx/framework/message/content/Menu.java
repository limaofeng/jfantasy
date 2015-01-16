package com.fantasy.wx.framework.message.content;

import com.fantasy.framework.util.common.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 微信菜单
 */
public class Menu {

    public static enum MenuType {
        /**
         * 点击推事件 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event	的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
         */
        CLICK("click"),
        /**
         * 跳转URL 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
         */
        VIEW("view"),
        /**
         * 扫码推事件 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
         */
        SCANCODE_PUSH("scancode_push"),
        /**
         * 扫码推事件且弹出“消息接收中”提示框 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
         */
        SCANCODE_WAITMSG("scancode_waitmsg"),
        /**
         * 弹出系统拍照发图
         * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
         */
        PIC_SYSPHOTO("pic_sysphoto"),
        /**
         * 弹出拍照或者相册发图  用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
         */
        PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
        /**
         * 弹出微信相册发图器 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
         */
        PIC_WEIXIN("pic_weixin"),
        /**
         * 弹出地理位置选择器 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
         */
        LOCATION_SELECT("location_select");

        private String value;

        private MenuType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private MenuType type;
    private String name;
    private String key;
    private String url;
    private List<Menu> children = new ArrayList<Menu>();

    private Menu(MenuType type, String name) {
        this.type = type;
        this.name = name;
    }

    public Menu(String name, String key, Menu... menus) {
        this(MenuType.CLICK, name, key, menus);
    }

    public Menu(String name, Menu... menus) {
        this(MenuType.CLICK, name, "", menus);
    }

    public Menu(MenuType type, String name, String urlOrKey, Menu... menus) {
        this(type, name);
        if (StringUtil.isNotBlank(urlOrKey)) {
            if (this.type == MenuType.VIEW) {
                this.url = urlOrKey;
            } else {
                this.key = urlOrKey;
            }
        }
        if (menus.length != 0) {
            this.children = Arrays.asList(menus);
        }
    }

    /**
     * 点击推事件 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event	的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
     *
     * @param name 名称
     * @param key  key
     * @return Menu
     */
    public static Menu click(String name, String key) {
        return new Menu(MenuType.CLICK, name, key);
    }

    /**
     * 跳转URL 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
     *
     * @param name 名称
     * @param url  URL
     * @return Menu
     */
    public static Menu view(String name, String url) {
        return new Menu(MenuType.VIEW, name, url);
    }

    /**
     * 扫码推事件 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
     *
     * @param name 名称
     * @return Menu
     */
    public static Menu scancodePush(String name) {
        return new Menu(MenuType.SCANCODE_PUSH, name);
    }

    /**
     * 扫码推事件且弹出“消息接收中”提示框 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
    public static Menu scancodeWaitmsg(String name) {
        return new Menu(MenuType.SCANCODE_WAITMSG, name);
    }

    /**
     * 弹出系统拍照发图
     * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
    public static Menu picSysphoto(String name) {
        return new Menu(MenuType.PIC_SYSPHOTO, name);
    }

    /**
     * 弹出拍照或者相册发图  用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
     *
     * @param name 名称
     * @return Menu
     */
    public static Menu picPhotoOrAlbum(String name) {
        return new Menu(MenuType.PIC_PHOTO_OR_ALBUM, name);
    }

    /**
     * 弹出微信相册发图器 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
    public static Menu picWeixin(String name) {
        return new Menu(MenuType.PIC_WEIXIN, name);
    }

    /**
     * 弹出地理位置选择器 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
    public static Menu locationSelect(String name) {
        return new Menu(MenuType.LOCATION_SELECT, name);
    }


    public MenuType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getUrl() {
        return url;
    }

    public List<Menu> getChildren() {
        return children;
    }

}
