package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.filtros.Filtro;
import java.util.List;

public interface IRepositorioDeMuestras {
    void agregarMuestra(Muestra muestra);
    List<Muestra> getMuestras();
    List<Muestra> buscarMuestras(Filtro filtro);
    List<Muestra> buscarMuestras(List<Filtro> filtros);
    List<Muestra> getMuestrasVerificadas();
    List<Muestra> getMuestrasNoVerificadas();
    int cantidadMuestras();
    int cantidadMuestrasVerificadas();
} 