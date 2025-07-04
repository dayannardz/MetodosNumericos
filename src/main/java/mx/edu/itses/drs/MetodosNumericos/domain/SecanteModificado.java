package mx.edu.itses.drs.MetodosNumericos.domain;

import lombok.Data;

@Data
public class SecanteModificado {
    
    private double Xi;
    private double Xi1;
    private double FXi;
    private double FXiSigma;
    private double Ea;
    private String FX;
    private int iteracionesMaximas;
    private double sigma;
    
}
