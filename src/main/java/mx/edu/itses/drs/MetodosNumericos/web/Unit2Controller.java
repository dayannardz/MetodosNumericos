package mx.edu.itses.drs.MetodosNumericos.web;

import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.drs.MetodosNumericos.domain.Biseccion;
import mx.edu.itses.drs.MetodosNumericos.domain.ReglaFalsa;
import mx.edu.itses.drs.MetodosNumericos.services.Funciones;
import mx.edu.itses.drs.MetodosNumericos.services.UnidadIIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class Unit2Controller {
    @Autowired
    private UnidadIIService bisectionservice;
    @Autowired
    private UnidadIIService reglafalsaservice;

    @GetMapping("unit2/formbisection")
    public String formBisection(Model model) {

        Biseccion bisection = new Biseccion();

        model.addAttribute("bisection", bisection);
        return "unit2/bisection/formbisection";
    }

    @PostMapping("unit2/solvebisection")
    public String solvebisection(Biseccion bisection, Model model) {

        // double valorFX = Funciones.Ecuacion(bisection.getFX(), bisection.getXL());
        // log.info("Valor de FX: " + valorFX);
        var solveBisection = bisectionservice.AlgoritmoBiseccion(bisection);

        //log.info("Arreglo: " + solveBisection);
        
        model.addAttribute("solveBisection", solveBisection);
        return "/unit2/bisection/solvebisection";
    }
    
    
    
    @GetMapping("unit2/formreglafalsa")
    public String formReglaFalsa(Model model) {

        ReglaFalsa reglafalsa = new ReglaFalsa();

        model.addAttribute("reglafalsa", reglafalsa);
        return "unit2/reglafalsa/formreglafalsa";
    }
    
    @PostMapping("unit2/solvereglafalsa")
    public String solvereglafalsa(ReglaFalsa reglafalsa, Model model) {

        // double valorFX = Funciones.Ecuacion(bisection.getFX(), bisection.getXL());
        // log.info("Valor de FX: " + valorFX);
        var solveReglaFalsa = reglafalsaservice.AlgoritmoReglaFalsa(reglafalsa);

        log.info("Arreglo: " + solveReglaFalsa);
        
        model.addAttribute("solveReglaFalsa", solveReglaFalsa);
        return "unit2/reglafalsa/solvereglafalsa";
    }
}
