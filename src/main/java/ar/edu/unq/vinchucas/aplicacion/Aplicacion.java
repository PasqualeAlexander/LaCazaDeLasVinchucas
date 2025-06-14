package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.usuario.*;
import ar.edu.unq.vinchucas.filtros.Filtro;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.organizacion.Organizacion;
import ar.edu.unq.vinchucas.organizacion.TipoOrganizacion;

import java.util.List;

public class Aplicacion {
    private SistemaDeUsuarios sistemaDeUsuarios;
    private IRepositorioDeMuestras repositorioDeMuestras;
    private ISistemaDeZonas sistemaDeZonas;
    private ISistemaDeOrganizaciones sistemaDeOrganizaciones;

    public Aplicacion(SistemaDeUsuarios sistemaDeUsuarios, IRepositorioDeMuestras repositorioDeMuestras, ISistemaDeZonas sistemaDeZonas, ISistemaDeOrganizaciones sistemaDeOrganizaciones) {
        this.sistemaDeUsuarios = sistemaDeUsuarios;
        this.repositorioDeMuestras = repositorioDeMuestras;
        this.sistemaDeZonas = sistemaDeZonas;
        this.sistemaDeOrganizaciones = sistemaDeOrganizaciones;
    }


    // Metodo para registrar usuario.
    public void registrarUsuario(Usuario usuario) throws SistemaDeExcepciones {
    	this.validarDatosBasicosDeUsuario(usuario);
        this.sistemaDeUsuarios.agregarUsuario(usuario);
    }
    
    // Metodo para validar datos basicos de usuario, las reglas de negocio se delegan al sistema.
    public void validarDatosBasicosDeUsuario(Usuario usuario) throws SistemaDeExcepciones {
    	if (usuario == null) {
            throw new SistemaDeExcepciones("El usuario no puede ser nulo");
        }
        if (usuario.getNombreUsuario().isEmpty() || usuario.getNombreUsuario().isBlank()){
            throw new SistemaDeExcepciones("El nombre es obligatorio");
        }
    }
    
    public void registrarMuestra(Usuario usuario, Muestra muestra) {
    	usuario.enviarMuestra(muestra);
    	repositorioDeMuestras.agregarMuestra(muestra);
    	// Procesar la muestra en las zonas de cobertura
    	sistemaDeZonas.procesarNuevaMuestra(muestra);
    }

    // === FUNCIONALIDAD DE BÚSQUEDA CON FILTROS ===
    
    public List<Muestra> buscarMuestras(Filtro filtro) {
        return repositorioDeMuestras.buscarMuestras(filtro);
    }

    public List<Muestra> buscarMuestras(List<Filtro> filtros) {
        return repositorioDeMuestras.buscarMuestras(filtros);
    }

    public List<Muestra> getMuestrasVerificadas() {
        return repositorioDeMuestras.getMuestrasVerificadas();
    }

    public List<Muestra> getMuestrasNoVerificadas() {
        return repositorioDeMuestras.getMuestrasNoVerificadas();
    }

    // === FUNCIONALIDAD DE ZONAS DE COBERTURA ===
    
    public void registrarZona(ZonaDeCobertura zona) {
        sistemaDeZonas.agregarZona(zona);
    }

    public List<ZonaDeCobertura> getZonas() {
        return sistemaDeZonas.getZonas();
    }

    public List<ZonaDeCobertura> zonasQueCubren(Muestra muestra) {
        return sistemaDeZonas.zonasQueCubren(muestra);
    }

    public List<ZonaDeCobertura> zonasQueCubren(Ubicacion ubicacion) {
        return sistemaDeZonas.zonasQueCubren(ubicacion);
    }

    public void procesarValidacion(Muestra muestra) {
        if (muestra.estaVerificada()) {
            sistemaDeZonas.procesarNuevaValidacion(muestra);
        }
    }

    // === GETTERS PARA ACCESO A SISTEMAS ===
    
    public SistemaDeUsuarios getSistemaDeUsuarios() {
        return sistemaDeUsuarios;
    }

    public IRepositorioDeMuestras getRepositorioDeMuestras() {
        return repositorioDeMuestras;
    }



    public ISistemaDeZonas getSistemaDeZonas() {
        return sistemaDeZonas;
    }

    // === FUNCIONALIDAD DE ORGANIZACIONES ===
    
    public void registrarOrganizacion(Organizacion organizacion) {
        sistemaDeOrganizaciones.registrarOrganizacion(organizacion);
    }

    public void eliminarOrganizacion(Organizacion organizacion) {
        sistemaDeOrganizaciones.eliminarOrganizacion(organizacion);
    }

    public List<Organizacion> getOrganizaciones() {
        return sistemaDeOrganizaciones.getOrganizaciones();
    }

    public List<Organizacion> getOrganizacionesPorTipo(TipoOrganizacion tipo) {
        return sistemaDeOrganizaciones.getOrganizacionesPorTipo(tipo);
    }

    public List<Organizacion> getOrganizacionesCercanas(Ubicacion ubicacion, double radio) {
        return sistemaDeOrganizaciones.getOrganizacionesCercanas(ubicacion, radio);
    }

    public void suscribirOrganizacionAZona(Organizacion organizacion, ZonaDeCobertura zona) {
        sistemaDeOrganizaciones.suscribirOrganizacionAZona(organizacion, zona);
    }

    public void desuscribirOrganizacionDeZona(Organizacion organizacion, ZonaDeCobertura zona) {
        sistemaDeOrganizaciones.desuscribirOrganizacionDeZona(organizacion, zona);
    }

    public int cantidadOrganizaciones() {
        return sistemaDeOrganizaciones.cantidadOrganizaciones();
    }

    public int cantidadOrganizacionesPorTipo(TipoOrganizacion tipo) {
        return sistemaDeOrganizaciones.cantidadOrganizacionesPorTipo(tipo);
    }

    public ISistemaDeOrganizaciones getSistemaDeOrganizaciones() {
        return sistemaDeOrganizaciones;
    }
}

