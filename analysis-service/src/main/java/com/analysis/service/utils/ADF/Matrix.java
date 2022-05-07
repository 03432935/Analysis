package com.analysis.service.utils.ADF;

/**
 * @description:矩阵
 * @author: lingwanxian
 * @date: 2022/4/22 17:57
 */
public class Matrix {

    public double myData[][];
    public int n;
    public int m;

    public Matrix() {
    }

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        try {
            myData = new double[n][m];
        } catch (Exception _ex) {
            System.out.println("Matrix(n,m) can't create");
        }
    }

    public Matrix(double data[][]) {
        n = data.length;
        m = data[0].length;
        myData = data;
    }

    public Matrix(double data[]) {
        n = data.length;
        m = 1;
        double[][] a = new double[n][1];
        for (int i = 0; i < n; i++) {
            a[i][0] = data[i];
        }
        myData = a;
    }

    public double Det() {
        if (n != m) {
            return 0.0D;
        }
        double mydet = 1.0D;
        Matrix temp1 = new Matrix(myData);
        double temp2[][] = temp1.Eshelon().getData();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == j) {
                    mydet *= temp2[i][j];
                }
            }

        }

        temp2 = null;
        temp1 = null;
        return mydet;
    }

    public Matrix Diff(Matrix A) {
        Matrix mynew = new Matrix(A.n, A.m);
        for (int i = 0; i < A.n; i++) {
            for (int j = 0; j < A.m; j++) {
                mynew.setValue(i, j, myData[i][j] - A.getValue(i, j));
            }

        }

        return mynew;
    }

    public Matrix Eshelon() {
        double pivot = 0.0D;
        Matrix t = new Matrix(myData);
        double temp[][] = new double[t.n][t.m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                temp[i][j] = t.getValue(i, j);
            }

        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pivot = -temp[j][i] / temp[i][i];
                for (int k = 0; k < m; k++) {
                    temp[j][k] = temp[i][k] * pivot + temp[j][k];
                }

            }

        }

        t = null;
        return new Matrix(temp);
    }

    public Matrix Inverse() {
        if (this.n != this.m || Det() == 0.0D) {
            return new Matrix(this.n, this.m);
        }
        Matrix temp = new Matrix(myData);
        int n = temp.n;
        int m = temp.m;
        Matrix Cofactor = new Matrix(n, m);
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                Matrix Mintemp = temp.Minor(i, j);
                double tempdet = Mintemp.Det();
                Cofactor.setValue(i, j, tempdet);
            }

        }

        Cofactor = Cofactor.Transpose();
        double det = temp.Det();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Cofactor.setValue(i, j, (Cofactor.getValue(i, j) * Math.pow(-1D, i + j + 2)) / det);
            }

        }

        return Cofactor;
    }

    public Matrix Minor(int x, int y) {
        Matrix temp = new Matrix(myData);
        Matrix newmat = new Matrix(temp.n - 1, temp.m - 1);
        int n = temp.n;
        int m = temp.m;
        int cnt = 0;
        double myArr[] = new double[(n - 1) * (m - 1)];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i != x && j != y) {
                    myArr[cnt] = temp.getValue(i, j);
                    cnt++;
                }
            }

        }

        cnt = 0;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < m - 1; j++) {
                newmat.setValue(i, j, myArr[cnt]);
                cnt++;
            }

        }

        return newmat;
    }

    public Matrix Mult(Matrix A) {
        double c[][] = new double[n][A.m];
        double tempdata[][] = A.getData();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < A.m; j++) {
                for (int y = 0; y < m; y++) {
                    c[i][j] = c[i][j] + myData[i][y] * tempdata[y][j];
                }

            }

        }

        return new Matrix(c);
    }

    public Matrix Transpose() {
        Matrix temp = new Matrix(myData);
        int n = temp.n;
        int m = temp.m;
        Matrix newmat = new Matrix(m, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newmat.setValue(j, i, temp.getValue(i, j));
            }

        }

        return newmat;
    }


    public double[][] getData() {
        double temp[][] = myData;
        return temp;
    }

    public double getValue(int x, int y) {
        return myData[x][y];
    }


    public void setValue(int x, int y, double value) {
        myData[x][y] = value;
    }

}

