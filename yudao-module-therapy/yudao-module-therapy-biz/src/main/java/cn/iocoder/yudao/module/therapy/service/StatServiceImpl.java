package cn.iocoder.yudao.module.therapy.service;

import cn.iocoder.yudao.module.therapy.controller.app.vo.ScheduleStateRespVO;
import cn.iocoder.yudao.module.therapy.dal.mysql.survey.TreatmentScheduleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class StatServiceImpl implements StatService {
    @Resource
    private TreatmentScheduleMapper treatmentScheduleMapper;

    @Override
    public List<ScheduleStateRespVO> StatSchedule(Integer dayNum, Long userId) {
        List<ScheduleStateRespVO> rsp = new ArrayList<>();
        for (int i = dayNum; i > 0; i--) {
            Map<String, Object> map = treatmentScheduleMapper.statSchedule(LocalDate.now().plusDays(-i), LocalDate.now().plusDays(1 - i), userId);
            if (Objects.nonNull(map)) {
                ScheduleStateRespVO vo = new ScheduleStateRespVO();
                vo.setDay(LocalDate.now().plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                vo.setNum((Integer) map.getOrDefault("count", 0));
                Integer score = (Integer) map.getOrDefault("score", 0);
                vo.setScore(Math.round(score / vo.getNum()));
                rsp.add(vo);
            }
        }
        return rsp;
    }
}
