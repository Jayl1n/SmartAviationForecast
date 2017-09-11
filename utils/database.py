#!/usr/bin/env python
# -*- coding: utf-8 -*-

from sqlalchemy import Column, Date, DateTime, Integer, String, Table, text, create_engine, exc, orm, Float
from sqlalchemy.exc import InvalidRequestError
from sqlalchemy.ext.declarative import declarative_base

from utils.config import CONFIG

Base = declarative_base()
metadata = Base.metadata

class EventRaw(Base):
    __tablename__ = 'EventRaw'

    id = Column(Integer, primary_key=True)
    event_hash = Column(String(255), index=True)
    event_name = Column(String(512), nullable=False)
    event_area = Column(String(255))
    event_type = Column(String(32))
    event_begin_date = Column(Date)
    event_finish_date = Column(Date)
    event_heat = Column(String(32))
    event_history = Column(String(32))
    event_content = Column(String(10240))
    event_src_url = Column(String(1024))
    event_frequency_year = Column(String(32))
    event_organizer = Column(String(2048))
    event_create_date = Column(DateTime)

class EventView(Base):
    __tablename__ = 'EventView'

    event_hash = Column(String(255), primary_key=True)
    event_name = Column(String(512))
    event_area = Column(String(255))
    event_type = Column(Integer, server_default=text("'0'"))
    event_heat = Column(Integer, server_default=text("'0'"))
    event_history = Column(Integer, server_default=text("'0'"))
    event_begin_date = Column(Date)
    event_finish_date = Column(Date)
    event_type_raw = Column(String(32))
    event_heat_raw = Column(String(32))
    event_history_raw = Column(String(32))
    event_frequency_year = Column(Integer, server_default=text("'0'"))
    event_frequency_year_raw = Column(String(32), server_default=text("'1'"))
    event_organizer_type = Column(Integer, server_default=text("'0'"))
    event_organizer_level = Column(Integer, server_default=text("'0'"))
    event_influence_age = Column(Integer, server_default=text("'0'"))
    event_max_influence = Column(Integer, server_default=text("'0'"))
    event_has_fixed_population = Column(Integer, server_default=text("'0'"))
    event_influence_type = Column(Integer, server_default=text("'0'"))
    event_organizer = Column(String(2048))



class MySQLHelper(object):
    def __init__(self):
        self.host = CONFIG.get('db', 'db_host')
        self.port = CONFIG.getint('db', 'db_port')
        self.username = CONFIG.get('db', 'db_username')
        self.password = CONFIG.get('db', 'db_password')
        self.database = CONFIG.get('db', 'db_database')
        dsn = 'mysql+mysqldb://%s:%s@%s:%d/%s?charset=utf8' \
              % (self.username, self.password, self.host, self.port, self.database)

        try:
            engine = create_engine(dsn)
        except exc.OperationalError:
            pass
        finally:
            # engine.execute('CREATE DATABASE %s' % self.database)  # 创建数据库
            metadata.create_all(engine)  # 创建表
            engine.connect()

        Session = orm.sessionmaker(bind=engine, autoflush=True)
        self.ses = Session()

    def addEvent(self, event_raw):
        session = self.ses
        session.add(event_raw)
        try:
            session.commit()
        except InvalidRequestError:
            pass

    def addEventBatch(self, event_raw_list):
        session = self.ses
        for event_raw in event_raw_list:
            session.add(event_raw)
        session.commit()

    def getSession(self):
        return self.ses



    def finish(self):
        return self.ses.connection().close()


if __name__ == '__main__':
    # client = MySQLHelper()
    # event_raw = EventRaw(event_name='Hellotest2')
    # client.addEvent(event_raw)
    pass
