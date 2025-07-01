package mx.edu.itses.drs.MetodosNumericos.services;

import java.util.ArrayList;
import lombok.extern.slf4j.Slf4j;
import mx.edu.itses.drs.MetodosNumericos.domain.Biseccion;
import mx.edu.itses.drs.MetodosNumericos.domain.NewtonRaphson;
import mx.edu.itses.drs.MetodosNumericos.domain.PuntoFijo;
import mx.edu.itses.drs.MetodosNumericos.domain.ReglaFalsa;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UnidadIIServiceImpl implements UnidadIIService {

    @Override
    public ArrayList<Biseccion> AlgoritmoBiseccion(Biseccion biseccion) {
        ArrayList<Biseccion> respuesta = new ArrayList<>();
        double XL, XU, XRa, XRn, FXL, FXU, FXR, Ea;

        XL = biseccion.getXL();
        XU = biseccion.getXU();
        XRa = 0;
        Ea = 100;
        // Verificamos que en el intervalo definido haya un cambio de signo
        FXL = Funciones.Ecuacion(biseccion.getFX(), XL);
        FXU = Funciones.Ecuacion(biseccion.getFX(), XU);
        if (FXL * FXU < 0) {
            for (int i = 1; i <= biseccion.getIteracionesMaximas(); i++) {
                XRn = (XL + XU) / 2;
                FXL = Funciones.Ecuacion(biseccion.getFX(), XL);
                FXU = Funciones.Ecuacion(biseccion.getFX(), XU);
                FXR = Funciones.Ecuacion(biseccion.getFX(), XRn);
                if (i != 1) {
                    Ea = Funciones.ErrorRelativo(XRn, XRa);
                }
                Biseccion renglon = new Biseccion();
                renglon.setXL(XL);
                renglon.setXU(XU);
                renglon.setXR(XRn);
                renglon.setFXL(FXL);
                renglon.setFXU(FXU);
                renglon.setFXR(FXR);
                renglon.setEa(Ea);
                if (FXL * FXR < 0) {
                    XU = XRn;
                } else if (FXL * FXR > 0) {
                    XL = XRn;
                } else if (FXL * FXR == 0) {
                    break;
                }
                XRa = XRn;
                respuesta.add(renglon);
                if (Ea <= biseccion.getEa()) {
                    break;
                }
            }
        } else {
            Biseccion renglon = new Biseccion();
            // renglon.setIntervaloInvalido(true);
            respuesta.add(renglon);
        }

        return respuesta;
    }

    @Override
    public ArrayList<ReglaFalsa> AlgoritmoReglaFalsa(ReglaFalsa reglafalsa) {
        ArrayList<ReglaFalsa> respuesta = new ArrayList<>();
        double XL, XU, XRa, XRn, FXL, FXU, FXR, Ea;

        XL = reglafalsa.getXL();
        XU = reglafalsa.getXU();
        XRa = 0;
        Ea = 100;

        FXL = Funciones.Ecuacion(reglafalsa.getFX(), XL);
        FXU = Funciones.Ecuacion(reglafalsa.getFX(), XU);

        if (FXL * FXU < 0) {
            for (int i = 1; i <= reglafalsa.getIteracionesMaximas(); i++) {
                XRn = XU - ((FXU * (XL - XU)) / (FXL - FXU));
                FXL = Funciones.Ecuacion(reglafalsa.getFX(), XL);
                FXU = Funciones.Ecuacion(reglafalsa.getFX(), XU);
                FXR = Funciones.Ecuacion(reglafalsa.getFX(), XRn);

                if (i != 1) {
                    Ea = Funciones.ErrorRelativo(XRn, XRa);
                }

                ReglaFalsa renglon = new ReglaFalsa();
                renglon.setXL(XL);
                renglon.setXU(XU);
                renglon.setXR(XRn);
                renglon.setFXL(FXL);
                renglon.setFXU(FXU);
                renglon.setFXR(FXR);
                renglon.setEa(Ea);

                respuesta.add(renglon);

                if (FXL * FXR < 0) {
                    XU = XRn;
                } else if (FXL * FXR > 0) {
                    XL = XRn;
                } else {
                    break; //sea raiz exacta
                }

                XRa = XRn;

                if (Ea <= reglafalsa.getEa()) {
                    break;
                }
            }
        } else {
            ReglaFalsa renglon = new ReglaFalsa();
            // renglon.setIntervaloInvalido(true);
            respuesta.add(renglon);
        }
        return respuesta;
    }

    @Override
    public ArrayList<PuntoFijo> AlgoritmoPuntoFijo(PuntoFijo puntofijo) {
        ArrayList<PuntoFijo> respuesta = new ArrayList<>();
        double Xi = puntofijo.getXi();
        double Xn;
        double Ea = 100;
        int maxIteraciones = puntofijo.getIteracionesMaximas();

        for (int i = 1; i <= maxIteraciones; i++) {
            Xn = Funciones.Ecuacion(puntofijo.getGX(), Xi);
            Ea = Funciones.ErrorRelativo(Xn, Xi);
            double gx = Funciones.Ecuacion(puntofijo.getGX(), Xi);

            PuntoFijo iteracion = new PuntoFijo();
            iteracion.setXi(Xi);
            iteracion.setGX(String.valueOf(gx));
            iteracion.setEa(Ea);
            iteracion.setIteracionesMaximas(i);

            respuesta.add(iteracion);

            if (Ea <= puntofijo.getEa()) {
                break;
            }
            Xi = Xn;
        }
        return respuesta;
    }

    @Override
    public ArrayList<NewtonRaphson> AlgoritmoNewtonRaphson(NewtonRaphson newtonraphson) {
        ArrayList<NewtonRaphson> respuesta = new ArrayList<>();

        double Xi = newtonraphson.getXi();//valor inicial
        double Xi1 = 0;//siguiente Xi
        double Ea = 100; //error inicial
        double h = 0.0001; //paso pequeño para aproximar derivada

        int maxIteraciones = newtonraphson.getIteracionesMaximas();

        for (int i = 1; i <= maxIteraciones; i++) {
            double FXi = Funciones.Ecuacion(newtonraphson.getFX(), Xi);
            double FdXi = (Funciones.Ecuacion(newtonraphson.getFX(), Xi + h) - FXi) / h;
            if (FdXi == 0) {
                System.out.println("Derivada cercana a cero, deteniendo iteración");
                break;
            }
            //Xi+1
            Xi1 = Xi - (FXi / FdXi);

            //Calcular el error relativo aproximado
            Ea = Funciones.ErrorRelativo(Xi1, Xi);

            //Guardar datos de la iteración
            NewtonRaphson iteracion = new NewtonRaphson();
            iteracion.setXi(Xi);
            iteracion.setFXi(FXi);
            iteracion.setDFXi(String.valueOf(FdXi));
            iteracion.setXi1(Xi1);
            iteracion.setEa(Ea);
            iteracion.setIteracionesMaximas(i);

            respuesta.add(iteracion);

            /*Verificar si el error ya está por debajo del deseado
            Preparar para siguiente iteración*/
            Xi = Xi1;
        }

        return respuesta;

    }

}
