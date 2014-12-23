微信粉丝 %{contextPath}/weixin/userinfo.do
微信配置 %{contextPath}/weixin/token.do
微信官网设置连接 http://test.jfantasy.org/wp/weixin/client/core.do token=haolue_weixin
applicationContext-hibernate.xml 添加 <property name="packagesToScan" value="com.fantasy.wx.bean">
applicationContext-security.xml 添加 <s:http pattern="/weixin/client/**" security="none"/>
添加spring的微信配置 <bean id="eventService" class="com.fantasy.wx.service.EventService" lazy-init="false"/>
添加js/jquery.scroll.js
添加<import resource="classpath:spring/applicationContext-websocket.xml"/>
applicationContext-websocket.xml添加
<websocket:handlers>
  <websocket:mapping path="/weixin/msg" handler="weixinWebSocketEndPoint"/>
</websocket:handlers>