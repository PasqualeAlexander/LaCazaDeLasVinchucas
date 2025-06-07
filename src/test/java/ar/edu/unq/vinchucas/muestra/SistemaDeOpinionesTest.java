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
        sistema = new SistemaDeOpiniones(TipoDeOpinion.VINCHUCA_INFESTANS);

        opinionMock = mock(Opinion.class);
        usuarioMock = mock(Usuario.class);
        expertoMock = mock(Usuario.class);

        when(usuarioMock.getNombreUsuario()).thenReturn("alex");
        when(usuarioMock.esNivelExperto()).thenReturn(false);

        when(expertoMock.esNivelExperto()).thenReturn(true);
    }

    @Test
    public void testAgregarOpinionValidaSeRegistra() throws SistemaDeExcepciones {
        when(opinionMock.getUsuario()).thenReturn(usuarioMock);
        when(opinionMock.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(opinionMock.eraExpertoAlOpinar()).thenReturn(false);

        sistema.agregarOpinion(opinionMock);

        assertEquals(1, sistema.getOpiniones().size());
        assertTrue(sistema.getHistorial().contains("alex"));
    }

    @Test
    public void testNoSePuedeAgregarOpinionSiUsuarioYaOpino() throws SistemaDeExcepciones {
        when(opinionMock.getUsuario()).thenReturn(usuarioMock);
        when(opinionMock.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(opinionMock.eraExpertoAlOpinar()).thenReturn(false);

        sistema.agregarOpinion(opinionMock);

        Opinion segundaOpinion = mock(Opinion.class);
        when(segundaOpinion.getUsuario()).thenReturn(usuarioMock);
        when(segundaOpinion.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(segundaOpinion.eraExpertoAlOpinar()).thenReturn(false);

        assertThrows(SistemaDeExcepciones.class, () -> sistema.agregarOpinion(segundaOpinion));
    }

    @Test
    public void testSiOpinaUnExpertoSeReinicianVotos() throws SistemaDeExcepciones {
        Opinion opinion1 = mock(Opinion.class);
        when(opinion1.getUsuario()).thenReturn(usuarioMock);
        when(opinion1.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(opinion1.eraExpertoAlOpinar()).thenReturn(false);

        sistema.agregarOpinion(opinion1);

        Opinion opinionExperta = mock(Opinion.class);
        when(opinionExperta.getUsuario()).thenReturn(expertoMock);
        when(opinionExperta.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_SORDIDA);
        when(opinionExperta.eraExpertoAlOpinar()).thenReturn(true);

        sistema.agregarOpinion(opinionExperta);

        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, sistema.getResultado());
    }

    @Test
    public void testCorrespondeVerificarConDosExpertosQueCoinciden() throws SistemaDeExcepciones {
        Opinion experto1 = mock(Opinion.class);
        when(experto1.getUsuario()).thenReturn(expertoMock);
        when(experto1.getTipoDeOpinion()).thenReturn(TipoDeOpinion.IMAGEN_POCO_CLARA);
        when(experto1.eraExpertoAlOpinar()).thenReturn(true);

        Usuario experto2 = mock(Usuario.class);
        when(experto2.getNombreUsuario()).thenReturn("otroExperto");
        when(experto2.esNivelExperto()).thenReturn(true);

        Opinion opinion2 = mock(Opinion.class);
        when(opinion2.getUsuario()).thenReturn(experto2);
        when(opinion2.getTipoDeOpinion()).thenReturn(TipoDeOpinion.IMAGEN_POCO_CLARA);
        when(opinion2.eraExpertoAlOpinar()).thenReturn(true);

        sistema.agregarOpinion(experto1);
        sistema.agregarOpinion(opinion2);

        assertTrue(sistema.correspondeVerificar());
    }

    @Test
    public void testGetFechaUltimaOpinionDevuelveFechaCorrecta() throws SistemaDeExcepciones {
        LocalDate fecha = LocalDate.of(2025, 6, 6);
        when(opinionMock.getUsuario()).thenReturn(usuarioMock);
        when(opinionMock.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(opinionMock.getFecha()).thenReturn(fecha);
        when(opinionMock.eraExpertoAlOpinar()).thenReturn(false);

        sistema.agregarOpinion(opinionMock);

        assertEquals(fecha, sistema.getFechaUltimaOpinion());
    }

    @Test
    public void testAdmiteOpinionDeUsuarioCuandoNoHaOpinadoNiEstaVerificada() {
        assertTrue(sistema.admiteOpinionDeUsuario(usuarioMock));
    }

    @Test
    public void testAdmiteOpinionDeUsuarioRetornaFalseSiYaOpino() throws SistemaDeExcepciones {
        when(opinionMock.getUsuario()).thenReturn(usuarioMock);
        when(opinionMock.getTipoDeOpinion()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
        when(opinionMock.eraExpertoAlOpinar()).thenReturn(false);

        sistema.agregarOpinion(opinionMock);

        assertFalse(sistema.admiteOpinionDeUsuario(usuarioMock));
    }
}
