package ar.edu.unq.vinchucas.zonas;

import ar.edu.unq.vinchucas.aplicacion.ISistemaDeZonas;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.organizacion.Organizacion;

import java.util.ArrayList;
import java.util.List;

public class SistemaDeZonas implements ISistemaDeZonas {
    private List<ZonaDeCobertura> zonas;

    public SistemaDeZonas() {
        this.zonas = new ArrayList<>();
    }

    @Override
    public void agregarZona(ZonaDeCobertura zona) {
        if (!zonas.contains(zona)) {
            zonas.add(zona);
        }
    }

    @Override
    public void eliminarZona(ZonaDeCobertura zona) {
        zonas.remove(zona);
    }

    @Override
    public List<ZonaDeCobertura> getZonas() {
        return new ArrayList<>(zonas);
    }

    @Override
    public List<ZonaDeCobertura> zonasQueCubren(Muestra muestra) {
        return zonas.stream()
                .filter(zona -> cubre(zona, muestra))
                .toList();
    }

    @Override
    public List<ZonaDeCobertura> zonasQueCubren(Ubicacion ubicacion) {
        return zonas.stream()
                .filter(zona -> cubre(zona, ubicacion))
                .toList();
    }

    @Override
	public void procesarNuevaMuestra(Muestra muestra) {
        List<ZonaDeCobertura> zonasCubiertas = zonasQueCubren(muestra);
        for (ZonaDeCobertura zona : zonasCubiertas) {
            zona.registrarMuestra(muestra);
        }
    }


    private boolean cubre(ZonaDeCobertura zona, Muestra muestra) {
        try {
            Ubicacion ubicacionMuestra = new Ubicacion(muestra.getUbicacion());
            return cubre(zona, ubicacionMuestra);
        } catch (Exception e) {
            return false; // Si no se puede parsear la ubicación, no está cubierta
        }
    }

    private boolean cubre(ZonaDeCobertura zona, Ubicacion ubicacion) {
        return zona.getEpicentro().distanciaHasta(ubicacion) <= zona.getRadio();
    }


} 