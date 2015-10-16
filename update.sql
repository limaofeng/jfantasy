--删除订单的收货手机号字段
ALTER TABLE `MALL_ORDER` DROP COLUMN `ship_phone`;
ALTER TABLE `mem_receiver` DROP COLUMN `phone`;

--测试提交