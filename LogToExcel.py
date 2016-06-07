#!/usr/bin/env python3
# -*- coding:utf-8 -*-

from datetime import datetime
from openpyxl import Workbook
import sys


ver = sys.version

# 将二维数组 data[][] 中的数据写入名为 filename 的Excel文件中
def log_excel(data, filename):
    # 行数
    rows = len(data)
    # 列数
    columns = len(data[0])

    # 在内存中创建Excel
    wb = Workbook()

    # 使用默认的worksheet，默认为sheet 0
    if ver.find('3.') >= 0:         #python 3x
        ws = wb.active
        startindex = 1
    else:
        ws = wb.get_active_sheet()  #jython 2x
        startindex = 0

    # 遍历cell并将data写入cell，注意使用.value访问cell的内容
    for i in range(0, rows):
        for j in range(0, columns):
            ws.cell(row=i+startindex, column=j+startindex).value = data[i][j]

    # 保存Excel
    wb.save(filename)

    return 'success'

# 测试函数
def test_log_excel():
    # 目录
    path = 'LogFile'
    # 文件名
    filename = datetime.now().strftime("%Y-%m-%d")
    # 后缀
    suffix = 'xlsx'
    # 全名
    fullname = '%s/%s.%s' % (path, filename, suffix)

    # 数据
    data =[['A1','A2', datetime.now()],['B1','B2','中文'],['C1','C2', 3.1415926]]

    # 写Excel
    log_excel(data, fullname)

# if ver.find('3.') >= 0:
#    test_log_excel()

# 外部调用
# 使用eval函数，将字符串转换成对应的数据类型
if ver.find('3.') >= 0:
    log_excel(eval(sys.argv[1]), sys.argv[2])