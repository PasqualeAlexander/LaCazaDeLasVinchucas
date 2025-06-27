package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.time.LocalDate;

/**
 * Filtro que permite buscar muestras por rango de fechas de la última votación.
 * Implementa el patrón Template Method heredando de FiltroAbstracto.
 */
public class FiltroPorUltimaVotacion extends FiltroAbstracto {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public FiltroPorUltimaVotacion(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    protected boolean cumpleCriterio(Muestra muestra) {
        LocalDate fechaUltimaVotacion = muestra.getFechaUltimaVotacion();
        if (fechaUltimaVotacion == null) {
            return false;
        }
        return !fechaUltimaVotacion.isBefore(fechaInicio) && !fechaUltimaVotacion.isAfter(fechaFin);
    }
} 