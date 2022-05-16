package com.analysis.service.service.impl.AnomalyDetectServiceImpl.ARIMA;

import java.util.Vector;

// 自回归过程(p)
public class ARModel
{
	private double[] data;
	private int p;

	public ARModel(double[] data, int p)
	{
		this.data = data;
		this.p = p;
	}

	public Vector<double[]> solveCoeOfAR()
	{
		Vector<double[]> vec = new Vector<>();
		double[] arCoe = new ARMAMethod().computeARCoe(this.data, this.p);

		vec.add(arCoe);

		return vec;
	}
}
