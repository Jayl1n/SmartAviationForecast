package edu.cap.service;

import edu.cap.dao.EventViewMapper;
import edu.cap.dao.SqliteClient;
import edu.cap.model.po.*;
import edu.cap.model.vo.CityCountVO;
import edu.cap.model.vo.EventCountVO;
import edu.cap.model.vo.EventFormVO;
import edu.cap.model.vo.OverallMeritVO;
import edu.cap.utils.CalcUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Jaylin on 17-8-23.
 */
@Service
public class APIService {

    @Autowired
    EventViewMapper eventViewMapper;

    @Autowired
    SqliteClient sqliteClient;

    public List<EventCountVO> countEventTypeGroup() {
        List<EventCountVO> eventCountVOList = new ArrayList<>();
        List<EventCount> eventCountList = eventViewMapper.countEventTypeGroup();
        eventCountVOList.add(new EventCountVO("展会",
                                              String.valueOf(eventCountList.get(0).getValue())
        ));
        eventCountVOList.add(new EventCountVO("演唱会",
                                              String.valueOf(eventCountList.get(1).getValue())
        ));
        eventCountVOList.add(new EventCountVO("体育赛事",
                                              String.valueOf(eventCountList.get(2).getValue())
        ));
        eventCountVOList.add(new EventCountVO("异常天气",
                                              String.valueOf(eventCountList.get(3).getValue())
        ));
        eventCountVOList.add(new EventCountVO("政治会议",
                                              String.valueOf(eventCountList.get(4).getValue())
        ));
        eventCountVOList.add(new EventCountVO("其他会议",
                                              String.valueOf(eventCountList.get(5).getValue())
        ));
        return eventCountVOList;
    }

    public List<OverallMerit> overallMerit(String city, int month) {
        //当前时间
        DateTime currentDate = DateTime.now().withMonthOfYear(month);
        //未来一个星期
        DateTime weekDate = currentDate.plusDays(7);
        //未来一个月
        DateTime monthDate = currentDate.plusMonths(1);
        //未来一个季度
        DateTime quarterDate = currentDate.plusMonths(3);

        List<OverallMerit> overallMeritVOList = new ArrayList<>();

        OverallMerit weekMerit = eventViewMapper.overallMerit(city, currentDate.toDate(), weekDate.toDate());
//        overallMeritVOList.addAll(convertToDict(weekMerit, 1));
        if (weekMerit == null) {
            weekMerit = new OverallMerit();
        }
        overallMeritVOList.add(weekMerit);
        OverallMerit monthMerit = eventViewMapper.overallMerit(city, currentDate.toDate(), monthDate.toDate());
        if (monthMerit == null) {
            monthMerit = new OverallMerit();
        }
//        overallMeritVOList.addAll(convertToDict(monthMerit, 2));
        overallMeritVOList.add(monthMerit);
        OverallMerit quarterMerit = eventViewMapper.overallMerit(city, currentDate.toDate(), quarterDate.toDate());
        if (quarterMerit == null) {
            quarterMerit = new OverallMerit();
        }
//        overallMeritVOList.addAll(convertToDict(quarterMerit, 3));
        overallMeritVOList.add(quarterMerit);
        return overallMeritVOList;
    }

    @Deprecated
    public List<OverallMeritVO> convertToDict(OverallMerit overallMerit, Integer series) {
        List<OverallMeritVO> overallMeritVOList = new ArrayList<>();
        overallMeritVOList.add(new OverallMeritVO("主办方级别", String.valueOf(overallMerit.getAvgOrganizerLevel()),
                                                  String.valueOf(series)
        ));
        overallMeritVOList.add(new OverallMeritVO("影响范围", String.valueOf(overallMerit.getAvgMaxInfluence()),
                                                  String.valueOf(series)
        ));
        overallMeritVOList
                .add(new OverallMeritVO("热度", String.valueOf(overallMerit.getAvgHeat()), String.valueOf(series)));
        overallMeritVOList.add(new OverallMeritVO("历史悠久程度", String.valueOf(overallMerit.getAvgHistory()),
                                                  String.valueOf(series)
        ));
        overallMeritVOList.add(new OverallMeritVO("一年内频率", String.valueOf(overallMerit.getAvgFrequencyYear()),
                                                  String.valueOf(series)
        ));

        return overallMeritVOList;
    }

