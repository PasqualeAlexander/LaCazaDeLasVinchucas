package ar.edu.unq.vinchucas.organizacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.FuncionalidadExterna;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrganizacionImplTest {

    private OrganizacionImpl organizacion;
    private Ubicacion ubicacion;
    private Muestra muestra;
    private ZonaDeCobertura zona;
    private FuncionalidadExterna funcionalidadMock;

    @BeforeEach
    public void setUp() {
        ubicacion = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        organizacion = new OrganizacionImpl("Hospital Italiano", ubicacion, TipoOrganizacion.SALUD, 150);
        
        muestra = mock(Muestra.class);
        zona = mock(ZonaDeCobertura.class);
        when(zona.getNombre()).thenReturn("Zona Centro");
        
        funcionalidadMock = mock(FuncionalidadExterna.class);
    }

    @Test
    public void testCreacionOrganizacion() {
        assertEquals("Hospital Italiano", organizacion.getNombre());
        assertEquals(ubicacion, organizacion.getUbicacion());
        assertEquals(TipoOrganizacion.SALUD, organizacion.getTipo());
        assertEquals(150, organizacion.getCantidadTrabajadores());
    }

    @Test
    public void testProcesarNuevaMuestraSinFuncionalidad() {
        // No debería lanzar excepción, solo hacer log por defecto
        assertDoesNotThrow(() -> {
            organizacion.procesarNuevaMuestra(muestra, zona);
        });
    }

    @Test
    public void testProcesarNuevaValidacionSinFuncionalidad() {
        when(muestra.getResultado()).thenReturn(ar.edu.unq.vinchucas.muestra.TipoDeOpinion.VINCHUCA_INFESTANS);
        
        // No debería lanzar excepción, solo hacer log por defecto
        assertDoesNotThrow(() -> {
            organizacion.procesarNuevaValidacion(muestra, zona);
        });
    }

    @Test
    public void testProcesarNuevaMuestraConFuncionalidad() {
        organizacion.setFuncionalidadNuevaMuestra(funcionalidadMock);
        
        organizacion.procesarNuevaMuestra(muestra, zona);
        
        verify(funcionalidadMock).nuevoEvento(organizacion, zona, muestra);
    }

    @Test
    public void testProcesarNuevaValidacionConFuncionalidad() {
        organizacion.setFuncionalidadValidacion(funcionalidadMock);
        
        organizacion.procesarNuevaValidacion(muestra, zona);
        
        verify(funcionalidadMock).nuevoEvento(organizacion, zona, muestra);
    }

    @Test
    public void testSetFuncionalidades() {
        FuncionalidadExterna funcionalidad1 = mock(FuncionalidadExterna.class);
        FuncionalidadExterna funcionalidad2 = mock(FuncionalidadExterna.class);
        
        organizacion.setFuncionalidadNuevaMuestra(funcionalidad1);
        organizacion.setFuncionalidadValidacion(funcionalidad2);
        
        organizacion.procesarNuevaMuestra(muestra, zona);
        organizacion.procesarNuevaValidacion(muestra, zona);
        
        verify(funcionalidad1).nuevoEvento(organizacion, zona, muestra);
        verify(funcionalidad2).nuevoEvento(organizacion, zona, muestra);
    }

    @Test
    public void testEqualsYHashCode() {
        OrganizacionImpl organizacion2 = new OrganizacionImpl("Hospital Italiano", ubicacion, TipoOrganizacion.SALUD, 150);
        OrganizacionImpl organizacion3 = new OrganizacionImpl("Hospital Italiano", new Ubicacion(-31.4201, -64.1888), TipoOrganizacion.SALUD, 150);
        
        assertEquals(organizacion, organizacion2);
        assertEquals(organizacion.hashCode(), organizacion2.hashCode());
        
        assertNotEquals(organizacion, organizacion3);
        assertNotEquals(organizacion.hashCode(), organizacion3.hashCode());
    }

    @Test
    public void testToString() {
        String resultado = organizacion.toString();
        
        assertTrue(resultado.contains("Hospital Italiano"));
        assertTrue(resultado.contains("SALUD"));
        assertTrue(resultado.contains("150"));
    }

    @Test
    public void testOrganizacionesConDiferentesTipos() {
        OrganizacionImpl escuela = new OrganizacionImpl("Escuela Primaria", ubicacion, TipoOrganizacion.EDUCATIVA, 25);
        OrganizacionImpl centro = new OrganizacionImpl("Centro Cultural", ubicacion, TipoOrganizacion.CULTURAL, 8);
        OrganizacionImpl asistencia = new OrganizacionImpl("Cruz Roja", ubicacion, TipoOrganizacion.ASISTENCIA, 50);
        
        assertEquals(TipoOrganizacion.EDUCATIVA, escuela.getTipo());
        assertEquals(TipoOrganizacion.CULTURAL, centro.getTipo());
        assertEquals(TipoOrganizacion.ASISTENCIA, asistencia.getTipo());
    }
} 