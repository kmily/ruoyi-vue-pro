package cn.iocoder.yudao.module.yr.enums;

/**
 * QgDictTreeConstants 业务字典分类树的常量类
 *
 * @author alex
 */
public interface QgDictTreeConstants {

     Long TREE_TOP_PARENTID = 0l; // 顶级节点的父节点
     Integer TREE_STEP_LEVEL=1; //树的递增层级
     String SEP=">"; //间隔符
     Integer IS_READ_TRUE = 1;//只读标记
     String TREE_TOP_NAME="全部分类";//顶点名称
     String DIC_EDU_STUDY_FORM="edu_study_form";//学习形式
     String DIC_EDU_PROVINCE="edu_province";//省份

     Integer TREE_TYPE_STUDYFORM=2;//学习形式
     Integer TREE_TYPE_PROVINCE=3;//省份
     Integer TREE_TYPE_SCHOOL=4;//学校




}
