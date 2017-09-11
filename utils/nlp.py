#!/usr/bin/env python
# -*- coding: utf-8 -*-
import math

"""
余弦相似度计算
"""


def cossim(vector1, vector2):
    vector_list = list(set(vector1).union(set(vector2)))

    vector1_tf = []
    vector2_tf = []
    for i in range(len(vector_list)):
        vector1_tf.append(vector1.count(vector_list[i]))
        vector2_tf.append(vector2.count(vector_list[i]))

    numerator_list = []
    for i in range(len(vector_list)):
        numerator_list.append(vector1_tf[i] * vector2_tf[i])

    numerator = sum(numerator_list)
    denominator = math.sqrt(sum(map(lambda x: x * x, vector1_tf))) * math.sqrt(sum(map(lambda x: x * x, vector2_tf)))

    # 求值
    cosin = numerator / denominator
    return cosin


if __name__ == '__main__':
    v1 = ['你好', '哈', ]
    v2 = ['哈哈', '你好', ]
    print cossim(v1, v2)
