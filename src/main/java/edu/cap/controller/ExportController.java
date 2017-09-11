package edu.cap.controller;

import edu.cap.service.ExportService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;

@RestController
public class ExportController {

    @Autowired
    ExportService exportService;

    /**
     * 导出事件数据接口
     */
    @RequestMapping(value = "/download/{month}", method = RequestMethod.GET)
    public void downloadEventList(HttpServletResponse response, @PathVariable("month") int month) {
        String fileName;
        if (month != 13) {
            fileName = DateTime.now().withMonthOfYear(month)
                               .dayOfMonth()
                               .withMinimumValue()
                               .toString("yyyy-MM-dd")
                    + "--" + DateTime.now().withMonthOfYear(month)
                                     .dayOfMonth()
                                     .withMaximumValue()
                                     .toString("yyyy-MM-dd")
                    + ".xls";
        } else {
            fileName = DateTime.now().toString("yyyy") + "-all.xls";
        }

        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        try (
                HSSFWorkbook hssfWorkbook = exportService.export(month);
                BufferedOutputStream bos = IOUtils.buffer(response.getOutputStream())
        ) {
            hssfWorkbook.write(bos);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