    public List<EventFormVO> eventForm(int month,int type) {
        DateTime currentMinDate = DateTime.now()
                                          .withMonthOfYear(month)
                                          .dayOfMonth()
                                          .withMinimumValue();
        DateTime currentMaxDate = DateTime.now()
                                          .withMonthOfYear(month)
                                          .dayOfMonth()
                                          .withMaximumValue();
        List<EventView> eventViewList = eventViewMapper.selectByDateAndType(currentMinDate.toDate(), currentMaxDate.toDate(),type);

        List<EventFormVO> eventFormVOList = new ArrayList<>();
        for (EventView eventView : eventViewList) {
            eventFormVOList.add(new EventFormVO(eventView.getEventName(),
                                                eventView.getEventArea(),
                                                CalcUtil.calcHeat(eventView)
            ));
        }
        return eventFormVOList;
    }


    public List<List<EventCount>> hot5City(int month) {
        DateTime currentMonthMinDate = DateTime.now().withMonthOfYear(month)
                                               .dayOfMonth().withMinimumValue();
        DateTime currentMonthMaxDate = DateTime.now().withMonthOfYear(month)
                                               .dayOfMonth().withMaximumValue();
        //最热的5个城市
        List<HotCity> hotCities = eventViewMapper
                .countEventAreaGroupByDate(currentMonthMinDate.toDate(), currentMonthMaxDate.toDate(), 5);

        List<List<EventCount>> eventCountList = reformatEventCount(currentMonthMinDate, currentMonthMaxDate, hotCities);
        return eventCountList;
    }

    public List<EventCount> hot10City(int month) {
        DateTime currentMonthMinDate = DateTime.now().withMonthOfYear(month)
                                               .dayOfMonth().withMinimumValue();
        DateTime next3MonthMaxDate = DateTime.now().withMonthOfYear(month)
                                             .plusMonths(3).dayOfMonth().withMaximumValue();
        //最热的10个城市
        List<HotCity> hotCities = eventViewMapper
                .countEventAreaGroupByDate(currentMonthMinDate.toDate(), next3MonthMaxDate.toDate(), 10);

        List<EventCount> eventCountList = new ArrayList<>();
        for (HotCity hotCity : hotCities) {
            List<EventCount> cityEventCountGroupByType = eventViewMapper
                    .selectCountByAreaAndDate(hotCity.getCity(), currentMonthMinDate.toDate(),
                                              next3MonthMaxDate.toDate()
                    );
            int sum = 0;
            for (EventCount eventCount : cityEventCountGroupByType) {
                sum += eventCount.getValue();
            }
            EventCount eventCount = new EventCount(hotCity.getCity(), sum);
            eventCountList.add(eventCount);
        }
        return eventCountList;
    }

