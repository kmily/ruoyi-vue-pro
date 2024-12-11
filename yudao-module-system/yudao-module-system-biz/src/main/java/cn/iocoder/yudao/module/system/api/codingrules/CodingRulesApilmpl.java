package cn.iocoder.yudao.module.system.api.codingrules;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrules.CodingRulesDO;
import cn.iocoder.yudao.module.system.dal.dataobject.codingrulesdetails.CodingRulesDetailsDO;
import cn.iocoder.yudao.module.system.dal.mysql.codingrules.CodingRulesMapper;
import cn.iocoder.yudao.module.system.dal.mysql.codingrulesdetails.CodingRulesDetailsMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;

/**
 * 编码规则 API 实现类
 *
 * @author panjiabao
 */
@Service
public class CodingRulesApilmpl  implements CodingRulesApi{

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CodingRulesMapper codingRulesMapper;

    @Resource
    private CodingRulesDetailsMapper codingRulesDetailsMapper;

    /**
     * 生成编码规则
     * @author panjiabao
     * @param code 编码规则编号
     * @param preview 是否是预览，true代表是预览模式，不消耗流水号
     * @return java.lang.String 生成的编码
     */
    @Override
    public String genCodingRules(String code,boolean preview) {
        LambdaQueryWrapper<CodingRulesDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CodingRulesDO::getCode, code);
        CodingRulesDO codingRulesDO = codingRulesMapper.selectOne(queryWrapper);
        if (codingRulesDO == null) {
            throw new ServiceException(new ErrorCode(500, String.format("编码为[%s]的编码规则不存在", code)));
        }

        List<CodingRulesDetailsDO> codingRulesDetails = codingRulesDetailsMapper.selectList(
                new LambdaQueryWrapper<CodingRulesDetailsDO>()
                        .eq(CodingRulesDetailsDO::getRuleId, codingRulesDO.getId())
        );

        if (codingRulesDetails.isEmpty()) {
            throw new ServiceException(new ErrorCode(500, String.format("编码为[%s]的编码规则未设置规则明细", code)));
        }

        StringBuilder no = new StringBuilder();
        for (CodingRulesDetailsDO detail : codingRulesDetails) {
            //类型
            String type = detail.getType();
            //设置值
            String value = detail.getValue();
            //长度
            Integer len = detail.getLen();
            //起始值
            Integer initial = detail.getInitial();
            //步长
            Integer stepSize = detail.getStepSize();
            //补位符
            String fillKey = detail.getFillKey();

            switch (type) {
                // 常量
                case "1":
                    no.append(value);
                    break;
                // 日期
                case "2":
                    no.append(formatDate(value));
                    break;
                // 日流水号
                case "3":
                    no.append(generateSerialNumber(code, "yyyyMMdd", initial, stepSize, fillKey, len, Duration.ofDays(1),preview));
                    break;
                // 月流水号
                case "4":
                    no.append(generateSerialNumber(code, "yyyyMM", initial, stepSize, fillKey, len, Duration.ofDays(31),preview));
                    break;
                // 年流水号
                case "5":
                    no.append(generateSerialNumber(code, "yyyy", initial, stepSize, fillKey, len, Duration.ofDays(370),preview));
                    break;
                default:
                    throw new ServiceException(new ErrorCode(500, String.format("不支持的编码规则类型[%s]", type)));
            }
        }
        return no.toString();
    }

    /**
     * 日期格式化
     *
     * @param pattern 格式 yyyyMMdd/yyyyMM...
     * @return java.lang.String
     * @author panjiabao
     */
    private String formatDate(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 生成流水号
     *
     * @param code       编码规则编号
     * @param dateFormat 日期格式
     * @param initial    起始值
     * @param stepSize   步长
     * @param fillKey    补位符
     * @param len        长度
     * @param duration   redis过期时间
     * @param preview 预览模式，true代表是浏览模式，不消耗流水号
     * @return java.lang.String
     * @author panjiabao
     */
    private String generateSerialNumber(String code, String dateFormat, int initial, int stepSize, String fillKey,
                                        int len, Duration duration,boolean preview) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String datePart = sdf.format(new Date());
        String redisKey = code + ":" + datePart;
        Integer currentValue = initial;
        if (!preview){
            currentValue = getCurrentSerialNumber(redisKey, initial, stepSize, duration);
        }
        return flushLeft(fillKey, len, String.valueOf(currentValue));
    }

    /**
     * 获取当前流水号
     *
     * @param redisKey redisKey
     * @param initial  起始值
     * @param stepSize 步长
     * @param duration redis过期时间
     * @return java.lang.Integer 当前流水号
     * @author panjiabao
     */
    private Integer getCurrentSerialNumber(String redisKey, int initial, int stepSize, Duration duration) {
        Object value = redisTemplate.opsForValue().get(redisKey);
        Integer currentValue;
        if (value == null) {
            currentValue = initial;
            redisTemplate.opsForValue().set(redisKey, String.valueOf(currentValue), duration);
        } else {
            currentValue = Integer.valueOf(value.toString()) + stepSize;
            redisTemplate.opsForValue().set(redisKey, String.valueOf(currentValue), duration);
        }
        return currentValue;
    }

    /**
     * 左侧字符补位
     *
     * @param fillKey 补位符
     * @param length  长度
     * @param target  补位值
     * @return java.lang.String 补位后字符
     * @author panjiabao
     */
    private static String flushLeft(String fillKey, int length, String target) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - target.length(); i++) {
            sb.append(fillKey);
        }
        sb.append(target);
        return sb.toString();
    }
}
