package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NivelExpertoTest {

    private NivelExperto nivelExperto;

    @BeforeEach
    public void setUp() {
        nivelExperto = new NivelExperto();
    }

    @Test
    public void testPuedeVerificarDevuelveTrue() {
        assertTrue(nivelExperto.puedeVerificar());
    }

    @Test
    public void testNombreNivelEsCorrecto() {
        assertEquals("Nivel Experto", nivelExperto.getNombreNivel());
    }
}
