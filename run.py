#!/usr/bin/env python
# -*- coding: utf-8 -*-
from datetime import datetime

from apscheduler.events import *
from apscheduler.schedulers.blocking import BlockingScheduler
from scrapy.crawler import CrawlerProcess
from scrapy.utils.project import get_project_settings

from SmartAviationForecast.spiders.allconfs import AllconfsSpider
from SmartAviationForecast.spiders.cnweather import CnweatherSpider
from SmartAviationForecast.spiders.damai import DamaiSpider
from SmartAviationForecast.spiders.eshow365 import Eshow365Spider
from processer import processer
from utils import config
from utils.logger import Logger

"""
控制台
"""


class Console(object):
    def __init__(self):
        self.process = CrawlerProcess(get_project_settings())
        self.process.crawl(AllconfsSpider)
        self.process.crawl(DamaiSpider)
        self.process.crawl(CnweatherSpider)
        self.process.crawl(Eshow365Spider)

    def run(self):
        self.process.start()

    def stop(self):
        self.process.stop()


if __name__ == '__main__':
    console = Console()
    interval_hour = config.CONFIG.getint('scrapy', 'interval_hour')
    Logger.i('Smart Aviation Forecast V2.0 is Running...')
    scheduler = BlockingScheduler()
    # scheduler.add_job(console.run, 'interval', hours=3)
    scheduler.add_job(console.run, 'date', run_date=datetime.now())
    scheduler.add_listener(processer.run, EVENT_JOB_EXECUTED | EVENT_JOB_ERROR)
    scheduler.start()
