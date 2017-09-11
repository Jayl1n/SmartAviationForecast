# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class EventItem(scrapy.Item):
    event_hash = scrapy.Field()
    event_name = scrapy.Field()
    event_type = scrapy.Field()
    event_area = scrapy.Field()
    event_begin_date = scrapy.Field()
    event_finish_date = scrapy.Field()
    event_heat = scrapy.Field()
    event_history = scrapy.Field()
    event_content = scrapy.Field()
    event_src_url = scrapy.Field()
    event_frequency_year = scrapy.Field()
    event_organizer = scrapy.Field()
