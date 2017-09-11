#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os

import numpy as np
import tflearn.data_utils as data_utils

from utils import config
from utils.database import MySQLHelper, EventRaw, EventView

_sess = MySQLHelper().getSession()
vocab_path = config.projectAbsPath() + os.sep + 'analyzer' + os.sep + 'data' + os.sep + 'vocab'


def load_vocab_processor(path=vocab_path):
    return data_utils.VocabularyProcessor.restore(path)


def load_event_raw_table():
    return _sess.query(EventRaw).order_by(EventRaw.id)


def init_input_x():
    vocab_processor = load_vocab_processor()
    event_raw_table = _sess.query(EventRaw).order_by(EventRaw.id)
    # x_raw = [' '.join(jieba.cut_for_search(ev.event_name)) for ev in event_raw_table]
    # input_x = np.array(list(vocab_processor.fit_transform(x_raw)))
    input_x = [ev.event_name for ev in event_raw_table]
    return input_x, event_raw_table


def batch_iter(data, batch_size, num_epochs, shuffle=True):
    """
    Generates a batch iterator for a dataset.
    """
    data = np.array(data)
    data_size = len(data)
    num_batches_per_epoch = int(len(data) / batch_size) + 1
    for epoch in range(num_epochs):
        # Shuffle the data at each epoch
        if shuffle:
            shuffle_indices = np.random.permutation(np.arange(data_size))
            shuffled_data = data[shuffle_indices]
        else:
            shuffled_data = data
        for batch_num in range(num_batches_per_epoch):
            start_index = batch_num * batch_size
            end_index = min((batch_num + 1) * batch_size, data_size)
            yield shuffled_data[start_index:end_index]


def flush_event_view_table():
    _sess.query(EventView).delete()
    _sess.commit()


def insert_event_view_table(event_view_list):
    _sess.bulk_insert_mappings(
            EventView,
            [event.__dict__ for event in event_view_list]
    )
    _sess.commit()


if __name__ == '__main__':
    # print load_raw_data_list()
    # print load_vocab_processor().vocabulary_
    # print init_input_x()
    # flush_event_view_table()
    # eventRawTable = _sess.query(EventRaw).order_by(EventRaw.id)
    flush_event_view_table()
