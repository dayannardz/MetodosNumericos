package mx.edu.itses.drs.MetodosNumericos.services;

import java.util.ArrayList;
import mx.edu.itses.drs.MetodosNumericos.domain.ReglaCramer;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.drs.MetodosNumericos.domain.Gauss;

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

}
