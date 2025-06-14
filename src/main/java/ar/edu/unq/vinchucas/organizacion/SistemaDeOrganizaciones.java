package ar.edu.unq.vinchucas.organizacion;

import ar.edu.unq.vinchucas.aplicacion.ISistemaDeOrganizaciones;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;

import java.util.ArrayList;
import java.util.List;

public class SistemaDeOrganizaciones implements ISistemaDeOrganizaciones {
    private List<Organizacion> organizaciones;

    public SistemaDeOrganizaciones() {
        this.organizaciones = new ArrayList<>();
    }

    @Override
    public void registrarOrganizacion(Organizacion organizacion) {
        if (!organizaciones.contains(organizacion)) {
            organizaciones.add(organizacion);
        }
    }

    @Override
    public void eliminarOrganizacion(Organizacion organizacion) {
        organizaciones.remove(organizacion);
    }

    @Override
    public List<Organizacion> getOrganizaciones() {
        return new ArrayList<>(organizaciones);
    }

    @Override
    public List<Organizacion> getOrganizacionesPorTipo(TipoOrganizacion tipo) {
        return organizaciones.stream()
                .filter(org -> org.getTipo() == tipo)
                .toList();
    }

    @Override
    public List<Organizacion> getOrganizacionesCercanas(Ubicacion ubicacion, double radio) {
        return organizaciones.stream()
                .filter(org -> org.getUbicacion().distanciaHasta(ubicacion) <= radio)
                .toList();
    }

    @Override
    public void suscribirOrganizacionAZona(Organizacion organizacion, ZonaDeCobertura zona) {
        if (organizaciones.contains(organizacion)) {
            zona.suscribirOrganizacion(organizacion);
        }
    }

    @Override
    public void desuscribirOrganizacionDeZona(Organizacion organizacion, ZonaDeCobertura zona) {
        zona.desuscribirOrganizacion(organizacion);
    }

    @Override
    public int cantidadOrganizaciones() {
        return organizaciones.size();
    }

    @Override
    public int cantidadOrganizacionesPorTipo(TipoOrganizacion tipo) {
        return (int) organizaciones.stream()
                .filter(org -> org.getTipo() == tipo)
                .count();
    }
} 