package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SistemaDeUsuariosTest {

    private SistemaDeUsuarios sistema;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaDeUsuarios();
        usuario1 = mock(Usuario.class);
        usuario2 = mock(Usuario.class);
    }

    @Test
    public void testAgregarUsuarioExitosamente() {
        when(usuario1.getNombreUsuario()).thenReturn("alex");

        try {
            sistema.agregarUsuario(usuario1);
        } catch (SistemaDeExcepciones se) {
            fail("No debería lanzar excepción al agregar un usuario nuevo");
        }

        List<Usuario> registrados = sistema.getUsuariosRegistrados();
        assertEquals(1, registrados.size());
        assertTrue(registrados.contains(usuario1));
    }

    @Test
    public void testAgregarUsuarioConNombreDuplicadoLanzaExcepcion() {
        when(usuario1.getNombreUsuario()).thenReturn("alex");
        when(usuario2.getNombreUsuario()).thenReturn("alex");

        try {
            sistema.agregarUsuario(usuario1);
        } catch (SistemaDeExcepciones se) {
            fail("No debería lanzar excepción al agregar el primer usuario");
        }

        try {
            sistema.agregarUsuario(usuario2);
            fail("Debería lanzar excepción al agregar un usuario con nombre duplicado");
        } catch (SistemaDeExcepciones se) {
            assertEquals("El nombre ya está en uso", se.getMessage());
        }
    }

    @Test
    public void testListaInicialDeUsuariosEstaVacia() {
        assertTrue(sistema.getUsuariosRegistrados().isEmpty());
    }
}
