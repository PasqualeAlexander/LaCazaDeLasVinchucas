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
    public void testAgregarOpinionInicialSeRegistra() {
        Opinion opinionInicial = new Opinion(usuarioMock, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        sistema.agregarOpinionInicial(opinionInicial);

        assertEquals(1, sistema.getOpiniones().size());
        assertTrue(sistema.getOpiniones().contains(opinionInicial));
    }

    @Test
    public void testAgregarOpinionSeRegistra() {
        Opinion opinion = new Opinion(usuarioMock, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        sistema.agregarOpinion(opinion);

        assertEquals(1, sistema.getOpiniones().size());
        assertTrue(sistema.getOpiniones().contains(opinion));
    }

    @Test
    public void testYaOpinoElUsuarioRetornaTrueSiYaOpino() {
        Opinion opinion = new Opinion(usuarioMock, TipoDeOpinion.VINCHUCA_INFESTANS);
        sistema.agregarOpinion(opinion);

        assertTrue(sistema.yaOpinoElUsuario(usuarioMock));
    }

    @Test
    public void testYaOpinoElUsuarioRetornaFalseSiNoOpino() {
        assertFalse(sistema.yaOpinoElUsuario(usuarioMock));
    }

    @Test
    public void testGetFechaUltimaOpinionDevuelveFechaCorrecta() {
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

}
