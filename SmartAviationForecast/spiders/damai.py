# -*- coding: utf-8 -*-

import urlparse

import requests
import scrapy
from scrapy import cmdline

from SmartAviationForecast.items import EventItem
from utils import parser
from utils.cachemanager import CacheManager

"""
大麦网 - 爬虫
"""


class DamaiSpider(scrapy.Spider):
    name = 'damai'
    allowed_domains = ['damai.cn']
    start_urls = [
        'https://search.damai.cn/search.html?ctl=%E6%BC%94%E5%94%B1%E4%BC%9A&order=1',
                  'https://search.damai.cn/search.html?ctl=%E9%9F%B3%E4%B9%90%E4%BC%9A&order=1',
                  'https://search.damai.cn/search.html?ctl=%E4%BD%93%E8%82%B2%E6%AF%94%E8%B5%9B&order=1']

    cacheManager = CacheManager(name)

    def parse(self, response):
        page_count = int(response.xpath("//*[@id='search_list_page_tj']/div/ul/ul/li[last()-1]/text()").extract()[0])
        request_query_param = urlparse.parse_qs(urlparse.urlparse(response.request.url).query)
        ctl = request_query_param['ctl'][0]
        for i in range(1, page_count):
            r = requests.post(
                url='https://search.damai.cn/searchajax.html',
                data={'ctl': ctl,
                      'currPage': i},
            )
            event_list = r.json()['pageData']['resultData']

            for event in event_list:
                event_name = event['name']
                event_city = event['cityname']
                event_type = event['categoryname']
                event_src_url = 'https://piao.damai.cn/' + str(event['projectid']) + '.html'

                if self.cacheManager.isExist(event_src_url):
                    continue
                else:
                    self.cacheManager.add(event_src_url)

                try:
                    event_showtime = parser.date(event['showtime'])
                except ValueError:
                    continue

                event_content = event['description']
                event_begin_date = event_showtime['begin_date']
                event_finish_date = event_showtime['finish_date']

                item = EventItem()
                item['event_name'] = event_name
                item['event_area'] = event_city
                item['event_type'] = event_type
                item['event_src_url'] = event_src_url
                item['event_content'] = event_content
                item['event_begin_date'] = event_begin_date
                item['event_finish_date'] = event_finish_date
                yield item

    def close(self, spider, reason):
        self.cacheManager.saveCache()
        return super(DamaiSpider, spider).close(spider, reason)


if __name__ == '__main__':
    cmdline.execute('scrapy crawl damai'.split())
