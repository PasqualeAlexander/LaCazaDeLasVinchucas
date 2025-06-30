package ar.edu.unq.vinchucas.muestra.estado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class EstadoVerificadaTest {

    private EstadoVerificada estadoVerificada;

    @BeforeEach
    public void setUp() {
        estadoVerificada = new EstadoVerificada(TipoDeOpinion.VINCHUCA_INFESTANS);
    }

    @Test
    public void testEstadoVerificadaMantieneResultado() {
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, estadoVerificada.getResultado());
    }

    @Test
    public void testEstadoVerificadaEsVerificada() {
        assertTrue(estadoVerificada.esVerificada());
    }

    @Test
    public void testNoSePuedeAgregarOpinionLanzaExcepcion() {
        Opinion opinionMock = mock(Opinion.class);
        Muestra muestraMock = mock(Muestra.class);

        SistemaDeExcepciones thrown = assertThrows(
            SistemaDeExcepciones.class,
            () -> estadoVerificada.agregarOpinion(muestraMock, opinionMock)
        );

        assertEquals("No se pueden agregar opiniones a una muestra verificada", thrown.getMessage());
    }

    @Test
    public void testNingunUsuarioPuedeOpinarEnEstadoVerificada() {
        Usuario usuarioMock = mock(Usuario.class);
        Muestra muestraMock = mock(Muestra.class);
        
        assertFalse(estadoVerificada.puedeOpinarUsuario(usuarioMock, muestraMock));
    }
}
