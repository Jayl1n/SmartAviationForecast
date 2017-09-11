#!/usr/bin/env python
# -*- coding: utf-8 -*-
import logging


class Logger:
    logging.basicConfig(level=logging.DEBUG,
                        format=' [%(levelname)s] %(asctime)s %(filename)s[line:%(lineno)d] %(message)s : ',
                        datefmt='%Y/%m/%d %H:%M:%S',
                        # filename='myapp.log',
                        # filemode='w'
                        )
    console = logging.StreamHandler()
    console.setLevel(logging.INFO)
    formatter = logging.Formatter('%(name)-12s: %(levelname)-8s %(message)s')
    console.setFormatter(formatter)
    logging.getLogger('').addHandler(console)

    @staticmethod
    def i(message):
        logging.info(message)

    @staticmethod
    def d(message):
        logging.debug(message)

    @staticmethod
    def w(message):
        logging.warning(message)

    @staticmethod
    def e(message):
        logging.error(message)
