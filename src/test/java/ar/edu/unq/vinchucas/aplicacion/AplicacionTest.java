package ar.edu.unq.vinchucas.aplicacion;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class AplicacionTest {

    private SistemaDeUsuarios sistemaDeUsuarios;
    private IRepositorioDeMuestras repositorioDeMuestras;
    private ISistemaDeZonas sistemaDeZonas;
    private Aplicacion aplicacion;

    @BeforeEach
    public void setUp() {
        sistemaDeUsuarios = mock(SistemaDeUsuarios.class);
        repositorioDeMuestras = mock(IRepositorioDeMuestras.class);
        sistemaDeZonas = mock(ISistemaDeZonas.class);
        aplicacion = new Aplicacion(sistemaDeUsuarios, repositorioDeMuestras, sistemaDeZonas);
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
}
