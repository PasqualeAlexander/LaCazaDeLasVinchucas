package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SistemaDeOpinionesTest {

    private SistemaDeOpiniones sistema;
    private Opinion opinionMock;
    private Usuario usuarioMock;
    private Usuario expertoMock;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaDeOpiniones();

        opinionMock = mock(Opinion.class);
        usuarioMock = mock(Usuario.class);
        expertoMock = mock(Usuario.class);

        when(usuarioMock.getNombreUsuario()).thenReturn("alex");
        when(usuarioMock.esNivelExperto()).thenReturn(false);

        when(expertoMock.getNombreUsuario()).thenReturn("experto");
        when(expertoMock.esNivelExperto()).thenReturn(true);
    }

    @Test
    public void testAgregarOpinionSeRegistra() throws SistemaDeExcepciones {
        Opinion opinion = new Opinion(usuarioMock, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        sistema.agregarOpinion(opinion);

        assertEquals(1, sistema.getOpiniones().size());
        assertTrue(sistema.getOpiniones().contains(opinion));
    }

    @Test
    public void testAgregarOpinionDuplicadaLanzaExcepcion() throws SistemaDeExcepciones {
        Opinion opinion1 = new Opinion(usuarioMock, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(usuarioMock, TipoDeOpinion.CHINCHE_FOLIADA);
        
        // Primera opinión se agrega correctamente
        sistema.agregarOpinion(opinion1);
        
        // Segunda opinión del mismo usuario debe lanzar excepción
        assertThrows(SistemaDeExcepciones.class, () -> {
            sistema.agregarOpinion(opinion2);
        });
        
        // Solo debe haber una opinión
        assertEquals(1, sistema.getOpiniones().size());
    }

    @Test
    public void testAgregarOpinionNulaLanzaExcepcion() {
        assertThrows(SistemaDeExcepciones.class, () -> {
            sistema.agregarOpinion(null);
        });
    }

    @Test
    public void testAgregarOpinionConUsuarioNuloLanzaExcepcion() {
        Opinion opinionConUsuarioNulo = mock(Opinion.class);
        when(opinionConUsuarioNulo.getUsuario()).thenReturn(null);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            sistema.agregarOpinion(opinionConUsuarioNulo);
        });
    }

    @Test
    public void testGetFechaUltimaOpinionDevuelveFechaCorrecta() throws SistemaDeExcepciones {
        LocalDate fecha = LocalDate.of(2025, 6, 6);
        
        // Mock de la fecha
        Opinion opinionConFecha = mock(Opinion.class);
        when(opinionConFecha.getUsuario()).thenReturn(usuarioMock);
        when(opinionConFecha.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(opinionConFecha.getFecha()).thenReturn(fecha);
        
        sistema.agregarOpinion(opinionConFecha);

        assertEquals(fecha, sistema.getFechaUltimaOpinion());
    }

    @Test
    public void testGetFechaUltimaOpinionRetornaNullSiNoHayOpiniones() {
        assertNull(sistema.getFechaUltimaOpinion());
    }

    @Test
    public void testMultiplesOpinionesDeDiferentesUsuarios() throws SistemaDeExcepciones {
        Opinion opinion1 = new Opinion(usuarioMock, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(expertoMock, TipoDeOpinion.CHINCHE_FOLIADA);
        
        sistema.agregarOpinion(opinion1);
        sistema.agregarOpinion(opinion2);

        assertEquals(2, sistema.getOpiniones().size());
        assertTrue(sistema.getOpiniones().contains(opinion1));
        assertTrue(sistema.getOpiniones().contains(opinion2));
    }
}
