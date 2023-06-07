# 基于微信小程序的OA系统开发
## 一、相关资源
### 项目地址
https://gitee.com/gz3ray/ruoyi-vue-pro-oa
### 后台地址
* 公网访问地址:https://oa.3ray.info:23457
* 内网访问地址:http://192.168.0.197:8202
#### 登录信息
* 租户名: 三锐科技
* 用户名: sanray
* 密码: 3ray6666

#### APP登录功能测试步骤
1. 登录后台新建个人账户
2. APP 使用账户密码登录后绑定微信小程序账户，之后可以使用微信一键登录

#### 测试接口地址
基础设施 ->系统接口 -> oa
1. ~~用户APP-认证 -> 账号+密码登录~~ 
2. 用户APP-社交用户 -> 社交绑定，使用code授权码 
3. 用户APP-通知公告 -> 获取通知公告列表
4. 用户APP-认证 -> 修改密码

#注意事项：
> 1. 请求Header需要添加参数租户id tenant-id， 默认值 三锐科技 = 150

#更新日志
1.APP端禁用用户名+密码方式登录，增加手机号验证码登录
