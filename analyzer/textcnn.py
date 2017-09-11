#!/usr/bin/env python
# -*- coding: utf-8 -*-
import os
import sys

import gensim
import numpy as np
import tensorflow as tf
import tflearn

reload(sys)

vocab_path = 'data' + os.sep + 'vocab'

class TextCNN(object):
    """
    用于文本分类的神经网络
    使用了５层网络
    embedding layer,
    convolutional layer,
    max-pooling layer,
    softmax layer.
    """

    def __init__(
            self, file_path, sequence_length, num_classes, vocab_size,
            embedding_size, word2vec_model_path, filter_sizes, num_filters, l2_reg_lambda=0.0):

        # Placeholders for input, output and dropout

        self.input_x = tf.placeholder(tf.int32, [None, sequence_length], name="input_x")
        self.input_y = tf.placeholder(tf.float32, [None, num_classes], name="input_y")
        self.dropout_keep_prob = tf.placeholder(tf.float32, name="dropout_keep_prob")

        # Keeping track of l2 regularization loss (optional)
        l2_loss = tf.constant(0.0)
        ############################################################################
        # Restore vocabulary
        vocab_processor = tflearn.data_utils.VocabularyProcessor.restore(vocab_path)
        model = gensim.models.Word2Vec.load(word2vec_model_path)
        IdVec = [[]] * len(vocab_processor.vocabulary_)
        # print(model[vocab_processor.vocabulary_.reverse(1)].dtype)
        IdVec[0] = np.zeros(256, dtype=np.float32)
        bound = np.sqrt(6.0) / np.sqrt(vocab_size)
        # bound for random variables.
        # print len(vocab_processor.vocabulary_)
        for id in range(1, len(vocab_processor.vocabulary_)):
            try:
                embedding = model[vocab_processor.vocabulary_.reverse(id)]  # try to get vector:it is an array.
            except Exception:
                embedding = None
            if embedding is not None:  # the 'word' exist a embedding
                IdVec[id] = embedding
            else:  # no embedding for this word
                IdVec[id] = np.random.uniform(-bound, bound, embedding_size)
        IdVec_final = np.array(IdVec)
        tmp = tf.constant(IdVec_final, dtype=tf.float32)

        ############################################################################

        # Embedding layer
        with tf.device('/cpu:0'), tf.name_scope("embedding"):
            W = tf.Variable(
                    # tf.random_uniform([vocab_size, embedding_size], -1.0, 1.0),
                    tmp,
                    name="W")
            self.embedded_chars = tf.nn.embedding_lookup(W, self.input_x)
            self.embedded_chars_expanded = tf.expand_dims(self.embedded_chars, -1)

        # Create a convolution + maxpool layer for each filter size
        pooled_outputs = []
        for i, filter_size in enumerate(filter_sizes):
            with tf.name_scope("conv-maxpool-%s" % filter_size):
                # Convolution Layer
                filter_shape = [filter_size, embedding_size, 1, num_filters]
                W = tf.Variable(tf.truncated_normal(filter_shape, stddev=0.1), name="W")
                b = tf.Variable(tf.constant(0.1, shape=[num_filters]), name="b")
                conv = tf.nn.conv2d(
                        self.embedded_chars_expanded,
                        W,
                        strides=[1, 1, 1, 1],
                        padding="VALID",
                        name="conv")
                # Apply nonlinearity
                h = tf.nn.relu(tf.nn.bias_add(conv, b), name="relu")
                # Maxpooling over the outputs
                pooled = tf.nn.max_pool(
                        h,
                        ksize=[1, sequence_length - filter_size + 1, 1, 1],
                        strides=[1, 1, 1, 1],
                        padding='VALID',
                        name="pool")
                pooled_outputs.append(pooled)

        # Combine all the pooled features
        num_filters_total = num_filters * len(filter_sizes)
        self.h_pool = tf.concat(pooled_outputs, 3)
        self.h_pool_flat = tf.reshape(self.h_pool, [-1, num_filters_total])

        # Add dropout
        with tf.name_scope("dropout"):
            self.h_drop = tf.nn.dropout(self.h_pool_flat, self.dropout_keep_prob)

        # Final (unnormalized) scores and predictions
        with tf.name_scope("output"):
            W = tf.get_variable(
                    "W",
                    shape=[num_filters_total, num_classes],
                    initializer=tf.contrib.layers.xavier_initializer())
            b = tf.Variable(tf.constant(0.1, shape=[num_classes]), name="b")
            l2_loss += tf.nn.l2_loss(W)
            l2_loss += tf.nn.l2_loss(b)
            self.scores = tf.nn.xw_plus_b(self.h_drop, W, b, name="scores")
            self.predictions = tf.argmax(self.scores, 1, name="predictions")

        # CalculateMean cross-entropy loss
        with tf.name_scope("loss"):
            losses = tf.nn.softmax_cross_entropy_with_logits(logits=self.scores, labels=self.input_y)
            self.loss = tf.reduce_mean(losses) + l2_reg_lambda * l2_loss

        # Accuracy
        with tf.name_scope("accuracy"):
            correct_predictions = tf.equal(self.predictions, tf.argmax(self.input_y, 1))
            self.accuracy = tf.reduce_mean(tf.cast(correct_predictions, "float"), name="accuracy")
