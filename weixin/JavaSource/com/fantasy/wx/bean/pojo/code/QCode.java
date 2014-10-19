package com.fantasy.wx.bean.pojo.code;

/**
 * 创建扫描后有微信事件 的二维码的参数
 */
public class QCode {

    /**
     * 二维码类型，QR_SCENE为临时,QR_LIMIT_SCENE为永久
     */
    private String action_name;
    /**
     * 二维码详细信息
     */
    private ActionInfo action_info;
    public static class ActionInfo{
        private Scene scene;

        public Scene getScene() {
            return scene;
        }

        public void setScene(Scene scene) {
            this.scene = scene;
        }
    }
    public static class Scene{
        /**
         * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
         */
        public String scene_id;

        public String getScene_id() {
            return scene_id;
        }

        public void setScene_id(String scene_id) {
            this.scene_id = scene_id;
        }
    }

    public String getAction_name() {
        return action_name;
    }

    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    public ActionInfo getAction_info() {
        return action_info;
    }

    public void setAction_info(ActionInfo action_info) {
        this.action_info = action_info;
    }
}
