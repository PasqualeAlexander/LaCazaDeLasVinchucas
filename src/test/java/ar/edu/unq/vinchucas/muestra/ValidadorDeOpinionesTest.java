package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValidadorDeOpinionesTest {

    private ValidadorDeOpiniones validador;
    private Usuario usuario;
    private List<Opinion> opiniones;

    @BeforeEach
    public void setUp() {
        validador = new ValidadorDeOpiniones();
        usuario = mock(Usuario.class);
        opiniones = new ArrayList<>();
    }

    @Test
    public void testUsuarioPuedeOpinarSinOpinionesPrevias() {
        try {
            validador.validarQueUsuarioPuedeOpinar(usuario, opiniones, false);
        } catch (SistemaDeExcepciones e) {
            fail("No deberia lanzar una excepcion cuando el usuario no opinó");
        }
    }


    @Test
    public void testUsuarioYaOpinoYLanzaExcepcion() {
        Opinion opinion = mock(Opinion.class);
        when(opinion.getUsuario()).thenReturn(usuario);
        opiniones.add(opinion);

        SistemaDeExcepciones se = assertThrows(SistemaDeExcepciones.class, () ->
            validador.validarQueUsuarioPuedeOpinar(usuario, opiniones, false)
        );
        assertEquals("El usuario ya ha opinado sobre esta muestra", se.getMessage());
    }

    @Test
    public void testMuestraYaVerificadaYLanzaExcepcion() {
        SistemaDeExcepciones se = assertThrows(SistemaDeExcepciones.class, () ->
            validador.validarQueUsuarioPuedeOpinar(usuario, opiniones, true)
        );
        assertEquals("La muestra ya está verificada", se.getMessage());
    }
}
