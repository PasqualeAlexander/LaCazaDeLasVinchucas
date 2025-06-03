package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class MuestraTest {

    private Muestra muestra;
    private Usuario usuarioBasico;
    private Usuario usuarioExperto1;
    private Usuario usuarioExperto2;
    private Opinion opinionExperta1;
    private Opinion opinionExperta2;

    @BeforeEach
    void setUp() {
        usuarioBasico = mock(Usuario.class);
        usuarioExperto1 = mock(Usuario.class);
        usuarioExperto2 = mock(Usuario.class);
        
        when(usuarioBasico.esNivelBasico()).thenReturn(true);
        when(usuarioBasico.esNivelExperto()).thenReturn(false);
        when(usuarioExperto1.esNivelBasico()).thenReturn(false);
        when(usuarioExperto1.esNivelExperto()).thenReturn(true);
        when(usuarioExperto2.esNivelBasico()).thenReturn(false);
        when(usuarioExperto2.esNivelExperto()).thenReturn(true);
        
        opinionExperta1 = new Opinion(usuarioExperto1, TipoDeOpinion.VINCHUCA_SORDIDA);
        opinionExperta2 = new Opinion(usuarioExperto2, TipoDeOpinion.VINCHUCA_SORDIDA);
        
        muestra = new Muestra("foto.jpg", "Buenos Aires", usuarioBasico, 
                             new Opinion(usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS));
    }

	@Test
	public void testResultadoActualEsElEsperado() {
		Usuario user1 = Mockito.mock(Usuario.class);
		Usuario user2 = Mockito.mock(Usuario.class);
		Usuario user3 = Mockito.mock(Usuario.class);
		Usuario user4 = Mockito.mock(Usuario.class);
		Usuario user5 = Mockito.mock(Usuario.class);
		Usuario user6 = Mockito.mock(Usuario.class);
		
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
	public void testResultadoActualSinOpinionesRetonaLaOpinionDeQuienEnviaLaMuestra() {
		assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra.getResultado());
	}
	
    @Test
    void testVotoExpertoInvalidaVotosBasicos() {
        Opinion opinionBasica = new Opinion(usuarioBasico, TipoDeOpinion.IMAGEN_POCO_CLARA);
        
        muestra.agregarOpinion(opinionBasica);
        muestra.agregarOpinion(opinionExperta1);
        
        // El voto de un uSUARIO basico no debería contar después del voto experto.
        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestra.getResultado());
    }

    @Test
    void testMuestraSeVerificaConDosExpertos() {
        muestra.agregarOpinion(opinionExperta1);
        assertFalse(muestra.estaVerificada());
        
        muestra.agregarOpinion(opinionExperta2);
        assertTrue(muestra.estaVerificada());
    }

    @Test
    void testNoSePuedeOpinarSobreMuestraVerificada() {
        muestra.agregarOpinion(opinionExperta1);
        muestra.agregarOpinion(opinionExperta2);
        
        Opinion nuevaOpinion = new Opinion(usuarioExperto1, TipoDeOpinion.CHINCHE_FOLIADA);
        muestra.agregarOpinion(nuevaOpinion);
        
        // El resultado notendria que cambiar
        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestra.getResultado());
    }
}

