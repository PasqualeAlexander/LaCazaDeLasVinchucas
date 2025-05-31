package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FiltroPorFecha implements Filtro {
    private final LocalDate fechaDesde;
    private final LocalDate fechaHasta;

    public FiltroPorFecha(LocalDate fechaDesde, LocalDate fechaHasta) {
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
    }

    @Override
    public List<Muestra> filtrar(List<Muestra> muestras) {
        return muestras.stream()
                .filter(muestra -> !muestra.getFechaCreacion().isBefore(fechaDesde) &&
                        !muestra.getFechaCreacion().isAfter(fechaHasta))
                .collect(Collectors.toList());
    }
} 