package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.List;

public interface Filtro {
    List<Muestra> filtrar(List<Muestra> muestras);
} 