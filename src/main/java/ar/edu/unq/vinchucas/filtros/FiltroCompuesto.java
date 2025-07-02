package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.ArrayList;
import java.util.List;

public class FiltroCompuesto extends FiltroAbstracto {
    private final List<Filtro> filtros;
    private final OperadorStrategy operadorStrategy;

    public FiltroCompuesto(OperadorLogico operador) {
        this.filtros = new ArrayList<>();
        this.operadorStrategy = OperadorStrategyFactory.crearOperador(operador);
    }

    public void agregarFiltro(Filtro filtro) {
        filtros.add(filtro);
    }

    @Override
    protected boolean cumpleCriterio(Muestra muestra) {
        if (filtros.isEmpty()) {
            return true; // Si no hay filtros, todas las muestras cumplen el criterio
        }
        return operadorStrategy.evaluar(muestra, filtros);
    }
} 