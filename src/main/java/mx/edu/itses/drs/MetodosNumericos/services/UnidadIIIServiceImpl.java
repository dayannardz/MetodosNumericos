package mx.edu.itses.drs.MetodosNumericos.services;

import java.util.ArrayList;
import mx.edu.itses.drs.MetodosNumericos.domain.ReglaCramer;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.drs.MetodosNumericos.domain.Gauss;
import mx.edu.itses.drs.MetodosNumericos.domain.GaussJordan;
import mx.edu.itses.drs.MetodosNumericos.domain.GaussSeidel;
import mx.edu.itses.drs.MetodosNumericos.domain.Jacobi;

@Service
@Slf4j
public class UnidadIIIServiceImpl implements UnidadIIIService {

    @Override
    public ReglaCramer AlgoritmoReglaCramer(ReglaCramer modelCramer) {

        //almacenamos los determinantes de la matriz
        ArrayList<Double> determinantes = new ArrayList<>();
        ArrayList<Double> vectorX = new ArrayList<>();
        //tamaño del sistema lineal
        switch (modelCramer.getMN()) {
            case 2 -> {
                ArrayList<Double> A = modelCramer.getMatrizA();
                ArrayList<Double> b = modelCramer.getVectorB();

                double[][] MatrizA = {
                    {A.get(0), A.get(1)},
                    {A.get(2), A.get(3)}
                };
                log.info("Matriz A: " + MatrizA);
                determinantes.add(Det2(MatrizA));

                //calculamos determinante para X1
                double[][] MatrizX1 = {
                    {b.get(0), A.get(1)},
                    {b.get(1), A.get(3)}
                };
                determinantes.add(Det2(MatrizX1));
                log.info("Det X1: " + determinantes.get(1));

                //calculamos determinante para X2
                double[][] MatrizX2 = {
                    {A.get(0), b.get(0)},
                    {A.get(2), b.get(1)}
                };
                determinantes.add(Det2(MatrizX2));
                log.info("Det X2: " + determinantes.get(2));

                //resolviendo las variables
                //paraX1
                vectorX.add(determinantes.get(1) / determinantes.get(0));
                //paraX2
                vectorX.add(determinantes.get(2) / determinantes.get(0));

            }
            case 3 -> {
                ArrayList<Double> A = modelCramer.getMatrizA();
                ArrayList<Double> b = modelCramer.getVectorB();

                // matriz A 3x3
                double[][] MatrizA = {
                    {A.get(0), A.get(1), A.get(2)},
                    {A.get(3), A.get(4), A.get(5)},
                    {A.get(6), A.get(7), A.get(8)}
                };

                //determinante de A
                determinantes.add(Det3(MatrizA));
                log.info("Det A: " + determinantes.get(0));

                //Matriz para X1 
                double[][] MatrizX1 = {
                    {b.get(0), A.get(1), A.get(2)},
                    {b.get(1), A.get(4), A.get(5)},
                    {b.get(2), A.get(7), A.get(8)}
                };
                determinantes.add(Det3(MatrizX1));
                log.info("Matriz X1: " + determinantes.get(1));

                //Matriz para X2 
                double[][] MatrizX2 = {
                    {A.get(0), b.get(0), A.get(2)},
                    {A.get(3), b.get(1), A.get(5)},
                    {A.get(6), b.get(2), A.get(8)}
                };
                determinantes.add(Det3(MatrizX2));
                log.info("Matriz X2: " + determinantes.get(2));

                //Matriz para X3
                double[][] MatrizX3 = {
                    {A.get(0), A.get(1), b.get(0)},
                    {A.get(3), A.get(4), b.get(1)},
                    {A.get(6), A.get(7), b.get(2)}
                };

                determinantes.add(Det3(MatrizX3));
                log.info("Matriz X3: " + determinantes.get(3));

                //Calculamos el vector X
                vectorX.add(determinantes.get(1) / determinantes.get(0));
                vectorX.add(determinantes.get(2) / determinantes.get(0));
                vectorX.add(determinantes.get(3) / determinantes.get(0));

            }
        }
        modelCramer.setVectorX(vectorX);
        modelCramer.setDeterminantes(determinantes);
        return modelCramer;
    }

    private double Det2(double[][] A) {
        return A[0][0] * A[1][1] - A[0][1] * A[1][0];
    }

