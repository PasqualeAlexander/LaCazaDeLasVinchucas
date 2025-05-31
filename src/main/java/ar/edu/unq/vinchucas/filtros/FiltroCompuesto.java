package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.ArrayList;
import java.util.List;

public class FiltroCompuesto implements Filtro {
    private final List<Filtro> filtros;
    private final OperadorLogico operador;

    public FiltroCompuesto(OperadorLogico operador) {
        this.filtros = new ArrayList<>();
        this.operador = operador;
    }

    public void agregarFiltro(Filtro filtro) {
        filtros.add(filtro);
    }

    @Override
    public List<Muestra> filtrar(List<Muestra> muestras) {
        if (filtros.isEmpty()) {
            return muestras;
        }

        List<Muestra> resultado = new ArrayList<>(muestras);
        
        for (Filtro filtro : filtros) {
            List<Muestra> resultadoFiltro = filtro.filtrar(muestras);
            
            if (operador == OperadorLogico.AND) {
                resultado.retainAll(resultadoFiltro);
            } else {
                resultado.addAll(resultadoFiltro);
                resultado = resultado.stream().distinct().toList();
            }
        }

        return resultado;
    }
} 