package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.organizacion.Organizacion;
import ar.edu.unq.vinchucas.organizacion.TipoOrganizacion;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;

import java.util.List;

public interface ISistemaDeOrganizaciones {
    void registrarOrganizacion(Organizacion organizacion);
    void eliminarOrganizacion(Organizacion organizacion);
    List<Organizacion> getOrganizaciones();
    List<Organizacion> getOrganizacionesPorTipo(TipoOrganizacion tipo);
    List<Organizacion> getOrganizacionesCercanas(Ubicacion ubicacion, double radio);
    void suscribirOrganizacionAZona(Organizacion organizacion, ZonaDeCobertura zona);
    void desuscribirOrganizacionDeZona(Organizacion organizacion, ZonaDeCobertura zona);
    int cantidadOrganizaciones();
    int cantidadOrganizacionesPorTipo(TipoOrganizacion tipo);
} 