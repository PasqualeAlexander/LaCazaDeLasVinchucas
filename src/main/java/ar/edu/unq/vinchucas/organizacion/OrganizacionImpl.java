package ar.edu.unq.vinchucas.organizacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.FuncionalidadExterna;

public class OrganizacionImpl implements Organizacion {
    private final String nombre;
    private final Ubicacion ubicacion;
    private final TipoOrganizacion tipo;
    private final int cantidadTrabajadores;
    private FuncionalidadExterna funcionalidadNuevaMuestra;
    private FuncionalidadExterna funcionalidadValidacion;
    
    public OrganizacionImpl(String nombre, Ubicacion ubicacion, TipoOrganizacion tipo, int cantidadTrabajadores) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
        this.cantidadTrabajadores = cantidadTrabajadores;
    }
    
    @Override
    public Ubicacion getUbicacion() {
        return ubicacion;
    }
    
    @Override
    public TipoOrganizacion getTipo() {
        return tipo;
    }
    
    @Override
    public int getCantidadTrabajadores() {
        return cantidadTrabajadores;
    }

    @Override
    public void procesarNuevaMuestra(Muestra muestra, ZonaDeCobertura zona) {
        if (funcionalidadNuevaMuestra != null) {
            funcionalidadNuevaMuestra.nuevoEvento(this, zona, muestra);
        } else {
            // Comportamiento por defecto: log simple
            System.out.println("Nueva muestra detectada en " + zona.getNombre() + 
                             " por organización " + nombre + " (" + tipo + ")");
        }
    }

    @Override
    public void procesarNuevaValidacion(Muestra muestra, ZonaDeCobertura zona) {
        if (funcionalidadValidacion != null) {
            funcionalidadValidacion.nuevoEvento(this, zona, muestra);
        } else {
            // Comportamiento por defecto: log simple
            System.out.println("Muestra verificada en " + zona.getNombre() + 
                             " - Resultado: " + muestra.getResultado() + 
                             " (Notificado a " + nombre + ")");
        }
    }

    public void setFuncionalidadNuevaMuestra(FuncionalidadExterna funcionalidad) {
        this.funcionalidadNuevaMuestra = funcionalidad;
    }

    public void setFuncionalidadValidacion(FuncionalidadExterna funcionalidad) {
        this.funcionalidadValidacion = funcionalidad;
    }
    
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Organización{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", trabajadores=" + cantidadTrabajadores +
                ", ubicacion=" + ubicacion +
                '}';
    }
} 