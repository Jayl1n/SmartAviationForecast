package edu.cap.utils;

import edu.cap.model.po.EventView;

/**
 * Created by Jaylin on 17-8-24.
 */
public class CalcUtil {

    /**
     * 计算相对热度
     * <p>
     * 计算方法
     * 相对热度 = ((主办方级别 + 影响范围 ) * 0.2 + (一年内频率 + 历史悠久程度) * 0.1 + 热度 * 0.6) / 4.2 * 100
     *
     * @param eventView
     * @return 相对热度
     */
    public static int calcHeat(EventView eventView) {
        int relativeHeat = (int) ((((eventView.getEventOrganizerLevel() + eventView.getEventMaxInfluence()) * 0.2
                + (eventView.getEventFrequencyYear() + eventView.getEventHistory()) * 0.1
                + eventView.getEventHeat() * 0.6)) / 4.2 * 100);
        return relativeHeat;
    }
}
