package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class MuestraTest {

	private Muestra muestra;
	private Opinion opinion;
	private Usuario usuario;

	@BeforeEach
    public void setUp() {
		usuario = Mockito.mock(Usuario.class);
    	opinion = new Opinion(usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestra = new Muestra("foto.jpg", "ubicacion", usuario, opinion );
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

		TipoDeOpinion resultado = muestra.resultadoActual();

		assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, resultado);
	}

	@Test
	public void testResultadoActualSinOpinionesRetonaLaOpinionDeQuienEnviaLaMuestra() {
		assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra.resultadoActual());
	}
}
