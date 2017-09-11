#!/usr/bin/env python
# -*- coding: utf-8 -*-

from sqlalchemy import or_, func, tuple_

from analyzer import predict
from utils import nlp, config
from utils.database import MySQLHelper, EventRaw
from utils.logger import Logger

_db = MySQLHelper()
_sess = _db.getSession()
_similarity_threshold = config.CONFIG.getfloat('nlp', 'similarity_threshold')


def deleteInvalidEvent():
    outtime_list = _sess.query(EventRaw).filter(or_(
            EventRaw.event_name.like('%取消%'),
            EventRaw.event_name.like('%延期%'),
    ))
    outtime_list_count = outtime_list.count()
    Logger.i('查到空地点记录: %d 条' % outtime_list_count)
    outtime_list.delete(synchronize_session=False)
    _sess.commit()

    empty_list = _sess.query(EventRaw).filter(or_(
            None == EventRaw.event_area,
            '' == EventRaw.event_area,
            None == EventRaw.event_begin_date,
            None == EventRaw.event_finish_date
    ))
    empty_list_count = empty_list.count()
    Logger.i('查到空日期记录: %d 条' % empty_list_count)
    empty_list.delete(synchronize_session=False)
    _sess.commit()

    Logger.i('删除无效记录: %d 条' % (outtime_list_count + empty_list_count))


def deleteDuplicateEvent():
    # 查询重复HASH
    duplicate_hash = _sess.query(EventRaw).group_by(EventRaw.event_hash) \
        .having(func.count(EventRaw.event_hash) > 1)
    # 统计大小
    duplicate_hash_count = duplicate_hash.count()
    Logger.i('查到重复记录: %d 条' % duplicate_hash_count)
    duplicate_count = 0
    if duplicate_hash_count != 0:
        duplicate_count += duplicate_hash.count()

        for e in duplicate_hash.all():
            _sess.delete(e)

        _sess.commit()

    Logger.i('删除重复记录: %d 条' % duplicate_count)


def deleteSimilarEvent():
    sameAreaAndBeginDateList = _sess.query(EventRaw).filter(
            tuple_(EventRaw.event_area, EventRaw.event_begin_date).in_(
                    _sess.query(EventRaw.event_area, EventRaw.event_begin_date).group_by(EventRaw.event_area,
                                                                                         EventRaw.event_begin_date)
                        .having(func.count(EventRaw.event_area) > 1))).order_by(EventRaw.event_area,
                                                                                EventRaw.event_begin_date).all()

    Logger.i('查到同地区同时间记录: %d 条' % len(sameAreaAndBeginDateList))

    delete_list = []
    group_list = []
    group = []

    for i in range(len(sameAreaAndBeginDateList) - 1):
        eventCurrent = sameAreaAndBeginDateList[i]
        eventNext = sameAreaAndBeginDateList[i + 1]
        if eventCurrent.event_area == eventNext.event_area \
                and eventCurrent.event_begin_date == eventNext.event_begin_date:
            group.append(eventCurrent)
        else:
            group_list.append(group)
            group = []

    for g in group_list:
        for i in range(len(g) - 1):
            eventCurrent = g[i]
            eventNext = g[i + 1]
            if nlp.cossim(eventCurrent.event_name,
                          eventNext.event_name) >= _similarity_threshold:
                delete_list.append(eventCurrent)

    for e in delete_list:
        _sess.delete(e)

    _sess.commit()
    Logger.i('删除相似记录: %d 条' % len(delete_list))


def run(event=None):
    deleteDuplicateEvent()
    deleteInvalidEvent()
    deleteSimilarEvent()
    predict.run()


if __name__ == '__main__':
    # run()
    # reformatCity()
    a = u'南京'
    print a
    if a[-1] == u'市' or a[-1] == u'省':
        print a[:len(a)-1]
