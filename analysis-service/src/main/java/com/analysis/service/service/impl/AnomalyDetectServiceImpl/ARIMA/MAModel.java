package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA;

import java.util.Vector;

//移动平均过程(q)
public class MAModel
{
	private double[] data;
	private int q;

	public MAModel(double[] data, int q)
	{
		this.data = data;
		this.q = q;
	}

	public Vector<double[]> solveCoeOfMA()
	{
		Vector<double[]> vec = new Vector<>();
		double[] maCoe = new ARMAMethod().computeMACoe(this.data, this.q);

		vec.add(maCoe);

		return vec;
	}
}
