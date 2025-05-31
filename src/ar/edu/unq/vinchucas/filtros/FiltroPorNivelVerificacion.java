package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.List;

public class FiltroPorNivelVerificacion implements Filtro { // TOFO: Fix
    private final boolean verificada;

    public FiltroPorNivelVerificacion(boolean verificada) {
        this.verificada = verificada;
    }

    @Override
    public List<Muestra> filtrar(List<Muestra> muestras) {
        return muestras.stream()
                .filter(muestra -> muestra.estaVerificada() == verificada)
                .toList();
    }
} 