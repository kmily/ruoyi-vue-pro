package cn.iocoder.yudao.module.infra.dal.mysql.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.config.FileConfigPageReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileConfigDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * 文件配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface FileConfigMapper extends BaseMapperX<FileConfigDO> {

    default PageResult<FileConfigDO> selectPage(FileConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FileConfigDO>()
                .likeIfPresent(FileConfigDO::getName, reqVO.getName())
                .eqIfPresent(FileConfigDO::getStorage, reqVO.getStorage())
                .betweenIfPresent(FileConfigDO::getCreateTime, reqVO.getBeginCreateTime(), reqVO.getEndCreateTime())
                .orderByDesc(FileConfigDO::getId));
    }

    default Long selectExistsByUpdateTimeAfter(Date maxUpdateTime){
        FileConfigDO fileConfigDO = selectOne(new LambdaQueryWrapperX<FileConfigDO>()
                .gt(FileConfigDO::getUpdateTime, maxUpdateTime)
                .last(" AND ROWNUM = 1")
                .select(FileConfigDO::getId));
        if (fileConfigDO == null) {
            return null;
        }
        return fileConfigDO.getId();
    }

}
