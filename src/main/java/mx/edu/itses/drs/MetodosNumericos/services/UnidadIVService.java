
package mx.edu.itses.drs.MetodosNumericos.services;

import mx.edu.itses.drs.MetodosNumericos.domain.DDNewton;
import mx.edu.itses.drs.MetodosNumericos.domain.Lagrange;

public interface UnidadIVService {
    
    DDNewton AlgoritmoDDNewton(DDNewton model);
    Lagrange AlgoritmoLagrange(Lagrange model);
    
}
