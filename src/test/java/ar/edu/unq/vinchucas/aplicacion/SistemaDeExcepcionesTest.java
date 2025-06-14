package ar.edu.unq.vinchucas.aplicacion;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SistemaDeExcepcionesTest {

    @Test
    public void testCreacionConMensaje() {
        String mensaje = "Error de prueba";
        SistemaDeExcepciones excepcion = new SistemaDeExcepciones(mensaje);
        
        assertEquals(mensaje, excepcion.getMessage());
    }

    @Test
    public void testCreacionConMensajeVacio() {
        String mensaje = "";
        SistemaDeExcepciones excepcion = new SistemaDeExcepciones(mensaje);
        
        assertEquals(mensaje, excepcion.getMessage());
    }

    @Test
    public void testCreacionConMensajeNull() {
        SistemaDeExcepciones excepcion = new SistemaDeExcepciones(null);
        
        assertNull(excepcion.getMessage());
    }

    @Test
    public void testEsSubclaseDeException() {
        SistemaDeExcepciones excepcion = new SistemaDeExcepciones("test");
        
        assertTrue(excepcion instanceof Exception);
    }

    @Test
    public void testPuedeLanzarseYCapturarse() {
        String mensajeEsperado = "Excepción de prueba";
        
        SistemaDeExcepciones excepcionCapturada = assertThrows(SistemaDeExcepciones.class, () -> {
            throw new SistemaDeExcepciones(mensajeEsperado);
        });
        
        assertEquals(mensajeEsperado, excepcionCapturada.getMessage());
    }
} 