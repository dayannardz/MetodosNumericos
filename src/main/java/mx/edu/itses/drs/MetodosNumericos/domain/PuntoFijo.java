package mx.edu.itses.drs.MetodosNumericos.domain;

import lombok.Data;

@Data
public class PuntoFijo {
    
    private String GX; // Función a evaluar
    private double Xi;
    private double Ea;
    private int IteracionesMaximas;
    
    
}
