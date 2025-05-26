CREATE TABLE spec_group (
  group_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  group_name VARCHAR(50) NOT NULL,
  cat_id BIGINT NOT NULL,
  show_type TINYINT DEFAULT 1,
  FOREIGN KEY (cat_id) REFERENCES category(cat_id)
);

-- 规格组表的外键约束（分类删除时自动删除关联规格组）
ALTER TABLE spec_group
ADD CONSTRAINT fk_spec_group_category
FOREIGN KEY (cat_id)
REFERENCES category(cat_id)
ON DELETE CASCADE;


TRUNCATE TABLE spec_group;

INSERT INTO spec_group (group_id, group_name, cat_id, show_type) VALUES
(1, '颜色规格', 21, 1),    -- 空调
(2, '能效等级', 21, 1),
(3, '制冷量', 21, 1),
(4, '屏幕尺寸', 30, 1),    -- 智能手机
(5, '处理器型号', 30, 1),
(6, '内存容量', 30, 1),
(7, '传感器类型', 33, 1),  -- 单反相机
(8, '镜头卡口', 33, 1),
(9, '分辨率', 45, 1),      -- 打印机
(10, '纸张尺寸', 45, 1),
(11, '面料材质', 48, 1),   -- 衬衫
(12, '尺码规格', 48, 1),
(13, '电池容量', 36, 1),   -- 智能手表
(14, '防水等级', 36, 1),
(15, '咖啡机类型', 26, 1), -- 榨汁机（注意：此处应为厨房电器子类，可能需要调整 cat_id）
(16, '功率参数', 24, 1),   -- 电饭煲
(17, '容量规格', 22, 1),   -- 冰箱
(18, '洗涤容量', 23, 1),   -- 洗衣机
(19, '焦距范围', 35, 1),   -- 镜头
(20, '无人机类型', 38, 1); -- 无人机