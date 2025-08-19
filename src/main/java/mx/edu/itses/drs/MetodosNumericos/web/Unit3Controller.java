package mx.edu.itses.drs.MetodosNumericos.web;

import mx.edu.itses.drs.MetodosNumericos.domain.ReglaCramer;
import mx.edu.itses.drs.MetodosNumericos.services.UnidadIIIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.drs.MetodosNumericos.domain.Gauss;
import mx.edu.itses.drs.MetodosNumericos.domain.GaussJordan;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class Unit3Controller {

    @Autowired
    private UnidadIIIService unidadIIIsrv;

    @GetMapping("/unit3")
    public String index(Model model) {
        return "unit3/index";
    }

    @GetMapping("/unit3/formcramer")
    public String formCramer(Model model) {
        ReglaCramer modelCramer = new ReglaCramer();
        model.addAttribute("modelCramer", modelCramer);
        return "unit3/cramer/formcramer";
    }

    @PostMapping("/unit3/solvecramer")
    public String solveCramer(ReglaCramer modelCramer, Errors errores, Model model) {
        log.info("OBJECTOS:" + modelCramer);
        var solveCramer = unidadIIIsrv.AlgoritmoReglaCramer(modelCramer);
        log.info("Solucion: " + solveCramer.getVectorX());
        model.addAttribute("solveCramer", solveCramer);
        return "unit3/cramer/solvecramer";
    }

    @GetMapping("/unit3/formgauss")
    public String formGauss(Model model) {
        Gauss modelGauss = new Gauss();

        model.addAttribute("modelGauss", modelGauss);
        return "unit3/gauss/formgauss";
    }

    @PostMapping("/unit3/solvegauss")
    public String solveGauss(Gauss modelGauss,
            Errors errores,
            Model model) {
        var solveGauss = unidadIIIsrv.AlgoritmoGauss(modelGauss);
        model.addAttribute("solveGauss", solveGauss);
        return "unit3/gauss/solvegauss";
    }

    @GetMapping("/unit3/formgaussjordan")
    public String formGaussJordan(Model model) {
        GaussJordan modelGJ = new GaussJordan();
        model.addAttribute("modelGJ", modelGJ);
        return "unit3/gaussjordan/formgaussjordan";
    }

    @PostMapping("/unit3/solvegaussjordan")
    public String solveGaussJordan(GaussJordan modelGJ,
            Errors errores,
            Model model) {
        var solveGJ = unidadIIIsrv.AlgoritmoGaussJordan(modelGJ);
        model.addAttribute("solveGJ", solveGJ);
        return "unit3/gaussjordan/solvegaussjordan";
    }

}
