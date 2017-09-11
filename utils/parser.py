#!/usr/bin/env python
# -*- coding: utf-8 -*-
import re

import dateutil.parser as date_parser


def date(str_date):
    date_regx = r'\d{4}[.\-_/]*\d{1,2}[.\-_/]*\d{1,2}'
    date_match = re.findall(date_regx, str_date)
    date_array = {}
    len_match = len(date_match)

    if len_match == 1:
        begin_date = date_parser.parse(date_match[0])
        finish_date = begin_date
        date_array['begin_date'] = begin_date
        date_array['finish_date'] = finish_date

    elif len_match == 2:
        begin_date = date_parser.parse(date_match[0])
        finish_date = date_parser.parse(date_match[1])
        date_array['begin_date'] = begin_date
        date_array['finish_date'] = finish_date
    else:
        begin_date = date_parser.parse(str_date)
        finish_date = begin_date
        date_array['begin_date'] = begin_date
        date_array['finish_date'] = finish_date
    return date_array
