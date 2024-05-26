package cn.iocoder.yudao.module.therapy.controller.admin.survey.vo;


import lombok.Data;

/**
 * 用来反序列化前端传过来题目的json对象
 */
@Data
public class QuestSaveReqVO {
    private String type;
    private String $required;
    private String title;
    private String field;
}
