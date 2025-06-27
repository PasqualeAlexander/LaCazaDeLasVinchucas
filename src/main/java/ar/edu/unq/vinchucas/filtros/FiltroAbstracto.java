package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import java.util.List;

/**
 * Clase abstracta que implementa el patrón Template Method para los filtros.
 * Define el algoritmo común de filtrado y delega la lógica específica a las subclases.
 */
public abstract class FiltroAbstracto implements Filtro {
    
    /**
     * Template Method: Define el algoritmo común de filtrado.
     * Las subclases solo deben implementar el método cumpleCriterio().
     */
    @Override
    public final List<Muestra> filtrar(List<Muestra> muestras) {
        return muestras.stream()
                .filter(this::cumpleCriterio)
                .toList();
    }
    
    /**
     * Método abstracto que debe ser implementado por las subclases.
     * Define el criterio específico de filtrado para cada tipo de filtro.
     * 
     * @param muestra La muestra a evaluar
     * @return true si la muestra cumple con el criterio del filtro, false en caso contrario
     */
    protected abstract boolean cumpleCriterio(Muestra muestra);
} 