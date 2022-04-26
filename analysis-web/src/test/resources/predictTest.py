#coding:utf-8

import numpy as np
from matplotlib import pyplot as plt
import statsmodels.tsa.stattools as ts
import pandas as pd
from statsmodels.stats.diagnostic import acorr_ljungbox #白噪声检验
from statsmodels.graphics.tsaplots import plot_acf,plot_pacf #画图定阶
from statsmodels.tsa.arima_model import ARIMA #ARIMA模型
import statsmodels.api as sm
from statsmodels.stats.stattools import durbin_watson #DW检验
import sys

def paramConversion(vdata):
    # 传入参数转换 由string转numpy数组
    # 如"[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]" 转成 [1.0  2.0  3.0  4.0  5.0  6.0  7.0]
    print("input data:" + (vdata))
    vdata = vdata.strip("[")
    vdata = vdata.strip("]")
    vdata_list_str = vdata.split(",")

    print("str-list" + str(list(vdata_list_str)))

    vdata_list_float = []
    for i in list(vdata_list_str):
        vdata_list_float.append(float(i))
    vdata_array = np.array(list(vdata_list_float))
    print("np:" + str(vdata_array))
    return vdata_array

def adfFunc(vdata_array):

    # 对数据进行adf检验,判断时序数据是否平稳
    adf = ts.adfuller(vdata_array)
    print(adf)
    if adf[0] <adf[4]["1%"] and adf[1]<10**(-5):
        # 对比Adf结果和10%的时的假设检验 以及 P-value是否非常接近0(越小越好)
        print("序列平稳")
        print("stable")
    else:
        print("非平稳序列")
        print("unstable")

def random_test(vdata_array) : #随机性检验（白噪声检验）
    p = acorr_ljungbox(vdata_array,lags=1)
    print(str(p))
    p_value = np.array(p)  # p_value 返回二维数组，第二维为P值
    print(str(p_value))
    if min(p_value[:,1]) > 0.05:
        print("随机性序列,即白噪声序列")
        # p值都很大，所以该数据无法拒绝原假设，即认为该数据是纯随机数据
        #     return False
        print("random")
    else:
        print("非随机性序列")
        print("nonrandom")
    #     return  True

def order_determination(vdata_array):
    order_analyze = ts.arma_order_select_ic(vdata_array, max_ar=5, max_ma=5, ic=['aic', 'bic'])
    aic_min_order = order_analyze.aic_min_order
    print(aic_min_order)
    bic_min_order = order_analyze.bic_min_order
    print(bic_min_order)

    return bic_min_order

def ARMA_model(train_data,order):
    #入参为训练数据以及由定阶方法定下的阶数
    arma_model = sm.tsa.ARIMA(train_data,order=(order[0],1,order[1]))  #ARMA模型
    arma = arma_model.fit()  #激活模型
    # sample_pred = arma.predict(start=7,end = 15,dynamic=True)
    sample_pred_five = arma.forecast(5)
    # print(sample_pred)
    print(sample_pred_five)
    return arma,sample_pred_five

if __name__ == '__main__':
    print("py success use")

    y = paramConversion(sys.argv[1])

    # x = "[25.86000061, 25.79999924, 25.81999969, 25.80999947, 25.79999924]"
    # x = "[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]"
    # y = paramConversion(x)
    adfFunc(y)
    random_test(y)
    order = order_determination(y)
    ARMA_model(y,order)
