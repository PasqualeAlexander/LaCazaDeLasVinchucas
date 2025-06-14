package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;

import java.util.List;

public class FiltroPorZona implements Filtro {
    private final ZonaDeCobertura zona;

    public FiltroPorZona(ZonaDeCobertura zona) {
        this.zona = zona;
    }

    @Override
    public List<Muestra> filtrar(List<Muestra> muestras) {
        return muestras.stream()
                .filter(this::estaEnZona)
                .toList();
    }

    private boolean estaEnZona(Muestra muestra) {
        try {
            Ubicacion ubicacionMuestra = new Ubicacion(muestra.getUbicacion());
            double distancia = zona.getEpicentro().distanciaHasta(ubicacionMuestra);
            return distancia <= zona.getRadio();
        } catch (Exception e) {
            return false; // Si no se puede parsear la ubicación, no está en la zona
        }
    }
} 