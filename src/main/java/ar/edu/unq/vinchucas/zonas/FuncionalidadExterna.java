package ar.edu.unq.vinchucas.zonas;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.organizacion.Organizacion;

public interface FuncionalidadExterna {
    void nuevoEvento(Organizacion organizacion, ZonaDeCobertura zona, Muestra muestra);
    void muestraVerificada(Organizacion organizacion, ZonaDeCobertura zona, Muestra muestra);
} 