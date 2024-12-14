-- 菜单 SQL


INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status, component_name)
VALUES ('好卡案例管理', '', 2, 0, 2912,
        'demo', '', 'haoka/demo/index', 0, 'HaokaDemo');

-- 按钮父菜单ID
-- 暂时只支持 MySQL。如果你是 Oracle、PostgreSQL、SQLServer 的话，需要手动修改 @parentId 的部分的代码
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('好卡案例查询', 'haoka:demo:query', 3, 1, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('好卡案例创建', 'haoka:demo:create', 3, 2, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('好卡案例更新', 'haoka:demo:update', 3, 3, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('好卡案例删除', 'haoka:demo:delete', 3, 4, @parentId,
        '', '', '', 0);
INSERT INTO system_menu(name, permission, type, sort, parent_id,
                        path, icon, component, status)
VALUES ('好卡案例导出', 'haoka:demo:export', 3, 5, @parentId,
        '', '', '', 0);
