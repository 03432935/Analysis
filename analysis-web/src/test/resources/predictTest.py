#coding:utf-8
# This is a sample Python script.

# Press Shift+F10 to execute it or replace it with your code.
# Press Double Shift to search everywhere for classes, files, tool windows, actions, and settings.
import numpy as np
from matplotlib import pyplot as plt
import statsmodels.tsa.stattools as ts
import pandas as pd
from statsmodels.stats.diagnostic import acorr_ljungbox #白噪声检验
from statsmodels.graphics.tsaplots import plot_acf,plot_pacf #画图定阶
from statsmodels.tsa.arima_model import ARIMA #ARIMA模型
# from statsmodels.tsa.arima_model import ARMA #ARMA模型 已经弃用
import statsmodels.api as sm
from statsmodels.stats.stattools import durbin_watson #DW检验
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

def paramConversion(vdata):
    # 传入参数转换 由string转numpy数组
    # 如"[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0]" 转成 [1.0  2.0  3.0  4.0  5.0  6.0  7.0]
    print("input data:" + (vdata))
    vdata = vdata.strip("[")
    vdata = vdata.strip("]")
    vdata_list_str = vdata.split(",")
#     print(str(vdata_list_str))
#     print("str-list" + str(vdata_list_str))

    vdata_array_float = np.array(vdata_list_str).astype(np.float)
    print("vdata_array_float")
    print(vdata_array_float)

    return vdata_array_float

def adf_func(vdata_array):

    # 对数据进行adf检验,判断时序数据是否平稳
    # 原数据进行ADF检验
    print("begin adf")
    adf = ts.adfuller(vdata_array)
    print(adf)
    if adf[0] <adf[4]["1%"] and adf[1]<10**(-5):
        # 对比Adf结果和10%的时的假设检验 以及 P-value是否非常接近0(越小越好)
        print("序列平稳")
        print("stable")
        return True
    else:
        print("非平稳序列")
        print("unstable")
        return False

    # vdata_array_diff = vdata_array.diff().dropna()
    # adf_diff = ts.adfuller(vdata_array_diff)
    # print(adf_diff)
    # if adf_diff[0] <adf_diff[4]["1%"] and adf_diff[1]<10**(-5):
    #     # 对比Adf结果和10%的时的假设检验 以及 P-value是否非常接近0(越小越好)
    #     print("差分后数据序列平稳")
    #     print("diff stable")
    # else:
    #     print("差分后数据非平稳序列")
    #     print("diff unstable")


def random_func(vdata_array) : #随机性检验（白噪声检验）
    p = acorr_ljungbox(vdata_array,lags=1)
    print(str(p))
    p_value = np.array(p)  # p_value 返回二维数组，第二维为P值
    print(str(p_value))
    if min(p_value[:,1]) > 0.05:
        print("随机性序列,即白噪声序列")
        # p值都很大，所以该数据无法拒绝原假设，即认为该数据是纯随机数据
        print("random")
        return False
    else:
        print("非随机性序列")
        print("nonrandom")
        return True

def stationarity_func(data):
    ## 差分法,保存成新的列
    diff1 = data.diff(1).dropna()  # 1阶差分 dropna() 删除缺失值
    diff2 = diff1.diff(1).dropna()  # 在一阶差分基础上再做一次一阶差分，即二阶差分
    ## 画图
    # diff1.plot(color = 'red',title='diff 1',figsize=(10,4))
    # diff2.plot(color = 'black',title='diff 2',figsize=(10,4))

    ## 平滑法
    rollmean = data.rolling(window=4, center=False).mean()  ## 滚动平均
    rollstd = data.rolling(window=4, center=False).std()  ## 滚动标准差
    ## 画图
    # rollmean.plot(color = 'yellow',title='Rolling Mean',figsize=(10,4))
    # rollstd.plot(color = 'blue',title='Rolling Std',figsize=(10,4))

    return diff1, diff2, rollmean, rollstd

