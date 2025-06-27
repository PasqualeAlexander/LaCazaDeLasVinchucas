package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;

/**
 * Filtro que permite buscar muestras por su nivel de verificación.
 * Implementa el patrón Template Method heredando de FiltroAbstracto.
 */
public class FiltroPorNivelVerificacion extends FiltroAbstracto {
    private final boolean verificada;

    public FiltroPorNivelVerificacion(boolean verificada) {
        this.verificada = verificada;
    }

    @Override
    protected boolean cumpleCriterio(Muestra muestra) {
        return muestra.estaVerificada() == verificada;
    }
} 