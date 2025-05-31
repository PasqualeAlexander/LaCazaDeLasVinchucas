package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.List;

public interface OperadorStrategy {
    boolean evaluar(Muestra muestra, List<Filtro> filtros);
} 