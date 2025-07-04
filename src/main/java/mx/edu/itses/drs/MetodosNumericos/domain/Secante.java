package mx.edu.itses.drs.MetodosNumericos.domain;

import lombok.Data;

@Data
public class Secante {
    
    private String FX;
    private double Xi_1;    
    private double Xi;   
    private double FXi_1;  
    private double FXi;   
    private double Xi1;  
    private double Ea;   
    private int iteracionesMaximas;  
}
