package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;

import java.util.List;

public interface ISistemaDeZonas {
    void agregarZona(ZonaDeCobertura zona);
    void eliminarZona(ZonaDeCobertura zona);
    List<ZonaDeCobertura> getZonas();
    List<ZonaDeCobertura> zonasQueCubren(Muestra muestra);
    List<ZonaDeCobertura> zonasQueCubren(Ubicacion ubicacion);
    void procesarNuevaMuestra(Muestra muestra);
} 