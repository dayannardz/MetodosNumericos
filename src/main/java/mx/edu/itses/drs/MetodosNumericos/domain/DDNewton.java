
package mx.edu.itses.drs.MetodosNumericos.domain;

import java.util.ArrayList;
import lombok.Data;

@Data
public class DDNewton {
    
    private Integer orden;
    private ArrayList<Double> xs; 
    private ArrayList<Double> ys;
    private Double xEval;
    private ArrayList<Double> coeficientes;
    private ArrayList<Double> tablaAplanada; 
    private Double yEval;  

}
