# -*- coding: utf-8 -*-
import hashlib
from datetime import datetime

from utils.database import MySQLHelper, EventRaw


class MySQLPipeline(object):
    def __init__(self):
        self.client = MySQLHelper()

    def process_item(self, item, spider):
        hash_md5 = hashlib.md5()
        hash_md5.update(item['event_name'].encode('utf-8'))
        hash_md5.update(item['event_area'].encode('utf-8'))
        hash_md5.update(item['event_type'].encode('utf-8'))
        hash_md5.update(item['event_begin_date'].strftime('%Y%m%d'))
        eventRaw = EventRaw(
            event_hash=hash_md5.hexdigest(),
            event_create_date=datetime.now(),
            **item
        )
        self.client.addEvent(event_raw=eventRaw)
        return item
