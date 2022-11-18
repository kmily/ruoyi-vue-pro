package cn.iocoder.yudao.module.yr.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Qg 错误码枚举类
 *
 * Qg 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 字典类型 1002016000 ==========
    ErrorCode QG_DICT_TYPE_NOT_EXISTS = new ErrorCode(1002016001, "当前业务字典类型不存在");
    ErrorCode QG_DICT_TYPE_NOT_ENABLE = new ErrorCode(1002016002, "业务字典类型不处于开启状态，不允许选择");
    ErrorCode QG_DICT_TYPE_NAME_DUPLICATE = new ErrorCode(1002016003, "已经存在该名字的业务字典类型");
    ErrorCode QG_DICT_TYPE_TYPE_DUPLICATE = new ErrorCode(1002016004, "已经存在该类型的业务字典类型");
    ErrorCode QG_DICT_TYPE_HAS_CHILDREN = new ErrorCode(1002016005, "无法删除，该字典类型还有业务字典数据");

    // ========== 字典数据 1002017000 ==========
    ErrorCode QG_DICT_DATA_NOT_EXISTS = new ErrorCode(1002017001, "当前业务字典数据不存在");
    ErrorCode QG_DICT_DATA_NOT_ENABLE = new ErrorCode(1002017002, "业务字典数据({})不处于开启状态，不允许选择");
    ErrorCode QG_DICT_DATA_VALUE_DUPLICATE = new ErrorCode(1002017003, "已经存在该值的业务字典数据");

    // ========== 字典tree数据 1002018000 ==========
    ErrorCode QgDict_TREE_NOT_EXISTS = new ErrorCode(1002018001, "业务字典分类不存在");

    // ========== 店面 1002019000 ==========
    ErrorCode SYS_SHOP_NOT_EXISTS = new ErrorCode(1002019001, "店面不存在");
}
