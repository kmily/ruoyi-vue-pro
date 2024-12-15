-- 菜单 SQL
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status, component_name)
VALUES ('产品/渠道管理', '', 2, 0, 2912,
        'hao-ka-product', '', 'haoka/product/index', 0, 'HaoKaProduct');

-- 按钮父菜单ID
-- 暂时只支持 MySQL。如果你是 Oracle、PostgreSQL、SQLServer 的话，需要手动修改 @parentId 的部分的代码
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('产品/渠道查询', 'haoka:hao-ka-product:query', 3, 1, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('产品/渠道创建', 'haoka:hao-ka-product:create', 3, 2, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('产品/渠道更新', 'haoka:hao-ka-product:update', 3, 3, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('产品/渠道删除', 'haoka:hao-ka-product:delete', 3, 4, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('产品/渠道导出', 'haoka:hao-ka-product:export', 3, 5, @parentId,
        '', '', '', 0);
