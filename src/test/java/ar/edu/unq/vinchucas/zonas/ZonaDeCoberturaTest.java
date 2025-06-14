package ar.edu.unq.vinchucas.zonas;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.organizacion.Organizacion;
import ar.edu.unq.vinchucas.organizacion.OrganizacionImpl;
import ar.edu.unq.vinchucas.organizacion.TipoOrganizacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ZonaDeCoberturaTest {

    private ZonaDeCobertura zona;
    private Ubicacion epicentro;
    private FuncionalidadExterna funcionalidadMock;
    private Organizacion organizacion1;
    private Organizacion organizacion2;
    private Muestra muestra;

    @BeforeEach
    public void setUp() {
        epicentro = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        funcionalidadMock = mock(FuncionalidadExterna.class);
        zona = new ZonaDeCobertura("Zona Centro", epicentro, 10.0, funcionalidadMock);
        
        // Crear organizaciones
        Ubicacion ubicacionOrg1 = new Ubicacion(-34.6037, -58.3816);
        Ubicacion ubicacionOrg2 = new Ubicacion(-31.4201, -64.1888);
        organizacion1 = new OrganizacionImpl("Hospital", ubicacionOrg1, TipoOrganizacion.SALUD, 100);
        organizacion2 = new OrganizacionImpl("Escuela", ubicacionOrg2, TipoOrganizacion.EDUCATIVA, 50);
        
        // Crear muestra mock
        muestra = mock(Muestra.class);
        when(muestra.getUbicacion()).thenReturn("-34.6037,-58.3816"); // Dentro de la zona
    }

    @Test
    public void testCreacionZona() {
        assertEquals("Zona Centro", zona.getNombre());
        assertEquals(epicentro, zona.getEpicentro());
        assertEquals(10.0, zona.getRadio());
        assertTrue(zona.getOrganizacionesSuscriptas().isEmpty());
        assertTrue(zona.getMuestrasReportadas().isEmpty());
    }

    @Test
    public void testSuscribirOrganizacion() {
        zona.suscribirOrganizacion(organizacion1);
        
        assertEquals(1, zona.getOrganizacionesSuscriptas().size());
        assertTrue(zona.getOrganizacionesSuscriptas().contains(organizacion1));
    }

    @Test
    public void testSuscribirMultiplesOrganizaciones() {
        zona.suscribirOrganizacion(organizacion1);
        zona.suscribirOrganizacion(organizacion2);
        
        assertEquals(2, zona.getOrganizacionesSuscriptas().size());
        assertTrue(zona.getOrganizacionesSuscriptas().contains(organizacion1));
        assertTrue(zona.getOrganizacionesSuscriptas().contains(organizacion2));
    }

    @Test
    public void testDesuscribirOrganizacion() {
        zona.suscribirOrganizacion(organizacion1);
        zona.suscribirOrganizacion(organizacion2);
        
        zona.desuscribirOrganizacion(organizacion1);
        
        assertEquals(1, zona.getOrganizacionesSuscriptas().size());
        assertFalse(zona.getOrganizacionesSuscriptas().contains(organizacion1));
        assertTrue(zona.getOrganizacionesSuscriptas().contains(organizacion2));
    }

    @Test
    public void testRegistrarMuestraDentroDeZona() {
        zona.suscribirOrganizacion(organizacion1);
        
        zona.registrarMuestra(muestra);
        
        assertEquals(1, zona.getMuestrasReportadas().size());
        assertTrue(zona.getMuestrasReportadas().contains(muestra));
        verify(funcionalidadMock).nuevoEvento(organizacion1, zona, muestra);
    }

    @Test
    public void testRegistrarMuestraFueraDeZona() {
        when(muestra.getUbicacion()).thenReturn("-31.4201,-64.1888"); // Córdoba, fuera de la zona
        zona.suscribirOrganizacion(organizacion1);
        
        zona.registrarMuestra(muestra);
        
        assertEquals(0, zona.getMuestrasReportadas().size());
        verify(funcionalidadMock, never()).nuevoEvento(any(), any(), any());
    }

    @Test
    public void testRegistrarMuestraNotificaTodasLasOrganizaciones() {
        zona.suscribirOrganizacion(organizacion1);
        zona.suscribirOrganizacion(organizacion2);
        
        zona.registrarMuestra(muestra);
        
        verify(funcionalidadMock).nuevoEvento(organizacion1, zona, muestra);
        verify(funcionalidadMock).nuevoEvento(organizacion2, zona, muestra);
    }

    @Test
    public void testZonasQueSolapan() {
        // Crear otras zonas
        Ubicacion epicentro2 = new Ubicacion(-34.6100, -58.3900); // Cerca de la zona original
        Ubicacion epicentro3 = new Ubicacion(-31.4201, -64.1888); // Lejos de la zona original
        
        ZonaDeCobertura zona2 = new ZonaDeCobertura("Zona Norte", epicentro2, 5.0, funcionalidadMock);
        ZonaDeCobertura zona3 = new ZonaDeCobertura("Zona Córdoba", epicentro3, 5.0, funcionalidadMock);
        
        List<ZonaDeCobertura> todasLasZonas = List.of(zona, zona2, zona3);
        
        List<ZonaDeCobertura> solapantes = zona.zonasQueSolapan(todasLasZonas);
        
        assertEquals(1, solapantes.size());
        assertTrue(solapantes.contains(zona2));
        assertFalse(solapantes.contains(zona3));
    }

    @Test
    public void testZonasQueSolapanNoIncluyeLaMismaZona() {
        List<ZonaDeCobertura> todasLasZonas = List.of(zona);
        
        List<ZonaDeCobertura> solapantes = zona.zonasQueSolapan(todasLasZonas);
        
        assertTrue(solapantes.isEmpty());
    }


    @Test
    public void testRegistrarMuestraConUbicacionInvalida() {
        when(muestra.getUbicacion()).thenReturn("ubicacion-invalida");
        zona.suscribirOrganizacion(organizacion1);
        
        // No debería lanzar excepción, pero tampoco registrar la muestra
        assertDoesNotThrow(() -> zona.registrarMuestra(muestra));
        assertEquals(0, zona.getMuestrasReportadas().size());
    }

} 