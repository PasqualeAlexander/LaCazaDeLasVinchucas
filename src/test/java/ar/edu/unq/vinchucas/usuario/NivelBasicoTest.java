package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
	private Usuario usuario;

	@BeforeEach
	public void setUp() {
		muestraMock = mock(Muestra.class);
		muestrasMock = mock(RepositorioDeMuestras.class);
		opinionesMock = mock(RepositorioDeOpiniones.class);
		opinionMock = mock(Opinion.class);
		usuario = new Usuario("usuarioTest", "password", muestrasMock, opinionesMock);

	}

	@Test
	public void testPuedeVerificarDevuelveFalse() {
		NivelBasico nivel = new NivelBasico();
		assertFalse(nivel.puedeVerificar());
	}

	@Test
	public void testActualizarNivelCambiaANivelExpertoCuandoCumpleRequisitos() {
	    LocalDate fechaHoy = LocalDate.now();
	    
	    when(muestraMock.getFechaCreacion()).thenReturn(fechaHoy);
	    when(opinionMock.getFecha()).thenReturn(fechaHoy);

	    for (int i = 0; i < 20; i++) {
	        usuario.enviarMuestra(muestraMock);
	        usuario.opinar(muestraMock, opinionMock);
	    }
	    usuario.getNivel().actualizarNivel(usuario);
	    
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
