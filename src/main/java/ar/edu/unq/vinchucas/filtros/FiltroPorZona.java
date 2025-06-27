package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;

/**
 * Filtro que permite buscar muestras que se encuentran dentro de una zona de cobertura.
 * Implementa el patrón Template Method heredando de FiltroAbstracto.
 */
public class FiltroPorZona extends FiltroAbstracto {
    private final ZonaDeCobertura zona;

    public FiltroPorZona(ZonaDeCobertura zona) {
        this.zona = zona;
    }

    @Override
    protected boolean cumpleCriterio(Muestra muestra) {
        try {
            Ubicacion ubicacionMuestra = new Ubicacion(muestra.getUbicacion());
            double distancia = zona.getEpicentro().distanciaHasta(ubicacionMuestra);
            return distancia <= zona.getRadio();
        } catch (Exception e) {
            return false; // Si no se puede parsear la ubicación, no está en la zona
        }
    }
} 