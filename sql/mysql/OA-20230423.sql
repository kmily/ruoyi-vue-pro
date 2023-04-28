
/*
  OA数据结构
*/
-- ----------------------------
-- 1、客户表
-- ----------------------------
drop table if exists oa_customer;
create table `oa_customer` (
  id                bigint(20)      not null auto_increment    comment 'id',
  customer_name     varchar(100)    not null                   comment '名称',
  customer_type     char(1)         not null                   comment '类型',
  contact_name      varchar(30)                                comment '联系人',
  contact_phone     varchar(11)                                comment '联系电话',
  province          varchar(100)                               comment '省',
  city              varchar(200)                               comment '市',
  district          varchar(200)                               comment '区/县',
  address           varchar(500)                               comment '详细地址',
  bank_name         varchar(100)                               comment '开户行',
  bank_account      varchar(100)                               comment '账户',
  tax_number        varchar(50)                                comment '税号',
  remark            varchar(255)                               comment '备注',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '客户表';

-- ----------------------------
-- 2、考勤打卡
-- ----------------------------
drop table if exists oa_attendance;
create table `oa_attendance` (
  id                bigint(20)      not null auto_increment    comment 'id',
  attendance_type   char(1)         not null                   comment '打卡类型',
  --  上班打卡
  attendance_period varchar(1)                                 comment '打卡时间段',
  work_content      varchar(1000)   default ''                 comment '工作内容',
  --  拜访客户
  customer_id       bigint(20)                                 comment '拜访客户id',
  visit_type        char(1)                                    comment '拜访类型',
  visit_reason      varchar(255)                               comment '拜访事由',
  --  请假
  leave_begin_time  datetime                                   comment '请假开始时间',
  leave_end_time    datetime                                   comment '请假结束时间',
  leave_reason      varchar(255)                               comment '请假事由',
  leave_handover    varchar(255)                               comment '请假工作交接',
  
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '考勤打卡';

-- ----------------------------
-- 3、报销申请表
-- ----------------------------
drop table if exists oa_expenses;
create table `oa_expenses` (
  id                bigint(20)      not null auto_increment    comment 'id',
  expenses_type     char(1)         not null                   comment '报销类型',
  --  展会会费
  exhibit_name      varchar(150)                               comment '展会名称',
  exhibit_begin_date datetime                                  comment '展会开始时间',
  exhibit_end_date  datetime                                   comment '展会结束时间',
  exhibit_address   varchar(200)                               comment '展会地点',
  --  差旅
  customer_id       bigint(20)                                 comment '关联的拜访过的客户',
  --  其它
  fee_remark        varchar(255)                               comment '费用说明',
  
  fee               decimal(10,1)   not null                   comment '报销总费用',
  status            tinyint(1)      not null                   comment '申请单状态 0.草稿 1.审批 2.执行 3.完成',
  approval_status   tinyint(1)      default 0                  comment '审批状态 0.未审批 1.部门主管 2.总经理 3.财务 4.通过 -1.拒绝',
  remark            varchar(255)                               comment '备注',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '报销申请';

-- ----------------------------
-- 4、报销申请-差旅报销明细
-- ----------------------------
drop table if exists oa_expenses_detail;
create table `oa_expenses_detail` (
  id                bigint(20)      not null auto_increment    comment 'id',
  --  明细类型 1.餐饮 2.交通 3.住宿 4.其它
  detail_type       char(1)         not null                   comment '明细类型',
  consume_time      datetime        not null                   comment '消费时间',
  detail_fee        decimal(10,1)   not null                   comment '报销费用',
  detail_remark     varchar(100)                               comment '明细备注',
  expenses_id       bigint(20)      not null                   comment '报销申请id',
  primary key (id)
) engine=innodb auto_increment=1 comment = '报销明细';

-- ----------------------------
-- 5、借支申请
-- ----------------------------
drop table if exists oa_borrow;
create table `oa_borrow` (
  id                bigint(20)      not null auto_increment    comment 'id',
  borrow_reason     varchar(255)                               comment '说明',
  borrow_fee        decimal(10,1)   not null                   comment '借支总费用',
  repayment_fee     decimal(10,1)                              comment '已还款费用',
  status            tinyint(1)      not null                   comment '申请单状态',
  --   0.未审批 1.部门主管 2.总经理 3.财务 4.通过 -1.拒绝
  approval_status   tinyint(1)      default 0                  comment '审批状态',
  remark            varchar(255)                               comment '备注',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '借支申请';

-- ----------------------------
-- 6、产品
-- ----------------------------
drop table if exists oa_product;
create table `oa_product` (
  id                bigint(20)      not null auto_increment    comment 'id',
  product_code      varchar(50)     not null                   comment '产品编码',
  product_type      char(1)         not null                   comment '产品类型',
  product_model     varchar(100)    not null                   comment '产品型号',
  product_unit      varchar(10)     default ''                 comment '单位',
  price             decimal(10,1)   default 0                  comment '单价',
  reserve_price     decimal(10,1)   default 0                  comment '底价',
  remark            varchar(255)                               comment '备注',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '产品';

-- ----------------------------
-- 7、合同
-- ----------------------------
drop table if exists oa_contract;
create table `oa_contract` (
  id                bigint(20)      not null auto_increment    comment 'id',
  contract_no       varchar(100)    not null                   comment '合同编号',
  --   1.三锐 2.广州分公司
  company_type      char(1)         default '1'                comment '公司类型',
  customer_id       bigint(20)      not null                   comment '客户id',
  supplier_user_id  bigint(20)      not null                   comment '供方代表',
  total_fee         decimal(10,1)   default 0                  comment '总款',
  service_fee       decimal(10,1)   default 0                  comment '劳务费',
  commissions       decimal(10,1)   default 0                  comment '佣金',
  other_fee         decimal(10,1)   default 0                  comment '零星费用',
  impl_contact_name varchar(30)                                comment '工程实施联系人',
  impl_contact_phone varchar(11)                               comment '工程实施联系电话',
  --   0.草稿 1.审批 2.执行 3.完成, 4.作废
  status            tinyint(1)      default 0                  comment '合同状态',
  --   0.未审批 1.部门主管 2.客服经理 3.通过 -1.拒绝
  approval_status   tinyint(1)      default 0                  comment '审批状态',
  remark            varchar(255)                               comment '备注',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '合同';

-- ----------------------------
-- 8、产品清单表
-- ----------------------------
drop table if exists oa_product_list;
create table `oa_product_list` (
  id                bigint(20)      not null auto_increment    comment '清单',
  product_id        bigint(20)      not null                   comment '产品id',
  sale_price        decimal(10,1)   not null                   comment '合同价/单价',
  contract_id       bigint(20)      not null                   comment '合同id',
  amount            int unsigned    default 0                  comment '产品数量',
  primary key (id)
)engine=innodb auto_increment=1 comment = '产品清单列表';


-- ----------------------------
-- 9、工程实施列表
-- ----------------------------
drop table if exists oa_project_impl;
create table `oa_project_impl` (
  id                bigint(20)      not null auto_increment    comment 'id',
  contract_id       bigint(20)      not null                   comment '合同id',
  --   1.无需实施 2.软件应用 3.硬件设备 4.接口服务
  impl_scope        char(1)         not null                   comment '实施范围',
  impl_content      varchar(500)    default ''                 comment '实施内容',
  remark            varchar(255)                               comment '备注',
  primary key (id)
)engine=innodb auto_increment=1 comment = '工程实施列表';

-- ----------------------------
-- 10、工程实施日志列表
-- ----------------------------
drop table if exists oa_project_impl_log;
create table `oa_project_impl_log` (
  id                bigint(20)      not null auto_increment    comment 'id',
  log_content       varchar(500)    default ''                 comment '内容',
  contract_id       bigint(20)      not null                   comment '合同id',
  --   1.调研 2.部署 3.上线 4.试运行 5.验收 6.维护 
  impl_status       char(1)         default '1'                comment '工程进度',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  primary key (id)
)engine=innodb auto_increment=1 comment = '工程日志列表';

-- ----------------------------
-- 11、商机
-- ----------------------------
drop table if exists oa_opportunity;
create table `oa_opportunity` (
  id                bigint(20)      not null auto_increment    comment 'id',
  business_title    varchar(100)    not null                   comment '商机标题',
  detail            varchar(500)                               comment '商机详情',
  report_time       datetime                                   comment '上报时间',
  follow_user_id    bigint(20)                                 comment '跟进用户id',
  --   1.未指派 2.未跟进 3.跟进中 4.已完成
  status            char(1)         default '1'                comment '商机状态',
  remark            varchar(255)                               comment '备注',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
)engine=innodb auto_increment=1 comment = '商机';

-- ----------------------------
-- 12、商机跟进日志
-- ----------------------------
drop table if exists oa_opportunity_follow_log;
create table `oa_opportunity_follow_log` (
  id                bigint(20)      not null auto_increment    comment 'id',
  business_id       bigint(20)                                 comment '商机id',
  log_content       varchar(500)                               comment '跟进日志内容',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  primary key (id)
)engine=innodb auto_increment=1 comment = '商机-跟进日志';

-- ----------------------------
-- 13、产品反馈
-- ----------------------------
drop table if exists oa_feedback;
create table `oa_feedback` (
  id                bigint(20)      not null auto_increment    comment 'id',
  customer_name     varchar(100)                               comment '客户名称',
  feedback_content  varchar(500)                               comment '反馈内容',
  contact_name      varchar(30)                                comment '联系人',
  contact_phone     varchar(11)                                comment '联系电话',
  append_files      varchar(500)                               comment '附件',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
)engine=innodb auto_increment=1 comment = '产品反馈';

-- ----------------------------
-- 14、产品反馈-跟进日志
-- ----------------------------
drop table if exists oa_feedback_follow_log;
create table `oa_feedback_follow_log` (
  id                bigint(20)      not null auto_increment    comment 'id',
  feedback_id       bigint(20)      not null                   comment '产品反馈id',
  log_content       varchar(500)                               comment '跟进日志内容',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  primary key (id)
)engine=innodb auto_increment=1 comment = '产品反馈-跟进日志';


-- ----------------------------
-- 15、季度考核
-- ----------------------------
drop table if exists oa_quarterly_assessment;
create table `oa_quarterly_assessment` (
  id                bigint(20)      not null auto_increment    comment 'id',
  --  1.第一季度 2 3 4.第四季度
  quarter           char(1)         default '1'                comment '季度',
  quarter_year      int             not null                   comment '年份', 
  work_content      text                                       comment '本季度工作内容',
  critical_work     text                                       comment '关键工作',
  business_trip     text                                       comment '出差记录',
  next_quarter_work text                                       comment '下季度工作安排',
  append_files      varchar(2000)                              comment '上传附件',
  deleted           bit(1)          not null default b'0'      comment '是否删除',
  creator           varchar(64)     default ''                 comment '创建者',
  createTime        datetime                                   comment '创建时间',
  updater           varchar(64)     default ''                 comment '更新者',
  updateTime        datetime                                   comment '更新时间',
  tenant_id         bigint          not null default 0         comment '租户编号',
  primary key (id)
) engine=innodb auto_increment=1 comment = '季度考核';
