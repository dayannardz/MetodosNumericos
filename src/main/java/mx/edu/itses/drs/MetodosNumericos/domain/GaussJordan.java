
package mx.edu.itses.drs.MetodosNumericos.domain;

import java.util.ArrayList;
import lombok.Data;

@Data
public class GaussJordan {
    
    private Integer MN;                       
    private ArrayList<Double> matrizA;        
    private ArrayList<Double> vectorB;        
    private ArrayList<Double> vectorX;        
    private boolean singular;                 
    private ArrayList<Double> matrizRrefAumentada;
}
