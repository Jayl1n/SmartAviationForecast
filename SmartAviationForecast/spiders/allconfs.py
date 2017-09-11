# -*- coding: utf-8 -*-

import scrapy
from scrapy import cmdline, Request

from SmartAviationForecast.items import EventItem
from utils import parser
from utils.cachemanager import CacheManager


class AllconfsSpider(scrapy.Spider):
    name = 'allconfs'
    allowed_domains = ['allconfs.org']
    start_urls = [
        'http://www.allconfs.org/mobile/meeting_2016.asp',
        'http://www.allconfs.org/mobile/meeting_2017.asp',
        'http://www.allconfs.org/mobile/meeting_xs.asp',
        'http://www.allconfs.org/mobile/meeting_hy.asp',
        'http://www.allconfs.org/mobile/meeting_lt.asp'
    ]

    cacheManager = CacheManager(name)

    def parse(self, response):
        max_page = int(response.xpath('//div[@class="leftC_bc"]/form/strong/text()').re('\/(\d+)')[0])
        for i in range(1, max_page + 1):
            yield Request(response.url + '?page=%d' % i, callback=self.parse_next)

    def parse_next(self, response):
        items = response.xpath('//div[@class="leftCb_a"]')
        for item in items:
            event_name = item.xpath('./a/text()').extract()[0]
            event_begin_date = item.xpath('./span/text()').extract()[0].replace('\r\n', '').replace(' ', '')
            event_area = item.xpath('./span').re('\\xa0(.*)<a')[0].strip()
            event_src_url = item.xpath('./a/@href').extract()[0].replace('..', 'http://www.allconfs.org')

            if self.cacheManager.isExist(event_src_url):
                continue
            else:
                self.cacheManager.add(event_src_url)

            eventItem = EventItem()
            eventItem['event_name'] = event_name.strip()
            eventItem['event_area'] = event_area.strip()
            eventItem['event_type'] = u'其它会议'
            eventItem['event_src_url'] = event_src_url
            try:
                eventDate = parser.date(event_begin_date)
                eventItem['event_begin_date'] = eventDate['begin_date']
                eventItem['event_finish_date'] = eventDate['finish_date']
            except ValueError:
                continue

            yield eventItem

    def close(self, spider, reason):
        self.cacheManager.saveCache()
        return super(AllconfsSpider, spider).close(spider, reason)


if __name__ == '__main__':
    cmdline.execute('scrapy crawl allconfs'.split())
