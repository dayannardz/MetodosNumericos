
package mx.edu.itses.drs.MetodosNumericos.web;

import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.drs.MetodosNumericos.domain.DDNewton;
import mx.edu.itses.drs.MetodosNumericos.domain.Lagrange;
import mx.edu.itses.drs.MetodosNumericos.services.UnidadIVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class Unit4Controller {
    
    @Autowired
    private UnidadIVService unidadIVsrv;

    @GetMapping("/unit4")
    public String index(Model model) {
        return "unit4/index";
    }

    @GetMapping("/unit4/formddnewton")
    public String formDDNewton(Model model) {
        model.addAttribute("modelDDN", new DDNewton());
        return "unit4/ddnewton/formddnewton";
    }

    @PostMapping("/unit4/solveddnewton")
    public String solveDDNewton(DDNewton modelDDN, Errors errors, Model model) {
        var solveDDN = unidadIVsrv.AlgoritmoDDNewton(modelDDN);
        model.addAttribute("solveDDN", solveDDN);
        return "unit4/ddnewton/solveddnewton";
    }

    @GetMapping("/unit4/formlagrange")
    public String formLagrange(Model model) {
        model.addAttribute("modelLagrange", new Lagrange());
        return "unit4/lagrange/formlagrange";
    }

    @PostMapping("/unit4/solvelagrange")
    public String solveLagrange(Lagrange modelLagrange, Errors errors, Model model) {
        var solveLag = unidadIVsrv.AlgoritmoLagrange(modelLagrange);
        model.addAttribute("solveLag", solveLag);
        return "unit4/lagrange/solvelagrange";
    }
}
