package mx.edu.itses.drs.MetodosNumericos.services;

import java.util.ArrayList;
import mx.edu.itses.drs.MetodosNumericos.domain.Biseccion;
import mx.edu.itses.drs.MetodosNumericos.domain.ReglaFalsa;

public interface UnidadIIService {
    
    public ArrayList<Biseccion> AlgoritmoBiseccion(Biseccion biseccion);
    
    public ArrayList<ReglaFalsa> AlgoritmoReglaFalsa(ReglaFalsa reglafalsa);

}
