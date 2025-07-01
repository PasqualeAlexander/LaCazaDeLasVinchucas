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
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.filtros.Filtro;

import java.util.List;

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

    @Test
    public void testBuscarMuestrasConFiltro() {
        Filtro filtroMock = mock(Filtro.class);
        
        aplicacion.buscarMuestras(filtroMock);
        
        verify(repositorioDeMuestras).buscarMuestras(filtroMock);
    }

    @Test
    public void testBuscarMuestrasConListaDeFiltros() {
        List<Filtro> filtrosMock = List.of(mock(Filtro.class), mock(Filtro.class));
        
        aplicacion.buscarMuestras(filtrosMock);
        
        verify(repositorioDeMuestras).buscarMuestras(filtrosMock);
    }

    @Test
    public void testGetMuestrasVerificadas() {
        aplicacion.getMuestrasVerificadas();
        
        verify(repositorioDeMuestras).getMuestrasVerificadas();
    }

    @Test
    public void testGetMuestrasNoVerificadas() {
        aplicacion.getMuestrasNoVerificadas();
        
        verify(repositorioDeMuestras).getMuestrasNoVerificadas();
    }

    @Test
    public void testRegistrarZona() {
        ZonaDeCobertura zonaMock = mock(ZonaDeCobertura.class);
        
        aplicacion.registrarZona(zonaMock);
        
        verify(sistemaDeZonas).agregarZona(zonaMock);
    }

    @Test
    public void testGetZonas() {
        aplicacion.getZonas();
        
        verify(sistemaDeZonas).getZonas();
    }

    @Test
    public void testZonasQueCubrenMuestra() {
        Muestra muestraMock = mock(Muestra.class);
        
        aplicacion.zonasQueCubren(muestraMock);
        
        verify(sistemaDeZonas).zonasQueCubren(muestraMock);
    }

    @Test
    public void testZonasQueCubrenUbicacion() {
        Ubicacion ubicacionMock = mock(Ubicacion.class);
        
        aplicacion.zonasQueCubren(ubicacionMock);
        
        verify(sistemaDeZonas).zonasQueCubren(ubicacionMock);
    }


    @Test
    public void testEliminarOrganizacion() {
        Organizacion organizacionMock = mock(Organizacion.class);
        
        aplicacion.eliminarOrganizacion(organizacionMock);
        
        verify(sistemaDeOrganizaciones).eliminarOrganizacion(organizacionMock);
    }

    @Test
    public void testGetOrganizaciones() {
        aplicacion.getOrganizaciones();
        
        verify(sistemaDeOrganizaciones).getOrganizaciones();
    }

    @Test
    public void testGetOrganizacionesCercanas() {
        Ubicacion ubicacionMock = mock(Ubicacion.class);
        double radio = 5.0;
        
        aplicacion.getOrganizacionesCercanas(ubicacionMock, radio);
        
        verify(sistemaDeOrganizaciones).getOrganizacionesCercanas(ubicacionMock, radio);
    }

    @Test
    public void testDesuscribirOrganizacionDeZona() {
        Organizacion organizacionMock = mock(Organizacion.class);
        ZonaDeCobertura zonaMock = mock(ZonaDeCobertura.class);
        
        aplicacion.desuscribirOrganizacionDeZona(organizacionMock, zonaMock);
        
        verify(sistemaDeOrganizaciones).desuscribirOrganizacionDeZona(organizacionMock, zonaMock);
    }

    @Test
    public void testCantidadOrganizaciones() {
        aplicacion.cantidadOrganizaciones();
        
        verify(sistemaDeOrganizaciones).cantidadOrganizaciones();
    }

    @Test
    public void testCantidadOrganizacionesPorTipo() {
        aplicacion.cantidadOrganizacionesPorTipo(TipoOrganizacion.EDUCATIVA);
        
        verify(sistemaDeOrganizaciones).cantidadOrganizacionesPorTipo(TipoOrganizacion.EDUCATIVA);
    }

    @Test
    public void testGetSistemaDeOrganizaciones() {
        ISistemaDeOrganizaciones resultado = aplicacion.getSistemaDeOrganizaciones();
        
        assertEquals(sistemaDeOrganizaciones, resultado);
    }

    @Test
    public void testGetSistemaDeUsuarios() {
        SistemaDeUsuarios resultado = aplicacion.getSistemaDeUsuarios();
        
        assertEquals(sistemaDeUsuarios, resultado);
    }

    @Test
    public void testGetRepositorioDeMuestras() {
        IRepositorioDeMuestras resultado = aplicacion.getRepositorioDeMuestras();
        
        assertEquals(repositorioDeMuestras, resultado);
    }

    @Test
    public void testGetSistemaDeZonas() {
        ISistemaDeZonas resultado = aplicacion.getSistemaDeZonas();
        
        assertEquals(sistemaDeZonas, resultado);
    }
}
