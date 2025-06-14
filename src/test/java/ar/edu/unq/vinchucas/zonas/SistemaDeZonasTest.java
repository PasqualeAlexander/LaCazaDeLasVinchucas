package ar.edu.unq.vinchucas.zonas;

import ar.edu.unq.vinchucas.aplicacion.ISistemaDeZonas;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.organizacion.Organizacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SistemaDeZonasTest {

    private ISistemaDeZonas sistemaDeZonas;
    private ZonaDeCobertura zona1;
    private ZonaDeCobertura zona2;
    private Muestra muestra;
    private Organizacion organizacion;

    @BeforeEach
    public void setUp() {
        sistemaDeZonas = new SistemaDeZonas();
        
        // Crear zonas de cobertura mock
        zona1 = mock(ZonaDeCobertura.class);
        zona2 = mock(ZonaDeCobertura.class);
        
        // Crear muestra mock
        muestra = mock(Muestra.class);
        when(muestra.getUbicacion()).thenReturn("-34.6037,-58.3816"); // Buenos Aires
        
        // Crear organización mock
        organizacion = mock(Organizacion.class);
        
        // Configurar zona1 para que cubra Buenos Aires
        Ubicacion epicentro1 = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        when(zona1.getEpicentro()).thenReturn(epicentro1);
        when(zona1.getRadio()).thenReturn(10.0); // 10 km de radio
        
        // Configurar zona2 para que NO cubra Buenos Aires
        Ubicacion epicentro2 = new Ubicacion(-31.4201, -64.1888); // Córdoba
        when(zona2.getEpicentro()).thenReturn(epicentro2);
        when(zona2.getRadio()).thenReturn(5.0); // 5 km de radio
    }

    @Test
    public void testAgregarZona() {
        sistemaDeZonas.agregarZona(zona1);
        
        assertEquals(1, sistemaDeZonas.getZonas().size());
        assertTrue(sistemaDeZonas.getZonas().contains(zona1));
    }

    @Test
    public void testAgregarZonaDuplicadaNoSeAgrega() {
        sistemaDeZonas.agregarZona(zona1);
        sistemaDeZonas.agregarZona(zona1); // Intentamos agregar la misma zona
        
        assertEquals(1, sistemaDeZonas.getZonas().size());
    }

    @Test
    public void testEliminarZona() {
        sistemaDeZonas.agregarZona(zona1);
        sistemaDeZonas.agregarZona(zona2);
        
        sistemaDeZonas.eliminarZona(zona1);
        
        assertEquals(1, sistemaDeZonas.getZonas().size());
        assertFalse(sistemaDeZonas.getZonas().contains(zona1));
        assertTrue(sistemaDeZonas.getZonas().contains(zona2));
    }

    @Test
    public void testZonasQueCubrenMuestra() {
        sistemaDeZonas.agregarZona(zona1);
        sistemaDeZonas.agregarZona(zona2);
        
        var zonasCubren = sistemaDeZonas.zonasQueCubren(muestra);
        
        assertEquals(1, zonasCubren.size());
        assertTrue(zonasCubren.contains(zona1));
        assertFalse(zonasCubren.contains(zona2));
    }

    @Test
    public void testZonasQueCubrenUbicacion() {
        sistemaDeZonas.agregarZona(zona1);
        sistemaDeZonas.agregarZona(zona2);
        
        Ubicacion ubicacionBuenosAires = new Ubicacion(-34.6037, -58.3816);
        var zonasCubren = sistemaDeZonas.zonasQueCubren(ubicacionBuenosAires);
        
        assertEquals(1, zonasCubren.size());
        assertTrue(zonasCubren.contains(zona1));
    }

    @Test
    public void testProcesarNuevaMuestra() {
        sistemaDeZonas.agregarZona(zona1);
        sistemaDeZonas.agregarZona(zona2);
        
        sistemaDeZonas.procesarNuevaMuestra(muestra);
        
        // Verificamos que solo la zona que cubre la muestra es notificada
        verify(zona1).registrarMuestra(muestra);
        verify(zona2, never()).registrarMuestra(muestra);
    }

    @Test
    public void testProcesarNuevaValidacionSoloSiEstaVerificada() {
        when(muestra.estaVerificada()).thenReturn(false);
        sistemaDeZonas.agregarZona(zona1);
        
        sistemaDeZonas.procesarNuevaValidacion(muestra);
        
        // No debe procesar nada si no está verificada
        verify(zona1, never()).getOrganizacionesSuscriptas();
        
        // Ahora la verificamos
        when(muestra.estaVerificada()).thenReturn(true);
        sistemaDeZonas.procesarNuevaValidacion(muestra);
        
        // Ahora sí debe procesar
        verify(zona1).getOrganizacionesSuscriptas();
    }

    @Test
    public void testProcesarNuevaValidacionConOrganizacionesSuscritas() {
        when(muestra.estaVerificada()).thenReturn(true);
        when(zona1.getOrganizacionesSuscriptas()).thenReturn(java.util.List.of(organizacion));
        sistemaDeZonas.agregarZona(zona1);
        
        sistemaDeZonas.procesarNuevaValidacion(muestra);
        
        // Verificamos que se notifica a la organización
        verify(organizacion).procesarNuevaValidacion(muestra, zona1);
    }

    @Test
    public void testZonasQueCubrenMuestraConUbicacionInvalida() {
        Muestra muestraInvalida = mock(Muestra.class);
        when(muestraInvalida.getUbicacion()).thenReturn("ubicacion-invalida");
        
        sistemaDeZonas.agregarZona(zona1);
        
        var zonasCubren = sistemaDeZonas.zonasQueCubren(muestraInvalida);
        
        // No debe cubrir ninguna zona si la ubicación es inválida
        assertEquals(0, zonasCubren.size());
    }

    @Test
    public void testGetZonasDevuelveCopia() {
        sistemaDeZonas.agregarZona(zona1);
        
        var zonas = sistemaDeZonas.getZonas();
        zonas.clear(); // Modificamos la copia
        
        // El sistema original no debe verse afectado
        assertEquals(1, sistemaDeZonas.getZonas().size());
    }
} 