def order_determination(vdata_array):
    order_analyze = ts.arma_order_select_ic(vdata_array, max_ar=5, max_ma=5, ic=['aic', 'bic'])
    aic_min_order = order_analyze.aic_min_order
    print(aic_min_order)
    bic_min_order = order_analyze.bic_min_order
    print(bic_min_order)
    # bic_matrix = []
    # for p in range(pmax+1):
    return bic_min_order

def ARMA_model(train_data,order):
    #入参为训练数据以及由定阶方法定下的阶数
    arma_model = sm.tsa.ARIMA(train_data,order=(order[0],1,order[1]))  #ARMA模型
    arma = arma_model.fit()  #激活模型
    # sample_pred = arma.predict(start=7,end = 15,dynamic=True)
    sample_pred_five = arma.forecast(5)
    # print(sample_pred)
    print(sample_pred_five)
    return sample_pred_five

if __name__ == '__main__':
    print("py success use")

    train_data = paramConversion(sys.argv[1])

    print("paramConversion is over")
    # x = "[25.86000061, 25.79999924, 25.81999969, 25.80999947, 25.79999924]"
    # x = "[1.0, 2.5, 3.9, 4.7, 5.5, 6.2, 7.9]"
#     x = "[-0.030000076293945, -0.00499950408935495, 0.0299993896484381, 0.0150000000000006, -0.009999618530273, -0.00499950408935495, -0.01999984741210905, 0.0150000000000006, 0.0200001144409185, -0.00499950408935505, -0.044999465942382494, -0.009999618530273, 0.005000724792481, 0.0100008392333989, -3.4332275345057284E-7, -0.030000076293945, -0.024999961853027, 0.005000724792481, -0.00999961853027295, -0.00499950408935495, 0.0150000000000006, 0.0100008392333989, 0.0100008392333989, -0.0299991226196285, -0.064999923706054, -0.07500015258789, -0.080000267028808, -0.085000381469726, -0.070000038146972, -0.080000267028808, -0.080000267028808, -0.080000267028808, -0.0900004959106435, -0.09999977111816295, -0.109999999999999, -0.10999999999999947, -0.08500038146972548, -0.08500038146972548, -0.0949996566772455, -0.040000305175781, -0.0599998092651365, -0.08499942779541, -0.030000076293945, -0.07499919891357396, -0.06499992370605448, -0.10999999999999899, -0.10999999999999899, -0.099999771118163, -0.0949996566772455, -0.0949996566772455, -0.0999997711181635, -0.035000190734863004, -0.06499992370605448, -0.115000114440917, -0.120000228881835, -0.109999999999999, -0.35666659037272036, -0.10499988555908099, -0.10499988555908099, -0.079999313354492, -0.040000305175781, -0.0599998092651365, -0.0949996566772455, -0.10499988555908099, -0.0949996566772455, -0.07500015258789, -0.0949996566772455, -0.0700000381469725, -0.0899995422363275, -0.155000076293944, -0.1649993515014635, -0.1649993515014635, -0.169999465942382, -0.159999237060545, -0.1449998474121075, -0.145000801086425, -0.149999961853026, -0.1649993515014635, -0.120000228881835, -0.1699994659423815, -0.189999923706054, -0.210000381469726, -0.20000015258789, -0.20000015258789, -0.20000015258789, -0.210000381469726, -0.17000041961669848, -0.149999961853026, -0.1749995803833, -0.19500003814697198, -0.20000015258789, -0.20000015258789, -0.2349999999999995, -0.220000610351562, -0.2299998855590815, -0.2299998855590815, -0.21500049591064402, -0.215000495910644, -0.220000610351562, -0.215000495910644, -0.20999942779540948, -0.20000015258789, -0.2299998855590815, -0.2149995422363275, -0.2149995422363275, -0.2400001144409175, -0.215000495910644, -0.20000015258789, -0.2349999999999995, -0.230000839233398, -0.230000839233398, -0.254999504089355, -0.254999504089355, -0.249999389648437, -0.264999732971191, -0.28500019073486305, -0.274999961853027, -0.259999618530273, -0.310000762939453, -0.28000007629394497, -0.324999198913574, -0.295000419616699, -0.28500019073486305, -0.339999542236327, -0.359999999999999, -0.35499988555908146, -0.3949998474121085, -0.370000228881835, -0.3449996566772455, -0.35499988555908046, -0.35499988555908146, -0.3399995422363275, -0.36500011444091696, -0.3449996566772455, -0.3750003433227525, -0.3700002288818345, -0.349999771118163, -0.374999389648437, -0.429999694824218, -0.4250005340576165, -0.43000064849853403, -0.420000419616698, -0.41500030517578, -0.40500007629394397, -0.3949998474121085, -0.375000343322752, -0.3650001144409165, -0.3999999618530265, -0.420000419616698, -0.46500049591064396, -0.45000015258789, -0.4399999237060535, -0.4249995803833, -0.40500007629394397, -0.395000801086424, -0.4100001907348615, -0.399999961853026, -0.4349998092651355, -0.41999946594238147, -0.43499980926513604, -0.420000419616698, -0.439999923706054, -0.4450000381469715, -0.4549993133544915, -0.439999923706054, -0.4399999237060535, -0.42499958038329955, -0.4100001907348615, -0.395000801086424, -0.395000801086424, -0.375000343322753, -0.399999961853026, -0.42499958038329955, -0.4100001907348625, -0.3949998474121085, -0.420000419616698, -0.41999946594238147, -0.41500030517578, -0.445000038146972, -0.42999969482421796, -0.4399999237060535, -0.3850005722045885, -0.3850005722045885, -0.390000686645506, -0.40500007629394397, -0.409999237060546, -0.4150003051757795, -0.429999694824218, -0.4500001525878895, -0.445000038146972, -0.41999946594238147, -0.4399999237060535, -0.42499958038329955, -0.414999351501464, -0.414999351501464, -0.38000045776367053, -0.3850005722045885, -0.400000915527342, -0.6266664123535137, -0.359999999999999, -0.3850005722045885, -0.395000801086424, -0.380000457763671, -0.3650001144409165, -0.375000343322753, -0.3650001144409165, -0.38000045776367053, -0.38000045776367053, -0.319999084472656, -0.319999084472656, -0.339999542236328, -0.329999313354492, -0.3250001525878905, -0.324999198913574, -0.349999771118163, -0.3149999237060545, -0.324999198913574, -0.3349994277954095, -0.33499942779541003, -0.310000762939453, -0.339999542236328, -0.295000419616699, -0.300000534057617, -0.3099998092651365, -0.3049996948242185, -0.3049996948242185, -0.290000305175781, -0.28000007629394497, -0.264999732971191, -0.249999389648437, -0.244999275207519, -0.26499973297119095, -0.290000305175781, -0.28000007629394497, -0.295000419616699, -0.274999961853027, -0.2500003433227535, -0.2349999999999995, -0.249999389648437, -0.244999275207519, -0.24500022888183548]"
#     train_data = paramConversion(x)
    df = train_data
    # 由于ARMA和ARIMA需要时间序列满足平稳性和非白噪声的要求，
    # 所以要用差分法和平滑法（滚动平均和滚动标准差）来实现序列的平稳性操作
    diff_flag = False
    if not adf_func(train_data):
        # 如果不满足该算法的前提要求，就进行差分法实现
        Smooth_data = stationarity_func(pd.DataFrame(train_data))
        print(Smooth_data)

        if adf_func(Smooth_data[0]) and random_func(Smooth_data[0]):
            print("use smooth[0],diff")
            diff_flag = True
            train_data = np.array(Smooth_data[0])

    order = order_determination(train_data)
    res = ARMA_model(train_data,order)
    if diff_flag:
        pred = []
        last = df[len(df)-1]
        print("last")
        print(str(last))
        for i in range(0,len(res)):
            last = last + res[i]
            pred.append(last)
        print("Result")
        print(str(list(pred)))
    else:
        print("Result")
        print(str(list(res)))
# See PyCharm help at https://www.jetbrains.com/help/pycharm/
