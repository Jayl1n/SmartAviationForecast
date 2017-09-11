package edu.cap.service;

import com.sun.istack.internal.Nullable;
import edu.cap.dao.EventViewMapper;
import edu.cap.model.po.EventView;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.ibatis.io.Resources;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Jaylin on 17-9-5.
 */
@Service
public class ExportService {

    @Autowired
    EventViewMapper eventViewMapper;

    /**
     * 读取数据库并生成excel
     *
     * @return HSSFWorkbook excel
     */
    public HSSFWorkbook export(int month) {
        Date startDate;
        Date endDate;
        if (month != 13) {
            startDate = DateTime.now().withMonthOfYear(month).dayOfMonth().withMinimumValue().toDate();
            endDate = DateTime.now().withMonthOfYear(month).dayOfMonth().withMaximumValue().toDate();
        } else {
//            startDate = DateTime.now().monthOfYear().withMinimumValue()
//                                .dayOfMonth().withMinimumValue();
//            endDate = DateTime.now().monthOfYear().withMaximumValue()
//                              .dayOfMonth().withMaximumValue();
            startDate = null;
            endDate = null;
        }

        //查询结果
        List<EventView> eventList = eventViewMapper.selectEventForExport(startDate, endDate);

        POIFSFileSystem fs;
        HSSFWorkbook wb = null;
        try {
            fs = new POIFSFileSystem(new FileInputStream(
                    Resources.getResourceAsFile("export.xls")
            ));
            wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            //起始行号
            int startRowNumber = 2;
            //字段数
            int columnCount = 34;
            for (EventView eventView : eventList) {
                HSSFRow row = sheet.createRow(startRowNumber);
                for (int i = 0; i < columnCount; i++) {
                    row.createCell(i).setCellValue(0);
                }
                for (int i = 0; i < columnCount; i++) {
                    //Hash
                    row.getCell(0).setCellValue(eventView.getEventHash());
                    //名称
                    row.getCell(1).setCellValue(eventView.getEventName());
                    //开始日期
                    row.getCell(2).setCellValue(DateFormatUtils.format(eventView.getEventBeginDate(), "yyyy-MM-dd"));
                    //结束如期
                    row.getCell(3).setCellValue(DateFormatUtils.format(eventView.getEventFinishDate(), "yyyy-MM-dd"));
                    //地点
                    row.getCell(4).setCellValue(eventView.getEventArea());
                    //主办方级别
                    Integer eventOrganizerLevel = eventView.getEventOrganizerLevel();
                    row.getCell(5 + eventOrganizerLevel).setCellValue(1);
                    //主办方类型
                    Integer eventOrganizerType = eventView.getEventOrganizerType();
                    row.getCell(9 + eventOrganizerType).setCellValue(1);
                    //影响年龄段
                    Integer eventInfluenceAge = eventView.getEventInfluenceAge() - 1;
                    row.getCell(13 + eventInfluenceAge - 1).setCellValue(1);
                    //影响社会群体
                    Integer eventInfluenceType = eventView.getEventInfluenceType() - 1;
                    row.getCell(17 + eventInfluenceType).setCellValue(1);
                    //最大影响范围
                    Integer eventMaxInfluence = eventView.getEventMaxInfluence() - 1;
                    row.getCell(19 + eventMaxInfluence).setCellValue(1);
                    //事件类型
                    Integer eventType = eventView.getEventType() - 1;
                    row.getCell(24 + eventType).setCellValue(1);
                    //有无固定参与人群
                    row.getCell(30).setCellValue(eventView.getEventHasFixedPopulation());
                    //热度
                    row.getCell(31).setCellValue(eventView.getEventHeat());
                    //历史悠久程度
                    int eventHistory = eventView.getEventHistory();
                    row.getCell(32).setCellValue(eventHistory);
                    //一年内频率
                    row.getCell(33).setCellValue(eventView.getEventFrequencyYear());
                }
                startRowNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

}
