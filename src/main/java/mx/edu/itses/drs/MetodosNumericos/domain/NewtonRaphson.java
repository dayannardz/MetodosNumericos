package mx.edu.itses.drs.MetodosNumericos.domain;

import lombok.Data;

@Data
public class NewtonRaphson {
    
    private String FX; // Función a evaluar
    private double Xi;
    private double FXi;
    private String DFXi;
    private double Xi1;
    private double Ea;
    private int IteracionesMaximas;
}