    private double Det3(double[][] m) {
        return m[0][0] * (m[1][1] * m[2][2] - m[1][2] * m[2][1])
                - m[0][1] * (m[1][0] * m[2][2] - m[1][2] * m[2][0])
                + m[0][2] * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);
    }

    @Override
    public Gauss AlgoritmoGauss(Gauss modelGauss) {
        if (modelGauss == null) {
            return null;
        }

        Integer n = modelGauss.getMN();
        var Aflat = modelGauss.getMatrizA();
        var bList = modelGauss.getVectorB();

        Gauss out = new Gauss();
        out.setMN(n);
        out.setSingular(false);

        //Validaciones
        if (n == null || Aflat == null || bList == null) {
            out.setSingular(true);
            return out;
        }
        if (n < 2 || n > 4) {
            out.setSingular(true);
            return out;
        }
        if (Aflat.size() != n * n || bList.size() != n) {
            out.setSingular(true);
            return out;
        }

        double[][] A = toMatrix(Aflat, n);
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = bList.get(i);
        }

        //eliminacion hacia adelante con pivoteo parcial
        for (int k = 0; k < n - 1; k++) {
            // Buscar pivote
            int piv = k;
            double maxAbs = Math.abs(A[k][k]);
            for (int r = k + 1; r < n; r++) {
                double val = Math.abs(A[r][k]);
                if (val > maxAbs) {
                    maxAbs = val;
                    piv = r;
                }
            }
            //sistema singular
            if (Math.abs(A[piv][k]) < 1e-12) {
                out.setSingular(true);
                out.setMatrizUEscalonada(flatten(A));
                return out;
            }
            //intercambio de filas si es necesario
            if (piv != k) {
                swapRows(A, k, piv);
                double tmp = b[k];
                b[k] = b[piv];
                b[piv] = tmp;
            }
            //eliminacion
            for (int i = k + 1; i < n; i++) {
                double factor = A[i][k] / A[k][k];
                A[i][k] = 0.0;
                for (int j = k + 1; j < n; j++) {
                    A[i][j] -= factor * A[k][j];
                }
                b[i] -= factor * b[k];
            }
        }

        if (Math.abs(A[n - 1][n - 1]) < 1e-12) {
            out.setSingular(true);
            out.setMatrizUEscalonada(flatten(A));
            return out;
        }

        //sustitucion hacia atras
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            x[i] = (b[i] - sum) / A[i][i];
        }

        //Salida
        out.setVectorX(toList(x));
        out.setMatrizUEscalonada(flatten(A));
        out.setMatrizA(new ArrayList<>(Aflat));
        out.setVectorB(new ArrayList<>(bList));

        return out;
    }

    private static double[][] toMatrix(ArrayList<Double> flat, int n) {
        double[][] M = new double[n][n];
        for (int i = 0, p = 0; i < n; i++) {
            for (int j = 0; j < n; j++, p++) {
                M[i][j] = flat.get(p);
            }
        }
        return M;
    }

    @Override
    public GaussJordan AlgoritmoGaussJordan(GaussJordan modelGJ) {
        if (modelGJ == null) {
            return null;
        }

        Integer n = modelGJ.getMN();
        var Aflat = modelGJ.getMatrizA();
        var bList = modelGJ.getVectorB();

        GaussJordan out = new GaussJordan();
        out.setMN(n);
        out.setSingular(false);

        if (n == null || Aflat == null || bList == null) {
            out.setSingular(true);
            return out;
        }
        if (n < 2 || n > 4) {
            out.setSingular(true);
            return out;
        }
        if (Aflat.size() != n * n || bList.size() != n) {
            out.setSingular(true);
            return out;
        }

        double[][] M = new double[n][n + 1];
        for (int i = 0, p = 0; i < n; i++) {
            for (int j = 0; j < n; j++, p++) {
                M[i][j] = Aflat.get(p);
            }
        }
        for (int i = 0; i < n; i++) {
            M[i][n] = bList.get(i);
        }

        for (int col = 0; col < n; col++) {

            int piv = col;
            double maxAbs = Math.abs(M[col][col]);
            for (int r = col + 1; r < n; r++) {
                double v = Math.abs(M[r][col]);
                if (v > maxAbs) {
                    maxAbs = v;
                    piv = r;
                }
            }
            if (Math.abs(M[piv][col]) < 1e-12) {
                out.setSingular(true);
                out.setMatrizRrefAumentada(flattenAug(M));
                return out;
            }
            if (piv != col) {
                swapRowsAug(M, piv, col);
            }

            double diag = M[col][col];
            for (int j = col; j <= n; j++) {
                M[col][j] /= diag;
            }

            for (int r = 0; r < n; r++) {
                if (r == col) {
                    continue;
                }
                double factor = M[r][col];
                if (Math.abs(factor) < 1e-15) {
                    continue;
                }
                for (int j = col; j <= n; j++) {
                    M[r][j] -= factor * M[col][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = M[i][n];
        }

        out.setVectorX(toList(x));
        out.setMatrizRrefAumentada(flattenAug(M));
        out.setMatrizA(new java.util.ArrayList<>(Aflat));
        out.setVectorB(new java.util.ArrayList<>(bList));
        return out;
    }

    private static ArrayList<Double> flattenAug(double[][] M) {
        int n = M.length, m = M[0].length;
        var out = new java.util.ArrayList<Double>(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                out.add(M[i][j]);
            }
        }
        return out;
    }

    private static void swapRowsAug(double[][] M, int r1, int r2) {
        double[] tmp = M[r1];
        M[r1] = M[r2];
        M[r2] = tmp;
    }

    private static ArrayList<Double> flatten(double[][] M) {
        int n = M.length;
        ArrayList<Double> out = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.add(M[i][j]);
            }
        }
        return out;
    }

    private static ArrayList<Double> toList(double[] v) {
        ArrayList<Double> out = new ArrayList<>(v.length);
        for (double x : v) {
            out.add(x);
        }
        return out;
    }

    private static void swapRows(double[][] A, int r1, int r2) {
        double[] tmp = A[r1];
        A[r1] = A[r2];
        A[r2] = tmp;
    }

    @Override
    public Jacobi AlgoritmoJacobi(Jacobi modelJacobi) {
        Integer n = modelJacobi.getMN();
        var Aflat = modelJacobi.getMatrizA();
        var bList = modelJacobi.getVectorB();

        Jacobi out = new Jacobi();
        out.setMN(n);
        out.setMatrizA(Aflat);
        out.setVectorB(bList);

        // validaciones
        if (n == null || Aflat == null || bList == null) {
            out.setConvergente(false);
            return out;
        }

        double[][] A = new double[n][n];
        double[] b = new double[n];
        for (int i = 0, p = 0; i < n; i++) {
            for (int j = 0; j < n; j++, p++) {
                A[i][j] = Aflat.get(p);
            }
            b[i] = bList.get(i);
        }

        // parámetros de iteración
        int maxIter = modelJacobi.getIteraciones() > 0 ? modelJacobi.getIteraciones() : 50;
        double tol = modelJacobi.getTolerancia() > 0 ? modelJacobi.getTolerancia() : 1e-6;

        double[] xOld = new double[n]; // inicial en 0
        double[] xNew = new double[n];

        boolean convergio = false;
        for (int k = 0; k < maxIter; k++) {
            for (int i = 0; i < n; i++) {
                double suma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        suma += A[i][j] * xOld[j];
                    }
                }
                xNew[i] = (b[i] - suma) / A[i][i];
            }

            // calcular error
            double error = 0.0;
            for (int i = 0; i < n; i++) {
                error = Math.max(error, Math.abs(xNew[i] - xOld[i]));
                xOld[i] = xNew[i];
            }

            if (error < tol) {
                convergio = true;
                out.setIteraciones(k + 1);
                break;
            }
        }

        out.setVectorX(toList(xNew));
        out.setTolerancia(tol);
        out.setConvergente(convergio);
        return out;
    }

    @Override
    public GaussSeidel AlgoritmoGaussSeidel(GaussSeidel modelGS) {
        var out = new GaussSeidel();
        if (modelGS == null) {
            return out;
        }

        Integer n = modelGS.getMN();
        var Aflat = modelGS.getMatrizA();
        var bList = modelGS.getVectorB();

        int maxIter = (modelGS.getIteraciones() != null && modelGS.getIteraciones() > 0) ? modelGS.getIteraciones() : 50;
        double tol = (modelGS.getTolerancia() != null && modelGS.getTolerancia() > 0.0) ? modelGS.getTolerancia() : 1e-6;

        out.setMN(n);
        out.setIteraciones(0);
        out.setTolerancia(tol);
        out.setConvergente(false);

        if (n == null || Aflat == null || bList == null) {
            return out;
        }
        if (n < 2 || n > 4) {
            return out;
        }
        if (Aflat.size() != n * n || bList.size() != n) {
            return out;
        }

        double[][] A = new double[n][n];
        double[] b = new double[n];
        for (int i = 0, p = 0; i < n; i++) {
            for (int j = 0; j < n; j++, p++) {
                A[i][j] = Aflat.get(p);
            }
            b[i] = bList.get(i);
            if (Math.abs(A[i][i]) < 1e-12) {
                return out;
            }
        }

        double[] x = new double[n];

        boolean ok = false;
        for (int k = 0; k < maxIter; k++) {
            double maxRelErr = 0.0;

            for (int i = 0; i < n; i++) {
                double suma = 0.0;
                for (int j = 0; j < n; j++) {
                    if (j == i) {
                        continue;
                    }
                    suma += A[i][j] * x[j];
                }
                double nuevo = (b[i] - suma) / A[i][i];

                double denom = Math.abs(nuevo) < 1e-12 ? 1.0 : Math.abs(nuevo);
                double rel = Math.abs(nuevo - x[i]) / denom;
                if (rel > maxRelErr) {
                    maxRelErr = rel;
                }

                x[i] = nuevo;
            }

            if (maxRelErr < tol) {
                ok = true;
                out.setIteraciones(k + 1);
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            x[i] = pretty(x[i]);
        }

        out.setVectorX(toList(x));
        out.setConvergente(ok);
        return out;
    }

    private static double pretty(double v) {
        if (Math.abs(v) < 1e-12) {
            return 0.0;
        }
        double r = Math.rint(v);
        if (Math.abs(v - r) < 1e-9) {
            return r;
        }
        return Math.round(v * 1e6) / 1e6;
    }

}
