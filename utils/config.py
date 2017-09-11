#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
from ConfigParser import ConfigParser


def projectAbsPath():
    return os.path.dirname(os.path.dirname(__file__))


CONFIG = ConfigParser()
CONFIG_PATH = projectAbsPath() + os.sep + 'config.ini'
CONFIG.read(CONFIG_PATH)

if __name__ == '__main__':
    print CONFIG.get('db', 'db_host')
