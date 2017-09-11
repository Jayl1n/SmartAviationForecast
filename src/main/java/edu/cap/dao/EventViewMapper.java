package edu.cap.dao;

import edu.cap.model.po.*;
import edu.cap.model.vo.EventCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface EventViewMapper {

    int deleteByPrimaryKey(String eventHash);

    int insert(EventView record);

    int insertSelective(EventView record);

    EventView selectByPrimaryKey(String eventHash);

    int updateByPrimaryKeySelective(EventView record);

    int updateByPrimaryKey(EventView record);

    List<EventCount> countEventTypeGroup();

    OverallMerit overallMerit(
            @Param("city") String city, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<EventView> selectByDateAndType(
            @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("type") int type);

    List<HotCity> countEventAreaGroupByDate(
            @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("num") int num);

    List<EventCount> selectCountByAreaAndDate(
            @Param("city") String city, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    CityCount selectSingleCountByAreaAndDate(
            @Param("city") String city, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    EventCountVO countCurrentMonth(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<CityCount> selectChinaMap(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int selectRankByCityAndMonth(
            @Param("city") String city, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<EventView> selectEventForExport(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}