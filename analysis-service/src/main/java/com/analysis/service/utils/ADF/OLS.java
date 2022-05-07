package com.analysis.service.utils.ADF;

import com.analysis.service.utils.ADF.Matrix;

/**
 * @description:
 * @author: lingwanxian
 * @date: 2022/4/22 17:59
 */
public class OLS {
    public Matrix betas;
    public Matrix x;
    public Matrix y;
    boolean intercept;

    public OLS()
    {
    }

    public Matrix Regress(Matrix x, Matrix y, boolean intercept)
    {
        this.intercept=intercept;
        betas = new Matrix();
        this.x = x;
        this.y = y;
        if(!intercept) {
            //Transpose  转置 Mult相乘  inverse 求逆
            betas = x.Transpose().Mult(x).Inverse().Mult(x.Transpose()).Mult(y);
        } else {
            int n = x.n;
            int m = x.m;
            Matrix design = new Matrix(n, m + 1);
            for(int i = 0; i < n; i++)
                design.setValue(i, 0, 1.0D);

            for(int i = 0; i < n; i++)
            {
                for(int j = 0; j < m; j++)
                    design.setValue(i, j + 1, x.getValue(i, j));

            }

            betas = design.Transpose().Mult(design).Inverse().Mult(design.Transpose()).Mult(y);
            this.x =new Matrix(design.myData);
        }
        return betas;
    }

    public Matrix getResiduals()
    {
        Matrix temp = y.Diff(x.Mult(betas));
        return temp;
    }

    public double getEstimatedVariance(){
        Matrix temp=new Matrix(x.n,1);
        temp.myData=getResiduals().myData;
        double temp1=0;
        for (int i=0;i<x.n;i++){
            temp1+= Math.pow(temp.getValue(i,0),2.0);
        }
        return (temp1/(x.n-x.m));
    }

    public Matrix getCovarianceMatrixOfParameters(){
        Matrix temp=(x.Transpose().Mult(x)).Inverse();
        double std=getEstimatedVariance();
        for (int i=0;i<x.m;i++){
            temp.myData[i][i]=temp.myData[i][i]*std;
        }
        return (temp);
    }

    public Matrix getStandartErrorsOfParameters() {
        Matrix temp=getCovarianceMatrixOfParameters();
        for (int i=0;i<x.m;i++){
            temp.myData[i][i]= Math.sqrt(temp.myData[i][i]);
        }
        return (temp);
    }

}
