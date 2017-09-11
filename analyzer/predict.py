#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import sys

import jieba
import numpy as np
import tensorflow as tf
import tflearn

from analyzer import data_helper
from utils import config
from utils.database import MySQLHelper, EventView
from utils.logger import Logger

reload(sys)
sys.setdefaultencoding('utf8')

FLAGS = tf.app.flags.FLAGS
# Eval Parameters
tf.flags.DEFINE_integer("batch_size", 64, "Batch Size (default: 64)")
tf.flags.DEFINE_boolean("eval_train", True, "Evaluate on all training data")

# Misc Parameters
tf.flags.DEFINE_boolean("allow_soft_placement", True, "Allow device soft device placement")
tf.flags.DEFINE_boolean("log_device_placement", False, "Log placement of ops on devices")

_db = MySQLHelper()
_sess = _db.getSession()

analyzer_dir = config.projectAbsPath() + os.sep + 'analyzer' + os.sep
vocab_path = analyzer_dir + 'data' + os.sep + 'vocab'
model_dir = analyzer_dir + 'data' + os.sep + 'model'
ckpt_dir = [(ckpt, os.path.join(model_dir, ckpt)) for ckpt in os.listdir(model_dir)]


def predict(x, check_path):
    """
      input:
          x: 未分词的title,例如: x = "第六届中国软件杯全国大学生软件设计大赛"
          checkpoint_dir: checkpoint目录
      output:
          cate: 分类的类别, 数字化类别
    """

    vocab_processor = tflearn.data_utils.VocabularyProcessor.restore(vocab_path)
    x_cut_list = [' '.join(list(jieba.cut_for_search(x_))) for x_ in x]
    x_list = list(x_cut_list)
    vocab_transform = vocab_processor.transform(x_list)
    x = np.array(list(vocab_transform))
    print("\nEvaluating...\n")
    # Evaluation
    # ==================================================
    checkpoint_file = tf.train.latest_checkpoint(check_path)
    graph = tf.Graph()
    with graph.as_default():
        session_conf = tf.ConfigProto(
                allow_soft_placement=FLAGS.allow_soft_placement,
                log_device_placement=FLAGS.log_device_placement)
        sess = tf.Session(config=session_conf)
        with sess.as_default():
            # Load the saved meta graph and restore variables
            saver = tf.train.import_meta_graph("{}.meta".format(checkpoint_file))
            saver.restore(sess, checkpoint_file)
            # Get the placeholders from the graph by name
            input_x = graph.get_operation_by_name("input_x").outputs[0]
            # input_y = graph.get_operation_by_name("input_y").outputs[0]
            dropout_keep_prob = graph.get_operation_by_name("dropout_keep_prob").outputs[0]

            # Tensors we want to evaluate
            predictions = graph.get_operation_by_name("output/predictions").outputs[0]
            # Generate batches for one epoch
            batches = data_helper.batch_iter(list(x), FLAGS.batch_size, 1, shuffle=False)
            # print list(batches)
            all_predictions = []

            for x_test_batch in batches:
                batch_predictions = sess.run(predictions, {input_x: x_test_batch, dropout_keep_prob: 1.0})
                all_predictions = np.concatenate([all_predictions, batch_predictions])
            return [int(y) for y in all_predictions]


def reformat_city(city):
    if city[-1] == u'市' or city[-1] == u'省':
        return city[:len(city) - 1]
    else:
        return city


def run():
    Logger.i('加载用于分析的数据.')
    input_x, event_raw_table = data_helper.init_input_x()
    rs = {}
    Logger.i('开始分析.')
    # '''
    for ckpt_name, ckpt_path in ckpt_dir:
        rs[ckpt_name] = predict(input_x, ckpt_path)
    Logger.i('分析结束，入库保存中.')
    # '''
    event_view_list = []
    hash_list = [e.event_hash for e in event_raw_table]
    name_list = [e.event_name for e in event_raw_table]
    city_list = [reformat_city(e.event_area) for e in event_raw_table]
    begin_date_list = [e.event_begin_date for e in event_raw_table]
    finish_date_list = [e.event_finish_date for e in event_raw_table]
    type_mapping = {u'展会': 1, u'音乐会': 2, u'演唱会': 2, u'体育比赛': 3, u'异常天气': 4, u'政治会议': 5, u'其它会议': 6}
    e_type_list = [type_mapping[e.event_type] for e in event_raw_table]
    for e_hash, e_name, city, begin_date, finish_date, e_type, heat, history, influence_age, influence_type, max_influence, organizer_level, organizer_type, has_fixed_population, frequency_year \
            in zip(hash_list, name_list, city_list, begin_date_list, finish_date_list, e_type_list, rs['heat'],
                   rs['history'], rs['influence_age'], rs['influence_type'], \
                   rs['max_influence'], rs['organizer_level'], rs['organizer_type'], rs['has_fixed_population'], \
                   rs['frequency_year']):
        event_view = EventView(
                event_hash=e_hash,
                event_name=e_name,
                event_area=city,
                event_begin_date=begin_date,
                event_finish_date=finish_date,
                event_type=e_type,
                event_heat=heat,
                event_history=history,
                event_influence_age=influence_age,
                event_influence_type=influence_type,
                event_max_influence=max_influence,
                event_organizer_level=organizer_level,
                event_organizer_type=organizer_type,
                event_has_fixed_population=has_fixed_population,
                event_frequency_year=frequency_year
        )
        # print(
        #     "e_name:{}, heat:{}, history:{}, influence_age:{}, influence_type:{}, max_influence:{}, organizer_level:{}, organizer_type:{}, has_fixed_population:{}, frequency_year {}").format(
        #         e_name, heat, history, influence_age, influence_type, max_influence, organizer_level, organizer_type,
        #         has_fixed_population, frequency_year)
        event_view_list.append(event_view)
    data_helper.flush_event_view_table()
    data_helper.insert_event_view_table(event_view_list)
    Logger.i('保存完毕.')


if __name__ == '__main__':
    """
    test = ['2017“好好地II”朴树巡回演唱会·郑州站',
            '2017水蜜桃嘻哈电音节',
            '黄国伦 没有不可能演唱会',
            'Live 4 LIVE 尖叫现场 中岛美嘉2017中国巡演',
            '海淀区暴雨蓝色预警']
    rs = {}
    for ckpt_name, ckpt_path in ckpt_dir:
        rs[ckpt_name] = predict(test, ckpt_path)

    # print test, rs['heat'], rs['history'], rs['influence_age'], rs['influence_type'], rs['max_influence'], rs['organizer_level'], rs['organizer_type'], rs['has_fixed_population'], rs['frequency_year']
    for e_name, heat, history, influence_age, influence_type, max_influence, organizer_level, organizer_type, has_fixed_population, frequency_year \
            in zip(test, rs['heat'], rs['history'], rs['influence_age'], rs['influence_type'], rs['max_influence'],
                   rs['organizer_level'], rs['organizer_type'], rs['has_fixed_population'], rs['frequency_year']):
        print("e_name:{}, heat:{}, history:{}, influence_age:{}, influence_type:{}, max_influence:{}, organizer_level:{}, organizer_type:{}, has_fixed_population:{}, frequency_year {}").format(e_name, heat, history, influence_age, influence_type, max_influence, organizer_level, organizer_type, has_fixed_population, frequency_year)
    """
    run()
