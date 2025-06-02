package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioTest {

	private Usuario usuario;
	private RepositorioDeMuestras muestras;
	private RepositorioDeOpiniones opiniones;
	private Muestra muestraMock;
	private Opinion opinionMock;

	@BeforeEach
	void setUp() {
		muestras = mock(RepositorioDeMuestras.class);
		opiniones = mock(RepositorioDeOpiniones.class);
		muestraMock = mock(Muestra.class);
		opinionMock = mock(Opinion.class);
		usuario = new Usuario("alex", "1234", muestras, opiniones);
	}

	@Test
	void testOpinarAgregaOpinionALaMuestra() {
		usuario.opinar(muestraMock, opinionMock);
		verify(muestraMock).agregarOpinion(opinionMock);
	}

	@Test
	void testEnviarMuestraAgregaMuestraAlRepositorio() {
		usuario.enviarMuestra(muestraMock);
		verify(muestras).agregarMuestra(muestraMock);
	}

	@Test
	void testGetNombreUsuarioDevuelveNombreCorrecto() {
		assertEquals("alex", usuario.getNombreUsuario());
	}

	@Test
	void testSetYGetContraseña() {
		usuario.setContraseña("nuevaClave");
		assertEquals("nuevaClave", usuario.getContraseña());
	}

	@Test
	void testRepositorioEsElEsperado() {
		assertEquals(muestras, usuario.getRepositorio());
	}
	
/* 
	@Test
	void testAgregarOpinionALaListaDeUsuario() { // NO va a correr porque no está hecho el admiteOpiniones()
		Opinion opinion = mock(Opinion.class);
		usuario.opinar(muestraMock, opinion);
		usuario.getOpinionesEnviadas();
		assertTrue(usuario.getOpinionesEnviadas().contains(opinion));
	}

	@Test
	void testAgregarMuestraALaListaDeUsuario() { // NO va a correr porque no se agrega la muestra
		usuario.enviarMuestra(muestraMock);
		usuario.getMuestrasEnviadas();
		assertTrue(usuario.getMuestrasEnviadas().contains(muestraMock));
	}
*/
	@Test
	void testNivelInvestigadorPuedeVerificar() { 
		NivelDeUsuario investigador = new NivelInvestigador();
		assertTrue(investigador.puedeVerificar());
	}

	@Test
	void testNivelBasicoNoPuedeVerificar() {
		NivelDeUsuario basico = new NivelBasico();
		assertFalse(basico.puedeVerificar());
	}

	@Test
	void testNivelExpertoPuedeVerificar() {
		NivelDeUsuario experto = new NivelExperto();
		assertTrue(experto.puedeVerificar());
	}
}
