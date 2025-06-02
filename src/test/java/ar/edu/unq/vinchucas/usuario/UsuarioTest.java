package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.aplicacion.*;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioTest {

	private Usuario usuario;
	private Aplicacion aplicacion;
	private RepositorioDeMuestras muestras;
	private RepositorioDeOpiniones opiniones;

	@BeforeEach
	void setUp() {
		muestras = mock(RepositorioDeMuestras.class);
		opiniones = mock(RepositorioDeOpiniones.class);
		usuario = new Usuario("alex", "1234", muestras, opiniones, aplicacion);
	}

	@Test
	void testOpinarAgregaOpinionALaMuestra() {
		RepositorioDeMuestras repoMuestras = new RepositorioDeMuestras();
	    RepositorioDeOpiniones repoOpiniones = new RepositorioDeOpiniones();
	    Usuario usuario = new Usuario("testUser", "pass", repoMuestras, repoOpiniones, aplicacion);
	    Opinion opinion = new Opinion(usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
	    Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, opinion);
		usuario.opinar(muestra, opinion);
		assertEquals(1, usuario.getOpinionesEnviadas().size());
	}

	@Test
	void testEnviarMuestraAgregaMuestraAlRepositorio() {
		RepositorioDeMuestras repoMuestras = new RepositorioDeMuestras();
	    RepositorioDeOpiniones repoOpiniones = new RepositorioDeOpiniones();
	    Usuario usuario = new Usuario("testUser", "pass", repoMuestras, repoOpiniones, aplicacion);
	    Opinion opinion = new Opinion(usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
	    Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, opinion);
		usuario.enviarMuestra(muestra);
		assertEquals(1, usuario.getMuestrasEnviadas().size());
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
