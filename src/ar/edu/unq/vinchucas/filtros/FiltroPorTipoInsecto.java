package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import java.util.List;

public class FiltroPorTipoInsecto implements Filtro {
    private final TipoDeOpinion tipoInsecto;

    public FiltroPorTipoInsecto(TipoDeOpinion tipoInsecto) {
        this.tipoInsecto = tipoInsecto;
    }

    @Override
    public List<Muestra> filtrar(List<Muestra> muestras) {
        return muestras.stream()
                .filter(muestra -> muestra.getResultado().equals(tipoInsecto))
                .toList();
    }
} 