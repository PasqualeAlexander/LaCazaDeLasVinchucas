package ar.edu.unq.vinchucas.organizacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;

public interface Organizacion {
    /**
     * Obtiene la ubicación de la organización
     */
    Ubicacion getUbicacion();
    
    /**
     * Obtiene el tipo de organización (salud, educativa, cultural, asistencia)
     */
    TipoOrganizacion getTipo();
    
    /**
     * Obtiene la cantidad de personas que trabajan en la organización
     */
    int getCantidadTrabajadores();
    
    /**
     * Procesa un nuevo evento de muestra en una zona
     */
    void procesarNuevaMuestra(Muestra muestra, ZonaDeCobertura zona);
    
    /**
     * Procesa un nuevo evento de validación en una zona
     */
    void procesarNuevaValidacion(Muestra muestra, ZonaDeCobertura zona);
    
    /**
     * Configura la funcionalidad externa para nuevas muestras
    
    void setFuncionalidadNuevaMuestra(FuncionalidadExterna funcionalidad);
     */
    /**
     * Configura la funcionalidad externa para validaciones
    
    void setFuncionalidadValidacion(FuncionalidadExterna funcionalidad);
     */
} 