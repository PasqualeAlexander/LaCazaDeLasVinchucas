package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class MuestraTest {

    private Muestra muestra;
    private Usuario usuarioBasico;
    private Usuario usuarioExperto1;
    private Usuario usuarioExperto2;
    private Opinion opinionExperta1;
    private Opinion opinionExperta2;

    @BeforeEach
    void setUp() throws SistemaDeExcepciones {
        usuarioBasico = mock(Usuario.class);
        usuarioExperto1 = mock(Usuario.class);
        usuarioExperto2 = mock(Usuario.class);

        when(usuarioBasico.esNivelBasico()).thenReturn(true);
        when(usuarioBasico.esNivelExperto()).thenReturn(false);

        when(usuarioExperto1.esNivelBasico()).thenReturn(false);
        when(usuarioExperto1.esNivelExperto()).thenReturn(true);

        when(usuarioExperto2.esNivelBasico()).thenReturn(false);
        when(usuarioExperto2.esNivelExperto()).thenReturn(true);

        muestra = new Muestra("foto.jpg", "Buenos Aires", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);

        opinionExperta1 = new Opinion(usuarioExperto1, TipoDeOpinion.VINCHUCA_SORDIDA);
        opinionExperta2 = new Opinion(usuarioExperto2, TipoDeOpinion.VINCHUCA_SORDIDA);
    }

    @Test
    public void testResultadoActualEsElEsperado() throws SistemaDeExcepciones {
        Usuario user1 = mock(Usuario.class);
        Usuario user2 = mock(Usuario.class);
        Usuario user3 = mock(Usuario.class);
        Usuario user4 = mock(Usuario.class);
        Usuario user5 = mock(Usuario.class);
        Usuario user6 = mock(Usuario.class);

        muestra.agregarOpinion(new Opinion(user1, TipoDeOpinion.VINCHUCA_INFESTANS));
        muestra.agregarOpinion(new Opinion(user2, TipoDeOpinion.VINCHUCA_INFESTANS));
        muestra.agregarOpinion(new Opinion(user3, TipoDeOpinion.NINGUNA));
        muestra.agregarOpinion(new Opinion(user4, TipoDeOpinion.CHINCHE_FOLIADA));
        muestra.agregarOpinion(new Opinion(user5, TipoDeOpinion.VINCHUCA_INFESTANS));
        muestra.agregarOpinion(new Opinion(user6, TipoDeOpinion.CHINCHE_FOLIADA));

        TipoDeOpinion resultado = muestra.getResultado();

        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, resultado);
    }

    @Test
    public void testResultadoInicialEsVotoDelCreador() {
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra.getResultado());
    }

    @Test
    void testVotoExpertoInvalidaVotosBasicos() throws SistemaDeExcepciones {
    	Usuario otroBasico = mock(Usuario.class);
        when(otroBasico.esNivelBasico()).thenReturn(true);

        Opinion opinionBasica = new Opinion(otroBasico, TipoDeOpinion.IMAGEN_POCO_CLARA);
        
        muestra.agregarOpinion(opinionBasica);
        muestra.agregarOpinion(opinionExperta1);

        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestra.getResultado());
    }

    @Test
    void testMuestraSeVerificaConDosExpertos() throws SistemaDeExcepciones {
        muestra.agregarOpinion(opinionExperta1);
        assertFalse(muestra.estaVerificada());

        muestra.agregarOpinion(opinionExperta2);
        assertTrue(muestra.estaVerificada());
    }

    @Test
    void testNoSePuedeOpinarSobreMuestraVerificada() throws SistemaDeExcepciones {
        muestra.agregarOpinion(opinionExperta1);
        muestra.agregarOpinion(opinionExperta2);

        Opinion nuevaOpinion = new Opinion(usuarioExperto1, TipoDeOpinion.CHINCHE_FOLIADA);

        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            muestra.agregarOpinion(nuevaOpinion);
        });
        assertEquals("El usuario no puede opinar sobre esta muestra.", thrown.getMessage());

        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestra.getResultado());
    }

    @Test
    void testCreadorNoPuedeOpinarPeroSuVotoCuenta() throws SistemaDeExcepciones {
        Opinion opinionCreador = new Opinion(usuarioBasico, TipoDeOpinion.CHINCHE_FOLIADA);

        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            muestra.agregarOpinion(opinionCreador);
        });
        assertEquals("El usuario no puede opinar sobre esta muestra.", thrown.getMessage());

        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra.getResultado());
    }

    @Test
    void testOpinionesSeGuardanCorrectamente() throws SistemaDeExcepciones {
        Opinion opinion1 = new Opinion(usuarioExperto1, TipoDeOpinion.CHINCHE_FOLIADA);
        Opinion opinion2 = new Opinion(usuarioExperto2, TipoDeOpinion.NINGUNA);

        muestra.agregarOpinion(opinion1);
        muestra.agregarOpinion(opinion2);

        List<Opinion> opiniones = muestra.getOpiniones();
        assertTrue(opiniones.contains(opinion1));
        assertTrue(opiniones.contains(opinion2));
        assertEquals(2, opiniones.size());
    }
}

