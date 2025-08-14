package mx.edu.itses.drs.MetodosNumericos.services;

import mx.edu.itses.drs.MetodosNumericos.domain.Gauss;
import mx.edu.itses.drs.MetodosNumericos.domain.ReglaCramer;

public interface UnidadIIIService {
    
    public ReglaCramer AlgoritmoReglaCramer(ReglaCramer modelCramer);
    
    public Gauss AlgoritmoGauss(Gauss modelGauss);
}
