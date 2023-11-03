# 达梦数据库适配

## 达梦测试环境

建议使用官方提供的docker镜像进行测试，参考文档[Docker安装](https://eco.dameng.com/document/dm/zh-cn/start/dm-install-docker.html)

[下载Docker镜像(版本: 20230808)](https://download.dameng.com/eco/dm8/dm8_20230808_rev197096_x86_rh6_64_single.tar)

```shell
docker load -i dm8_20230808_rev197096_x86_rh6_64_single.tar

docker run -d -p 5236:5236 \
    --restart=unless-stopped \
    --name dm8_test \
    --privileged=true \
    -e PAGE_SIZE=16 \
    -e LD_LIBRARY_PATH=/opt/dmdbms/bin \
    -e EXTENT_SIZE=32 \
    -e BLANK_PAD_MODE=1 \
    -e LOG_SIZE=1024 \
    -e UNICODE_FLAG=1 \
    -e LENGTH_IN_CHAR=1 \
    -e INSTANCE_NAME=dm8_test \
    -v $PWD/dm8_test:/opt/dmdbms/data \
    dm8_single:dm8_20230808_rev197096_x86_rh6_64
```

备注：可以尝试使用大小写不敏感配置`-e CASE_SENSITIVE=N`，需要停止并删除容器后，删除`dm8_test`目录，重新`docker run`。

## 更新达梦jdbc版本

`yudao-dependencies/pom.xml`

```xml
<dm8.jdbc.version>8.1.3.62</dm8.jdbc.version>
```

## 数据库导入

为了方便起见，建议使用SYSDBA/SYSDBA001建立用户RUOYI_VUE_PRO/123456
这样就会建立一个名为RUOYI_VUE_PRO的schema，在不指定默认schema的情况下，会使用同名的schema作为默认。执行达梦的sql文件。

## 数据库链接信息

```yaml
# application-local.yaml
url: jdbc:dm://localhost:5236
username: RUOYI_VUE_PRO
password: 123456
```

## DAO改造

由于`domain`是达梦的关键字，因此需要修改`cn.iocoder.yudao.module.system.dal.dataobject.tenant.TenantDO`的相关字段

方法1：直接修改为`domain_`，对应修改数据库字段
方法2：添加注解`@TableField("\"DOMAIN\"")`

备注：可以通过?schema=RUOYI_VUE_PRO来制定默认schema名称，这样就不用建一个RUOYI_VUE_PRO这么憋屈的用户名

## 关于大小写敏感

参考文档[详解 DM 数据库字符串大小写敏感](https://eco.dameng.com/community/article/df11811a02de8e923c2e57ef6597bc62)

## TODO

工作流仍未适配
有价值的参考文章：https://blog.csdn.net/TangBoBoa/article/details/130392495