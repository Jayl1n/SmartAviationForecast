# -*- coding: utf-8 -*-
import scrapy
from scrapy import cmdline

from SmartAviationForecast.items import EventItem
from utils import parser
from utils.cachemanager import CacheManager


class CnweatherSpider(scrapy.Spider):
    name = 'cnweather'
    allowed_domains = ['weather.com.cn']
    start_urls = ['http://www.weather.com.cn/data/alarm_xml/alarminfo.xml']

    cacheManager = CacheManager(name)

    def parse(self, response):
        for station in response.xpath("//Station"):
            # 城市名
            stationName = station.xpath("@stationName").extract()[0]
            # 预警类型
            signalType = station.xpath("@signalType").extract()[0]
            # 蓝黄橙红
            signalLevel = station.xpath("@signalLevel").extract()[0]
            # 发布时间
            issueTime = station.xpath("@issueTime").extract()[0]
            # 结束时间
            relieveTime = station.xpath("@relieveTime").extract()[0]
            # 预警内容
            issueContent = station.xpath("@issueContent").extract()[0]

            content = stationName + signalType + signalLevel + issueTime + relieveTime

            if self.cacheManager.isExist(content):
                continue
            else:
                self.cacheManager.add(content)

            item = EventItem()
            item['event_name'] = stationName + signalType + signalLevel + u'预警'
            item['event_area'] = stationName
            item['event_type'] = u'异常天气'
            item['event_content'] = issueContent
            item['event_begin_date'] = parser.date_parser.parse(issueTime).date()
            item['event_finish_date'] = parser.date_parser.parse(relieveTime).date()
            yield item

    def close(self, spider, reason):
        self.cacheManager.saveCache()
        return super(CnweatherSpider, spider).close(spider, reason)


if __name__ == '__main__':
    cmdline.execute('scrapy crawl cnweather'.split())
