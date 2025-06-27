package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;

/**
 * Filtro que permite buscar muestras por el tipo de insecto detectado.
 * Implementa el patrón Template Method heredando de FiltroAbstracto.
 */
public class FiltroPorTipoInsecto extends FiltroAbstracto {
    private final TipoDeOpinion tipoInsecto;

    public FiltroPorTipoInsecto(TipoDeOpinion tipoInsecto) {
        this.tipoInsecto = tipoInsecto;
    }

    @Override
    protected boolean cumpleCriterio(Muestra muestra) {
        return muestra.getResultado().equals(tipoInsecto);
    }
} 