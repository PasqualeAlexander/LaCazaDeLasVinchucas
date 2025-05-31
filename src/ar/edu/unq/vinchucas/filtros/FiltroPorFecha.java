package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.time.LocalDate;
import java.util.List;

public class FiltroPorFecha implements Filtro {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;

    public FiltroPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    @Override
    public List<Muestra> filtrar(List<Muestra> muestras) {
        return muestras.stream()
                .filter(this::estaEnRangoFecha)
                .toList();
    }

    private boolean estaEnRangoFecha(Muestra muestra) {
        LocalDate fechaMuestra = muestra.getFechaCreacion();
        return !fechaMuestra.isBefore(fechaInicio) && !fechaMuestra.isAfter(fechaFin);
    }
} 