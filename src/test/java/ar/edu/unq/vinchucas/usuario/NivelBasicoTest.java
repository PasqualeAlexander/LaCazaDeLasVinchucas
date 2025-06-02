package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;
import ar.edu.unq.vinchucas.aplicacion.*;
import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;

public class NivelBasicoTest {
	private Muestra muestraMock;
	private Opinion opinionMock;
	private RepositorioDeMuestras muestrasMock;
	private RepositorioDeOpiniones opinionesMock;
	private Aplicacion aplicacion;
	private Usuario usuario;

	@BeforeEach
	public void setUp() {
		muestraMock = mock(Muestra.class);
		muestrasMock = mock(RepositorioDeMuestras.class);
		opinionesMock = mock(RepositorioDeOpiniones.class);
		opinionMock = mock(Opinion.class);
		usuario = new Usuario("usuarioTest", "password", muestrasMock, opinionesMock, aplicacion);

	}

	@Test
	public void testPuedeVerificarDevuelveFalse() {
		NivelBasico nivel = new NivelBasico();
		assertFalse(nivel.puedeVerificar());
	}

	@Test
	public void testActualizarNivelCambiaANivelExpertoCuandoCumpleRequisitos() {
		RepositorioDeMuestras repoMuestras = new RepositorioDeMuestras();
		RepositorioDeOpiniones repoOpiniones = new RepositorioDeOpiniones();
		TipoDeOpinion tipoDeOpinion = TipoDeOpinion.VINCHUCA_INFESTANS;
		Opinion opinion = new Opinion(usuario, tipoDeOpinion);
		Usuario usuario = new Usuario("testUser", "pass", repoMuestras, repoOpiniones, aplicacion);
		Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, opinion);

		for (int i = 0; i < 20; i++) {
			usuario.enviarMuestra(muestra);
			usuario.opinar(muestra, opinion);
		}

		assertEquals(20, usuario.getMuestrasEnviadas().size());
		assertEquals(20, usuario.getOpinionesEnviadas().size());
		assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
	}

	@Test
	public void testActualizarNivelPermaneceNivelBasicoCuandoNoCumpleRequisitos() {
		for (int i = 0; i < 10; i++) {
			usuario.enviarMuestra(muestraMock);
			usuario.opinar(muestraMock, opinionMock);
		}

		assertEquals("Nivel Basico", usuario.getNivel().getNombreNivel());
	}
}
