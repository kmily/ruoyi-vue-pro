package cn.iocoder.yudao.module.yr.convert.sys.dictTree;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreeCreateReqVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreeExcelVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreeRespVO;
import cn.iocoder.yudao.module.yr.controller.admin.sys.dictTree.vo.QgDictTreeUpdateReqVO;
import cn.iocoder.yudao.module.yr.dal.dataobject.sys.dictTree.QgDictTreeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 业务字典分类 Convert
 *
 * @author alex
 */
@Mapper
public interface QgDictTreeConvert {

    QgDictTreeConvert INSTANCE = Mappers.getMapper(QgDictTreeConvert.class);

    QgDictTreeDO convert(QgDictTreeCreateReqVO bean);

    QgDictTreeDO convert(QgDictTreeUpdateReqVO bean);

    QgDictTreeRespVO convert(QgDictTreeDO bean);

    List<QgDictTreeRespVO> convertList(List<QgDictTreeDO> list);

    PageResult<QgDictTreeRespVO> convertPage(PageResult<QgDictTreeDO> page);

    List<QgDictTreeExcelVO> convertList02(List<QgDictTreeDO> list);

}
