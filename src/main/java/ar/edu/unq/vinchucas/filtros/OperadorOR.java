package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.List;

public class OperadorOR implements OperadorStrategy {
    @Override
    public boolean evaluar(Muestra muestra, List<Filtro> filtros) {
        return filtros.stream()
                .anyMatch(filtro -> filtro.filtrar(List.of(muestra)).contains(muestra));
    }
} 