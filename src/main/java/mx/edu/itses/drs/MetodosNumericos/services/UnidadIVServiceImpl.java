
package mx.edu.itses.drs.MetodosNumericos.services;

import java.util.ArrayList;
import mx.edu.itses.drs.MetodosNumericos.domain.DDNewton;
import mx.edu.itses.drs.MetodosNumericos.domain.Lagrange;
import org.springframework.stereotype.Service;

@Service
public class UnidadIVServiceImpl implements UnidadIVService{

    @Override
    public DDNewton AlgoritmoDDNewton(DDNewton model) {
        DDNewton out = new DDNewton();
        if (model == null) return out;

        Integer orden = model.getOrden();
        var xs = model.getXs();
        var ys = model.getYs();
        Double xEval = model.getXEval();

        out.setOrden(orden);
        out.setXs(xs);
        out.setYs(ys);
        out.setXEval(xEval);

        // Validaciones básicas
        if (orden == null || orden < 1 || orden > 4) return out;
        int n = orden + 1;
        if (xs == null || ys == null || xs.size() != n || ys.size() != n) return out;

        // Construir tabla de diferencias divididas (n x n)
        double[][] T = new double[n][n];
        for (int i = 0; i < n; i++) T[i][0] = ys.get(i);
        for (int j = 1; j < n; j++) {
            for (int i = 0; i < n - j; i++) {
                double denom = xs.get(i + j) - xs.get(i);
                T[i][j] = (T[i + 1][j - 1] - T[i][j - 1]) / denom;
            }
        }

        // Coeficientes b0..bn (primera fila de cada columna)
        ArrayList<Double> coef = new ArrayList<>(n);
        for (int j = 0; j < n; j++) coef.add(T[0][j]);
        out.setCoeficientes(coef);

        // Evaluación P(xEval) con forma anidada de Newton
        Double yEval = null;
        if (xEval != null) {
            double acc = coef.get(n - 1);
            for (int k = n - 2; k >= 0; k--) {
                acc = coef.get(k) + (xEval - xs.get(k)) * acc;
            }
            yEval = acc;
        }
        out.setYEval(yEval);

        // Guardar tabla aplanada (opcional, útil si la muestras)
        ArrayList<Double> flat = new ArrayList<>(n * n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                flat.add(T[i][j]);
        out.setTablaAplanada(flat);

        return out;
    }

    @Override
    public Lagrange AlgoritmoLagrange(Lagrange model) {
        Lagrange out = new Lagrange();
        if (model == null) return out;

        Integer orden = model.getOrden();
        var xs = model.getXs();
        var ys = model.getYs();
        Double xEval = model.getXEval();

        out.setOrden(orden);
        out.setXs(xs);
        out.setYs(ys);
        out.setXEval(xEval);

        if (orden == null || orden < 1 || orden > 4) return out;
        int n = orden + 1;
        if (xs == null || ys == null || xs.size() != n || ys.size() != n) return out;
        if (xEval == null) return out;

        // Evaluación L(xEval)
        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            double Li = 1.0;
            for (int j = 0; j < n; j++) {
                if (j == i) continue;
                double denom = xs.get(i) - xs.get(j);
                Li *= (xEval - xs.get(j)) / denom;
            }
            sum += ys.get(i) * Li;
        }
        out.setYEval(pretty(sum));
        return out;
    }

    // ===== Helpers =====
    private static double pretty(double v) {
        if (Math.abs(v) < 1e-12) return 0.0;
        double r = Math.rint(v);
        if (Math.abs(v - r) < 1e-9) return r;
        return Math.round(v * 1e6) / 1e6;
    }
    
    
}
