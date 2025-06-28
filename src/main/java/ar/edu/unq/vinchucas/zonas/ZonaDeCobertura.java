package ar.edu.unq.vinchucas.zonas;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.ObservadorMuestra;
import ar.edu.unq.vinchucas.organizacion.Organizacion;
import java.util.ArrayList;
import java.util.List;

public class ZonaDeCobertura implements ObservadorMuestra {
    private final String nombre;
    private final Ubicacion epicentro;
    private final double radio;
    private final List<Organizacion> organizacionesSuscriptas;
    private final List<Muestra> muestrasReportadas;
    private final FuncionalidadExterna funcionalidadExterna;

    public ZonaDeCobertura(String nombre, Ubicacion epicentro, double radio, FuncionalidadExterna funcionalidadExterna) {
        this.nombre = nombre;
        this.epicentro = epicentro;
        this.radio = radio;
        this.organizacionesSuscriptas = new ArrayList<>();
        this.muestrasReportadas = new ArrayList<>();
        this.funcionalidadExterna = funcionalidadExterna;
    }

    public void suscribirOrganizacion(Organizacion organizacion) {
        organizacionesSuscriptas.add(organizacion);
    }

    public void desuscribirOrganizacion(Organizacion organizacion) {
        organizacionesSuscriptas.remove(organizacion);
    }

    public void registrarMuestra(Muestra muestra) {
        if (estaEnZona(muestra)) {
            muestrasReportadas.add(muestra);
            // Suscribirse como observador de la muestra
            muestra.agregarObservador(this);
            notificarNuevaMuestra(muestra);
        }
    }

    private boolean estaEnZona(Muestra muestra) {
        try {
            Ubicacion ubicacionMuestra = new Ubicacion(muestra.getUbicacion());
            return epicentro.distanciaHasta(ubicacionMuestra) <= radio;
        } catch (NumberFormatException e) {
            // Si la ubicación es inválida, consideramos que no está en la zona
            return false;
        }
    }

    private void notificarNuevaMuestra(Muestra muestra) {
        for (Organizacion organizacion : organizacionesSuscriptas) {
            funcionalidadExterna.nuevoEvento(organizacion, this, muestra);
        }
    }

    public List<ZonaDeCobertura> zonasQueSolapan(List<ZonaDeCobertura> todasLasZonas) {
        return todasLasZonas.stream()
                .filter(zona -> !zona.equals(this))
                .filter(zona -> epicentro.distanciaHasta(zona.epicentro) <= (radio + zona.radio))
                .toList();
    }

    public String getNombre() {
        return nombre;
    }

    public Ubicacion getEpicentro() {
        return epicentro;
    }

    public double getRadio() {
        return radio;
    }

    public List<Organizacion> getOrganizacionesSuscriptas() {
        return organizacionesSuscriptas;
    }

    public List<Muestra> getMuestrasReportadas() {
        return muestrasReportadas;
    }
    
    @Override
    public void muestraVerificada(Muestra muestra) {
        // Solo procesar si la muestra está en esta zona
        if (estaEnZona(muestra)) {
            notificarMuestraVerificada(muestra);
        }
    }
    
    private void notificarMuestraVerificada(Muestra muestra) {
        for (Organizacion organizacion : organizacionesSuscriptas) {
            funcionalidadExterna.muestraVerificada(organizacion, this, muestra);
        }
    }
} 