#coding:utf-8
# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import numpy as np
from matplotlib import pyplot as plt
# from arch.unitroot import ADF
import statsmodels.tsa.stattools as ts
from statsmodels.tsa.arima_model import ARMA
import pandas as pd

import sys

# 关于ts.adfuller的参数说明
# 输入参数：
# x：array_like，1d，要测试的数据系列。
# maxlag： 测试中包含的最大延迟，默认为12 *（nobs / 100）^ {1/4}。
# regression：{‘c’，‘ct’，‘ctt’，‘nc’}， 包含在回归中的常量和趋势顺序。‘c’：仅限常量（默认值）。 ‘ct’：恒定和趋势。 ‘ctt’：常数，线性和二次趋势。 ‘nc’：没有恒定，没有趋势。
# autolag： {‘AIC’，‘BIC’，‘t-stat’，None}自动确定滞后时使用的方法。如果为None，则使用maxlag滞后。如果是’AIC’（默认值）或’BIC’，则选择滞后数以最小化相应的信息标准。基于’t-stat’的maxlag选择。从maxlag开始并使用5％大小的测试来降低延迟，直到最后一个滞后长度的t统计量显着为止。
# store：bool，如果为True，则另外返回adf统计信息的结果实例。默认值为False。
# regresults：bool，optional，如果为True，则返回完整的回归结果。默认值为False。
#
# 返回参数：
# ADF：float，测试统计。
# pvalue：float，probability value：MacKinnon基于MacKinnon的近似p值（1994年，2010年）。
# usedlag：int，使用的滞后数量。
# NOBS：int，用于ADF回归和计算临界值的观察数。
# critical values：dict，测试统计数据的临界值为1％，5％和10％。基于MacKinnon（2010）。
# icbest：float，如果autolag不是None，则最大化信息标准。
# resstore：ResultStore, optional，一个虚拟类，其结果作为属性附加。

def adfFunc(vdata):

    # 传入参数转换 由string转numpy数组
    # 如"[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]" 转成 [1.0  2.0  3.0  4.0  5.0  6.0  7.0]
    print("input data:"+(vdata))
    vdata = vdata.strip("[")
    vdata = vdata.strip("]")
    vdata_list_str = vdata.split(",")

    print("str-list"+str(list(vdata_list_str)))

    vdata_list_float = []
    for i in list(vdata_list_str):
        vdata_list_float.append(float(i))
    vdata_array = np.array(list(vdata_list_float))
    print("np:"+str(vdata_array))

    # 对数据进行adf检验,判断时序数据是否平稳
    adf = ts.adfuller(vdata_array)
    print(adf)

    # adf = ADF(vdata)
    # adf.trend = 'ct'
    # print(adf.summary().as_text())

if __name__ == '__main__':
    print("py success use")

    adfFunc(sys.argv[1])

    # x = "[25.86000061, 25.79999924, 25.81999969, 25.80999947, 25.79999924]"
    # x = "[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]"
    # adfFunc(x)


# if __name__ == '__main__':
#     a = np.arange(12).reshape(3, 4)
#     print(a)
#


# def print_hi(name):
#     # file_location = "F:\desktop\Assignment\Concrete_Data.xls"
#     # data = xlrd.open_workbook(file_location)
#     # data = pd.read_excel(r'C:\Users\28196\Desktop\1110000201.xlsx')
#     data = xlrd.open_workbook(r'C:\Users\28196\Desktop\1110000201.xlsx')
#     print(data)
#     #	data是Excel里的数据
#     sheet = data.sheet_by_index(0)
#     #	根据索引读取
#     #	就是说data里有很多sheet
#     #	data.sheet_by_index（0）是sheet1，以此类推
#     #取第三列，v数值
#     vData = [sheet.cell_value(r, 3) for r in range(1, sheet.nrows)]
#     plt.plot(vData)
#     plt.show()
#
#     # Use a breakpoint in the code line below to debug your script.
#     print(f'Hi, {name}')  # Press Ctrl+F8 to toggle the breakpoint.
#
#
# # Press the green button in the gutter to run the script.
# if __name__ == '__main__':
#     print_hi('PyCharm')

# See PyCharm help at https://www.jetbrains.com/help/pycharm/
