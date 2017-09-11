# -*- coding: utf-8 -*-
import requests
import scrapy
from parsel import Selector
from scrapy import Request, cmdline

from SmartAviationForecast.items import EventItem
from utils import parser, logger
from utils.cachemanager import CacheManager


class Eshow365Spider(scrapy.Spider):
    name = 'eshow365'
    allowed_domains = ['eshow365.com']
    start_urls = ['http://www.eshow365.com/zhanhui/0-0-33-20000101/20201231/']

    cacheManager = CacheManager(name)

    def parse(self, response):
        showList = requests.post(
                url='http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx',
                data={
                    'starttime': '2015/1/1',
                    'startendtime': '2020/12/31',
                    'tag': 0
                }
        )
        tree = Selector(showList.text)
        maxPage = int(tree.xpath('//*[@id="pagestr"]/li[5]/select/option[last()]/@value').extract()[0])

        for pageNum in range(1, maxPage):
            # for pageNum in range(1, maxPage):
            # logger.d('-----------------当前爬行第%d页-----------------' % pageNum)
            r = requests.post(
                    url='http://www.eshow365.com/ZhanHui/Ajax/AjaxSearcherV3.aspx',
                    data={
                        'starttime': '2016/1/1',
                        'startendtime': '2020/12/31',
                        'tag': 0,
                        'page': pageNum
                    }
            )
            page = Selector(r.text)
            try:
                for i in xrange(1, 31):
                    eventSrcUrl = 'http://www.eshow365.com' + \
                                  page.xpath("//div[@class='sslist']/p[1]/a/@href").extract()[i]
                    eventArea = page.xpath("//div[@class='sslist']/p[4]/text()").extract()[i]

                    if self.cacheManager.isExist(eventSrcUrl):
                        continue
                    else:
                        self.cacheManager.add(eventSrcUrl)

                    yield Request(eventSrcUrl, callback=self.parseDetailPage,
                                  meta={'city': eventArea})
            except IndexError:
                continue

    def parseDetailPage(self, response):
        detail = response.css('.zhxxcontent>p::text')
        showTime = detail.extract()[0]
        event_name = response.css('.zhmaincontent>h1::text').extract()[0]
        event_city = response.meta['city']
        event_type = u'展会'
        event_begin_date = parser.date(showTime)['begin_date']
        event_finish_date = parser.date(showTime)['finish_date']

        item = EventItem()
        item['event_name'] = event_name.strip()
        item['event_area'] = event_city.strip()
        item['event_type'] = event_type.strip()
        item['event_src_url'] = response.url
        item['event_begin_date'] = event_begin_date
        item['event_finish_date'] = event_finish_date
        yield item

    def close(self, spider, reason):
        self.cacheManager.saveCache()
        return super(Eshow365Spider, spider).close(spider, reason)


if __name__ == '__main__':
    cmdline.execute('scrapy crawl eshow365'.split())