    private List<List<EventCount>> reformatEventCount(
            DateTime currentMonthMinDate, DateTime currentMonthMaxDate, List<HotCity> hotCities) {
        List<List<EventCount>> eventCountList = new ArrayList<>();
        for (int i = 0; i < hotCities.size(); i++) {
            List<EventCount> cityEventCount = eventViewMapper
                    .selectCountByAreaAndDate(hotCities.get(i).getCity(), currentMonthMinDate.toDate(),
                                              currentMonthMaxDate.toDate()
                    );
            if (cityEventCount.size() != 6) {
                int[] existEventType = new int[cityEventCount.size()];
                int index = 0;
                for (EventCount eventCount : cityEventCount) {
                    existEventType[index] = (int) eventCount.getType();
                    index++;
                }

                for (int j = 1; j <= 6; j++) {
                    if (!ArrayUtils.contains(existEventType, j)) {
                        cityEventCount.add(new EventCount(cityEventCount.get(0).getCity(), j, 0));
                    }
                }
                cityEventCount.sort((o1, o2) -> {
                    if (o1.getType() > o2.getType()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
            }
            eventCountList.add(cityEventCount);
        }
        return eventCountList;
    }

    public EventCountVO countCurrentMonth() {
        DateTime currentMonthMinDate = DateTime.now().dayOfMonth().withMinimumValue();
        DateTime currentMonthMaxDate = DateTime.now().dayOfMonth().withMaximumValue();
        return eventViewMapper.countCurrentMonth(currentMonthMinDate.toDate(), currentMonthMaxDate.toDate());
    }

    public List<CityCountVO> selectChinaMap(int month) {
        DateTime monthMinDate = DateTime.now().withMonthOfYear(month).dayOfMonth().withMinimumValue();
        DateTime monthMaxDate = DateTime.now().withMonthOfYear(month).dayOfMonth().withMaximumValue();
        List<CityCount> chinaMap = eventViewMapper.selectChinaMap(monthMinDate.toDate(), monthMaxDate.toDate());
        List<CityCountVO> chinaMapVO = new ArrayList<>();
        for (CityCount cityCount : chinaMap) {
            if (StringUtils.isEmpty(sqliteClient.selectLngAndLatByName(cityCount.getCity()))) {
                continue;
            }

            String[] lngAndLat = sqliteClient.selectLngAndLatByName(cityCount.getCity()).split(",");
            float[] value = new float[3];
            value[0] = Float.parseFloat(lngAndLat[0]);
            value[1] = Float.parseFloat(lngAndLat[1]);
            value[2] = cityCount.getCount();
            CityCountVO cityCountVO = new CityCountVO(cityCount.getCity(), value);
            chinaMapVO.add(cityCountVO);
        }
        return chinaMapVO;
    }

    public Map<String, Object> summary(String city, int month) {
        Map<String, Object> summaryMap = new HashMap<>();

        DateTime monthMinDate = DateTime.now().withMonthOfYear(month).dayOfMonth().withMinimumValue();
        DateTime monthMaxDate = DateTime.now().withMonthOfYear(month).dayOfMonth().withMaximumValue();

        DateTime beforeMonthMinDate = DateTime.now().minusMonths(1).dayOfMonth().withMinimumValue();
        DateTime beforeMonthMaxDate = DateTime.now().minusMonths(1).dayOfMonth().withMaximumValue();

        DateTime nextMonthMinDate = DateTime.now().plusMonths(1).dayOfMonth().withMinimumValue();
        DateTime nextMonthMaxDate = DateTime.now().plusMonths(1).dayOfMonth().withMaximumValue();

        //事件总数
        List<EventCount> eventCountList = eventViewMapper
                .selectCountByAreaAndDate(city, monthMinDate.toDate(), monthMaxDate.toDate());
        summaryMap.put("eventCount", eventCountList);

        //热度排名
        int cityRank = eventViewMapper
                .selectRankByCityAndMonth(city, monthMinDate.toDate(), monthMaxDate.toDate());
        summaryMap.put("rank", cityRank);

        //当月
        CityCount currentMonth = eventViewMapper
                .selectSingleCountByAreaAndDate(city, monthMinDate.toDate(), monthMaxDate.toDate());
        summaryMap.put("currentMonthCount", currentMonth.getCount());

        //上月
        CityCount lastMonth = eventViewMapper
                .selectSingleCountByAreaAndDate(city, beforeMonthMinDate.toDate(), beforeMonthMaxDate.toDate());
        float v1 = (float) ((currentMonth.getCount() - lastMonth.getCount()) * 1.0 / currentMonth.getCount());
        summaryMap.put("compareWithLastMonth",
                       v1
        );

        //下月
        CityCount nextMonth = eventViewMapper
                .selectSingleCountByAreaAndDate(city, nextMonthMinDate.toDate(), nextMonthMaxDate.toDate());
        float v = (float) ((currentMonth.getCount() - nextMonth.getCount()) * 1.0 / currentMonth.getCount());
        summaryMap.put("compareWithNextMonth",
                       v
        );

        return summaryMap;
    }
}
