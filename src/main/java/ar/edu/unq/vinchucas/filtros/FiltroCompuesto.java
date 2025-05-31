package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.ArrayList;
import java.util.List;

public class FiltroCompuesto implements Filtro {
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
    public List<Muestra> filtrar(List<Muestra> muestras) {
        if (filtros.isEmpty()) {
            return muestras;
        }

        return muestras.stream()
                .filter(muestra -> operadorStrategy.evaluar(muestra, filtros))
                .toList();
    }
} 