
package mx.edu.itses.drs.MetodosNumericos.domain;

import java.util.ArrayList;
import lombok.Data;

@Data
public class Lagrange {
    
    private Integer orden;
    private ArrayList<Double> xs; 
    private ArrayList<Double> ys;
    private Double xEval;
    private Double yEval;
}
