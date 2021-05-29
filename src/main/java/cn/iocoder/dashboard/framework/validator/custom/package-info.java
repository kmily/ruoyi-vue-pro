package cn.iocoder.dashboard.framework.validator.custom;

/**
 * 参数校验实现
 * <p>
 * 功能说明：https://blog.csdn.net/qq_38688267/article/details/115720412
 * <p>
 * 环境搭建：
 * 1、实现{@link cn.iocoder.dashboard.framework.validator.custom.ValidateAnnotationHandler}
 * 2、将实现类注册到{@link cn.iocoder.dashboard.framework.validator.custom.ValidateHandlerHelper}中
 * 3、实现全局异常拦截{@link javax.validation.ConstraintViolationException}异常
 *
 * 使用步骤：
 * 1、给存在需要校验方法的Controller加上{@link javax.validation.Validator} 注解
 * 2、给需要校验的方法的参数加上 {@link javax.validation.Valid} 注解
 * 3、给需要校验的参数属性加上想要校验的注解，
 * 如{@link javax.validation.constraints.NotNull} {@link javax.validation.constraints.NotEmpty}等
 * <p>
 * DEMO:
 * {@link cn.iocoder.dashboard.modules.system.controller.auth.SysAuthController#login(cn.iocoder.dashboard.modules.system.controller.auth.vo.auth.SysAuthLoginReqVO)}
 * <p>
 * 亮点：
 * 校验过程中有实现对集合类型对象的递归校验，实现深层校验
 */