-- 菜单 SQL
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status, component_name)
VALUES ('在售产品管理', '', 2, 0, 2912,
        'on-sale-product', '', 'haoka/onsaleproduct/index', 0, 'OnSaleProduct');

-- 按钮父菜单ID
-- 暂时只支持 MySQL。如果你是 Oracle、PostgreSQL、SQLServer 的话，需要手动修改 @parentId 的部分的代码
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('在售产品查询', 'haoka:on-sale-product:query', 3, 1, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('在售产品创建', 'haoka:on-sale-product:create', 3, 2, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('在售产品更新', 'haoka:on-sale-product:update', 3, 3, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('在售产品删除', 'haoka:on-sale-product:delete', 3, 4, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('在售产品导出', 'haoka:on-sale-product:export', 3, 5, @parentId,
        '', '', '', 0);
