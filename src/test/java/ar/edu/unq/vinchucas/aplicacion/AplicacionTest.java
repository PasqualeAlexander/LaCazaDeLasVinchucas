package ar.edu.unq.vinchucas.aplicacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.organizacion.Organizacion;
import ar.edu.unq.vinchucas.organizacion.TipoOrganizacion;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;

public class AplicacionTest {

    private SistemaDeUsuarios sistemaDeUsuarios;
    private IRepositorioDeMuestras repositorioDeMuestras;
    private ISistemaDeZonas sistemaDeZonas;
    private ISistemaDeOrganizaciones sistemaDeOrganizaciones;
    private Aplicacion aplicacion;

    @BeforeEach
    public void setUp() {
        sistemaDeUsuarios = mock(SistemaDeUsuarios.class);
        repositorioDeMuestras = mock(IRepositorioDeMuestras.class);
        sistemaDeZonas = mock(ISistemaDeZonas.class);
        sistemaDeOrganizaciones = mock(ISistemaDeOrganizaciones.class);
        aplicacion = new Aplicacion(sistemaDeUsuarios, repositorioDeMuestras, sistemaDeZonas, sistemaDeOrganizaciones);
    }

    @Test
    public void testRegistrarUsuarioValido() throws SistemaDeExcepciones {
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getNombreUsuario()).thenReturn("joaco");

        aplicacion.registrarUsuario(usuarioMock);

        verify(sistemaDeUsuarios).agregarUsuario(usuarioMock);
    }

    @Test
    public void testRegistrarUsuarioNuloLanzaExcepcion() {
    	SistemaDeExcepciones ex = assertThrows(SistemaDeExcepciones.class, () -> {
            aplicacion.registrarUsuario(null);
        });

        assertEquals("El usuario no puede ser nulo", ex.getMessage());
    }

    @Test
    public void testRegistrarUsuarioConNombreVacioLanzaExcepcion() {
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getNombreUsuario()).thenReturn("  "); // nombre en blacno

        SistemaDeExcepciones ex = assertThrows(SistemaDeExcepciones.class, () -> {
            aplicacion.registrarUsuario(usuarioMock);
        });

        assertEquals("El nombre es obligatorio", ex.getMessage());
    }
    
    @Test
    public void testRegistrarUsuarioConNombreDuplicadoLanzaExcepcion() throws SistemaDeExcepciones {
        Usuario usuarioMock = mock(Usuario.class);
        when(usuarioMock.getNombreUsuario()).thenReturn("juan123");

        // Simulamos que el sistema lanza la excepción cuando se intenta registrar
        doThrow(new SistemaDeExcepciones("El nombre ya está en uso"))
            .when(sistemaDeUsuarios).agregarUsuario(usuarioMock);

        SistemaDeExcepciones ex = assertThrows(SistemaDeExcepciones.class, () -> {
            aplicacion.registrarUsuario(usuarioMock);
        });

        assertEquals("El nombre ya está en uso", ex.getMessage());
    }
    
    @Test
    public void testRegistrarMuestraGuardaEnLaAplicacion() {
        Usuario usuarioMock = mock(Usuario.class);
        Muestra muestraMock = mock(Muestra.class);

        aplicacion.registrarMuestra(usuarioMock, muestraMock);

        verify(usuarioMock).enviarMuestra(muestraMock);
        verify(repositorioDeMuestras).agregarMuestra(muestraMock);
        verify(sistemaDeZonas).procesarNuevaMuestra(muestraMock);
    }

    @Test
    public void testRegistrarOrganizacion() {
        Organizacion organizacionMock = mock(Organizacion.class);

        aplicacion.registrarOrganizacion(organizacionMock);

        verify(sistemaDeOrganizaciones).registrarOrganizacion(organizacionMock);
    }

    @Test
    public void testSuscribirOrganizacionAZona() {
        Organizacion organizacionMock = mock(Organizacion.class);
        ZonaDeCobertura zonaMock = mock(ZonaDeCobertura.class);

        aplicacion.suscribirOrganizacionAZona(organizacionMock, zonaMock);

        verify(sistemaDeOrganizaciones).suscribirOrganizacionAZona(organizacionMock, zonaMock);
    }

    @Test
    public void testGetOrganizacionesPorTipo() {
        aplicacion.getOrganizacionesPorTipo(TipoOrganizacion.SALUD);

        verify(sistemaDeOrganizaciones).getOrganizacionesPorTipo(TipoOrganizacion.SALUD);
    }
}
