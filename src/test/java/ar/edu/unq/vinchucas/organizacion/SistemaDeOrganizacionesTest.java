package ar.edu.unq.vinchucas.organizacion;

import ar.edu.unq.vinchucas.aplicacion.ISistemaDeOrganizaciones;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SistemaDeOrganizacionesTest {

    private ISistemaDeOrganizaciones sistema;
    private Organizacion hospital;
    private Organizacion escuela;
    private Organizacion centro;
    private ZonaDeCobertura zona;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaDeOrganizaciones();
        
        Ubicacion ubicacionBA = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        Ubicacion ubicacionCordoba = new Ubicacion(-31.4201, -64.1888); // Córdoba
        
        hospital = new OrganizacionImpl("Hospital Italiano", ubicacionBA, TipoOrganizacion.SALUD, 150);
        escuela = new OrganizacionImpl("Escuela Primaria", ubicacionBA, TipoOrganizacion.EDUCATIVA, 25);
        centro = new OrganizacionImpl("Centro Cultural", ubicacionCordoba, TipoOrganizacion.CULTURAL, 8);
        
        zona = mock(ZonaDeCobertura.class);
    }

    @Test
    public void testRegistrarOrganizacion() {
        sistema.registrarOrganizacion(hospital);
        
        assertEquals(1, sistema.cantidadOrganizaciones());
        assertTrue(sistema.getOrganizaciones().contains(hospital));
    }

    @Test
    public void testRegistrarOrganizacionDuplicadaNoSeAgrega() {
        sistema.registrarOrganizacion(hospital);
        sistema.registrarOrganizacion(hospital); // Intentamos agregar la misma
        
        assertEquals(1, sistema.cantidadOrganizaciones());
    }

    @Test
    public void testEliminarOrganizacion() {
        sistema.registrarOrganizacion(hospital);
        sistema.registrarOrganizacion(escuela);
        
        sistema.eliminarOrganizacion(hospital);
        
        assertEquals(1, sistema.cantidadOrganizaciones());
        assertFalse(sistema.getOrganizaciones().contains(hospital));
        assertTrue(sistema.getOrganizaciones().contains(escuela));
    }

    @Test
    public void testGetOrganizacionesPorTipo() {
        sistema.registrarOrganizacion(hospital);
        sistema.registrarOrganizacion(escuela);
        sistema.registrarOrganizacion(centro);
        
        var organizacionesSalud = sistema.getOrganizacionesPorTipo(TipoOrganizacion.SALUD);
        var organizacionesEducativas = sistema.getOrganizacionesPorTipo(TipoOrganizacion.EDUCATIVA);
        var organizacionesCulturales = sistema.getOrganizacionesPorTipo(TipoOrganizacion.CULTURAL);
        
        assertEquals(1, organizacionesSalud.size());
        assertTrue(organizacionesSalud.contains(hospital));
        
        assertEquals(1, organizacionesEducativas.size());
        assertTrue(organizacionesEducativas.contains(escuela));
        
        assertEquals(1, organizacionesCulturales.size());
        assertTrue(organizacionesCulturales.contains(centro));
    }

    @Test
    public void testGetOrganizacionesCercanas() {
        sistema.registrarOrganizacion(hospital);
        sistema.registrarOrganizacion(escuela);
        sistema.registrarOrganizacion(centro);
        
        Ubicacion ubicacionBA = new Ubicacion(-34.6037, -58.3816);
        
        // Buscar organizaciones a 1 km de Buenos Aires
        var cercanas = sistema.getOrganizacionesCercanas(ubicacionBA, 1.0);
        
        assertEquals(2, cercanas.size()); // hospital y escuela están en BA
        assertTrue(cercanas.contains(hospital));
        assertTrue(cercanas.contains(escuela));
        assertFalse(cercanas.contains(centro)); // centro está en Córdoba
    }

    @Test
    public void testSuscribirOrganizacionAZona() {
        sistema.registrarOrganizacion(hospital);
        
        sistema.suscribirOrganizacionAZona(hospital, zona);
        
        verify(zona).suscribirOrganizacion(hospital);
    }

    @Test
    public void testSuscribirOrganizacionNoRegistradaNoHaceNada() {
        // Intentamos suscribir una organización que no está registrada
        sistema.suscribirOrganizacionAZona(hospital, zona);
        
        verify(zona, never()).suscribirOrganizacion(hospital);
    }

    @Test
    public void testDesuscribirOrganizacionDeZona() {
        sistema.registrarOrganizacion(hospital);
        
        sistema.desuscribirOrganizacionDeZona(hospital, zona);
        
        verify(zona).desuscribirOrganizacion(hospital);
    }

    @Test
    public void testCantidadOrganizacionesPorTipo() {
        sistema.registrarOrganizacion(hospital);
        sistema.registrarOrganizacion(escuela);
        sistema.registrarOrganizacion(centro);
        
        assertEquals(1, sistema.cantidadOrganizacionesPorTipo(TipoOrganizacion.SALUD));
        assertEquals(1, sistema.cantidadOrganizacionesPorTipo(TipoOrganizacion.EDUCATIVA));
        assertEquals(1, sistema.cantidadOrganizacionesPorTipo(TipoOrganizacion.CULTURAL));
        assertEquals(0, sistema.cantidadOrganizacionesPorTipo(TipoOrganizacion.ASISTENCIA));
    }

    @Test
    public void testGetOrganizacionesDevuelveCopia() {
        sistema.registrarOrganizacion(hospital);
        
        var organizaciones = sistema.getOrganizaciones();
        organizaciones.clear(); // Modificamos la copia
        
        // El sistema original no debe verse afectado
        assertEquals(1, sistema.cantidadOrganizaciones());
    }

    @Test
    public void testSistemaVacio() {
        assertEquals(0, sistema.cantidadOrganizaciones());
        assertTrue(sistema.getOrganizaciones().isEmpty());
        assertEquals(0, sistema.cantidadOrganizacionesPorTipo(TipoOrganizacion.SALUD));
    }
} 