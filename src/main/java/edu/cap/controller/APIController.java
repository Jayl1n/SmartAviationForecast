package edu.cap.controller;

import edu.cap.model.po.EventCount;
import edu.cap.model.po.OverallMerit;
import edu.cap.model.vo.CityCountVO;
import edu.cap.model.vo.EventCountVO;
import edu.cap.model.vo.EventFormVO;
import edu.cap.service.APIService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class APIController {

    @Autowired
    APIService apiService;

    /**
     * 地图接口
     */
    @RequestMapping(value = "/chinamap", method = RequestMethod.GET)
    public List<List<CityCountVO>> chinaMap() {
        List<List<CityCountVO>> chinaMapVO = new ArrayList();
        for (int month = 1; month <= 12; month++) {
            chinaMapVO.add(apiService.selectChinaMap(month));
        }
        return chinaMapVO;
    }

    /**
     * 事件占比接口 | 事件总数接口
     * <p>
     * 输出
     * [{name:"展会",value:"100"}]
     */
    @RequestMapping(value = "/eventcount", method = RequestMethod.GET)
    public List<EventCountVO> eventCount() {
        List<EventCountVO> eventCountVOList = apiService.countEventTypeGroup();
        return eventCountVOList;
    }

    /**
     * 当月事件总数
     */
    @RequestMapping(value = "/currentmonthcount", method = RequestMethod.GET)
    public List<EventCountVO> currentMonthCount() {
        List<EventCountVO> currentMonthCount = new ArrayList<>();
        currentMonthCount.add(apiService.countCurrentMonth());
        return currentMonthCount;
    }

    /**
     * 当月热门城市Top5
     */
    @RequestMapping(value = "/{month}/hot5city", method = RequestMethod.GET)
    public List<List<EventCount>> hot5City(@PathVariable("month") int month) {
        return apiService.hot5City(month);
    }

    /**
     * 未来三个月热门城市Top10
     */
    @RequestMapping(value = "/{month}/hot10city", method = RequestMethod.GET)
    public List<EventCount> hot10City(@PathVariable("month") int month) {
        return apiService.hot10City(month);
    }

    /**
     * 综合评价接口
     * <p>
     * 输出
     * [{ "avgFrequencyYear":1.0, "avgHeat":2.0954, "avgHistory":2.0954, "avgMaxInfluence":2.3779, "avgOrganizerLevel":2.5496 }]
     */
    @RequestMapping(value = "/overallmerit/{month}/{city}", method = RequestMethod.GET)
    public List<OverallMerit> overallMerit(@PathVariable("city") String city, @PathVariable("month") int month) {
        return apiService.overallMerit(city, month);
    }

    /**
     * 事件轮播列表接口
     * <p>
     * 输出
     * [{ "city": "中国", "heat": 688, "name": "xxxxxxxxxxxxxxxxxxx" },
     */
    @RequestMapping(value = "/{month}/{type}/eventlist", method = RequestMethod.GET)
    public List<EventFormVO> eventFormVOList(@PathVariable("month") int month, @PathVariable("type") int type) {
        return apiService.eventForm(month, type);
    }

    /**
     * 评价接口
     */
    @RequestMapping(value = "/summary/{month}/{city}", method = RequestMethod.GET)
    public Map<String, String> summary(@PathVariable("city") String city, @PathVariable("month") int month) {
        String template = "{0}，{1}月城市热度排在第{2}位，将举办{3}事件数量较上月{4}{5,number,percent}，预计未来一个月将{6}{7,number,percent}。";
        Map<String, Object> summaryMap = apiService.summary(city, month);
        List<EventCount> eventCount = (List<EventCount>) summaryMap.get("eventCount");

        if (eventCount.size() == 0) {
            Map<String, String> rs = new HashMap<>();
            template = "{0},{1}月无事件。";
            String summary = MessageFormat.format(template,
                                                  city,
                                                  month
            );
            rs.put("summary", summary);
        }

        StringBuffer eventListSB = new StringBuffer();
        for (EventCount count : eventCount) {
            switch ((int) count.getType()) {
                case 1:
                    eventListSB.append("展会" + count.getValue() + "场，");
                    break;
                case 2:
                    eventListSB.append("演唱会" + count.getValue() + "场，");
                    break;
                case 3:
                    eventListSB.append("体育赛事" + count.getValue() + "场，");
                    break;
                case 5:
                    eventListSB.append("政治会议" + count.getValue() + "场，");
                    break;
                case 6:
                    eventListSB.append("会议" + count.getValue() + "场，");
                    break;
                case 4:
                    eventListSB.append("发生异常天气" + count.getValue() + "次，");
                    break;
            }
        }
        int rank = (int) summaryMap.get("rank");
        float compareWithLastMonth = (float) summaryMap.get("compareWithLastMonth");
        float compareWithNextMonth = (float) summaryMap.get("compareWithNextMonth");
        String compareWithLastMonthFlag = compareWithLastMonth > 0 ? "增加" : "减少";
        String compareWithNextMonthFlag = compareWithNextMonth > 0 ? "增加" : "减少";

        String summary = MessageFormat.format(template,
                                              city,
                                              month,
                                              rank,
                                              eventListSB.toString(),
                                              compareWithLastMonthFlag,
                                              Math.abs(compareWithLastMonth),
                                              compareWithNextMonthFlag,
                                              Math.abs(compareWithNextMonth)
        );
        Map<String, String> rs = new HashMap<>();
        rs.put("summary", summary);
        return rs;
    }


}
