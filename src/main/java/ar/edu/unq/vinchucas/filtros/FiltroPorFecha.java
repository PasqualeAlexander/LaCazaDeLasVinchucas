package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.time.LocalDate;

/**
 * Filtro que permite buscar muestras por rango de fechas de creación.
 * Implementa el patrón Template Method heredando de FiltroAbstracto.
 */
public class FiltroPorFecha extends FiltroAbstracto {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public FiltroPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    protected boolean cumpleCriterio(Muestra muestra) {
        LocalDate fechaMuestra = muestra.getFechaCreacion();
        return !fechaMuestra.isBefore(fechaInicio) && !fechaMuestra.isAfter(fechaFin);
    }
} 