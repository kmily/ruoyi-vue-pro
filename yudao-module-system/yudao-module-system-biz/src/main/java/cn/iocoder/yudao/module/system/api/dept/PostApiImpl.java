package cn.iocoder.yudao.module.system.api.dept;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.datatranslation.core.DataTranslationHandler;
import cn.iocoder.yudao.module.system.api.dept.dto.PostRespDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.PostDO;
import cn.iocoder.yudao.module.system.service.dept.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * 岗位 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class PostApiImpl implements PostApi, DataTranslationHandler {

    @Resource
    private PostService postService;

    @Override
    public void validPostList(Collection<Long> ids) {
        postService.validatePostList(ids);
    }

    @Override
    public List<PostRespDTO> getPostList(Collection<Long> ids) {
        List<PostDO> list = postService.getPostList(ids);
        return BeanUtils.toBean(list, PostRespDTO.class);
    }

    @Override
    public List<Map<String, Object>> selectByIds(Collection<?> ids) {
        return convertList(getPostList(convertSet(ids, id -> Long.parseLong(id.toString()))), BeanUtil::beanToMap);
    }

}
