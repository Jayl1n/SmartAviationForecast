#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os
import pickle

from pybloom import BloomFilter

from utils import config

CACHE_BASE = config.projectAbsPath() + os.path.sep + 'cache' + os.path.sep


class CacheManager(object):
    def __init__(self, file_name):
        self.fileName = file_name + os.extsep + 'cache'
        self.cacheFilePath = CACHE_BASE + self.fileName
        if os.path.exists(self.cacheFilePath) and os.path.isfile(self.cacheFilePath):
            with open(self.cacheFilePath, 'r') as cacheFile:
                self.bloomFilter = pickle.load(cacheFile)
        else:
            self.bloomFilter = BloomFilter(capacity=10000, error_rate=0.0001)

    def add(self, url):
        self.bloomFilter.add(url)

    def isExist(self, url):
        return url in self.bloomFilter

    def clearCache(self):
        del self.bloomFilter
        os.remove(self.cacheFilePath)
        self.bloomFilter = BloomFilter(capacity=100000, error_rate=0.00001)

    def saveCache(self):
        if not (os.path.exists(CACHE_BASE) and os.path.isdir(CACHE_BASE)):
            os.mkdir(CACHE_BASE)
        with open(self.cacheFilePath, 'wr') as savePath:
            pickle.dump(self.bloomFilter, savePath, True)


if __name__ == '__main__':
    testB = CacheManager('test')
    testB.add('hello')
    print testB.isExist('hello')
    print testB.isExist('haaello')
    testB.saveCache()
    # testB.clearCache()
    # testB.saveCache()
