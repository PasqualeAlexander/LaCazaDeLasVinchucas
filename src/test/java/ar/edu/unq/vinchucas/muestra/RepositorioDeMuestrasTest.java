package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.aplicacion.IRepositorioDeMuestras;
import ar.edu.unq.vinchucas.filtros.FiltroPorTipoInsecto;
import ar.edu.unq.vinchucas.filtros.FiltroPorNivelVerificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RepositorioDeMuestrasTest {

	private IRepositorioDeMuestras repositorio;
	private Muestra muestra1;
	private Muestra muestra2;
	private Muestra muestraVerificada;

	@BeforeEach
	public void setUp() {
		repositorio = new RepositorioDeMuestras();
		muestra1 = mock(Muestra.class);
		muestra2 = mock(Muestra.class);
		muestraVerificada = mock(Muestra.class);
		
		// Configuramos una muestra como verificada
		when(muestraVerificada.estaVerificada()).thenReturn(true);
		when(muestra1.estaVerificada()).thenReturn(false);
		when(muestra2.estaVerificada()).thenReturn(false);
	}

	@Test
	public void testAgregarMuestraUnaVez() {
		repositorio.agregarMuestra(muestra1);
		assertEquals(1, repositorio.getMuestras().size());
		assertTrue(repositorio.getMuestras().contains(muestra1));
	}

	@Test
	public void testAgregarMultiplesMuestras() {
		repositorio.agregarMuestra(muestra1);
		repositorio.agregarMuestra(muestra2);
		assertEquals(2, repositorio.getMuestras().size());
		assertTrue(repositorio.getMuestras().contains(muestra1));
		assertTrue(repositorio.getMuestras().contains(muestra2));
	}



	@Test
	public void testGetMuestrasVerificadas() {
		repositorio.agregarMuestra(muestra1);
		repositorio.agregarMuestra(muestraVerificada);
		repositorio.agregarMuestra(muestra2);

		var verificadas = repositorio.getMuestrasVerificadas();
		assertEquals(1, verificadas.size());
		assertTrue(verificadas.contains(muestraVerificada));
	}

	@Test
	public void testGetMuestrasNoVerificadas() {
		repositorio.agregarMuestra(muestra1);
		repositorio.agregarMuestra(muestraVerificada);
		repositorio.agregarMuestra(muestra2);

		var noVerificadas = repositorio.getMuestrasNoVerificadas();
		assertEquals(2, noVerificadas.size());
		assertTrue(noVerificadas.contains(muestra1));
		assertTrue(noVerificadas.contains(muestra2));
	}

	@Test
	public void testCantidadMuestras() {
		assertEquals(0, repositorio.cantidadMuestras());
		
		repositorio.agregarMuestra(muestra1);
		assertEquals(1, repositorio.cantidadMuestras());
		
		repositorio.agregarMuestra(muestra2);
		assertEquals(2, repositorio.cantidadMuestras());
	}

	@Test
	public void testCantidadMuestrasVerificadas() {
		assertEquals(0, repositorio.cantidadMuestrasVerificadas());
		
		repositorio.agregarMuestra(muestra1);
		assertEquals(0, repositorio.cantidadMuestrasVerificadas());
		
		repositorio.agregarMuestra(muestraVerificada);
		assertEquals(1, repositorio.cantidadMuestrasVerificadas());
	}

	@Test
	public void testBuscarMuestrasConFiltro() {
		// Configuramos tipos para las muestras
		when(muestra1.getResultado()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
		when(muestra2.getResultado()).thenReturn(TipoDeOpinion.CHINCHE_FOLIADA);
		
		repositorio.agregarMuestra(muestra1);
		repositorio.agregarMuestra(muestra2);

		var filtro = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS);
		var resultado = repositorio.buscarMuestras(filtro);
		
		assertEquals(1, resultado.size());
		assertTrue(resultado.contains(muestra1));
	}

	@Test
	public void testBuscarMuestrasConMultiplesFiltros() {
		when(muestra1.getResultado()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
		when(muestra1.estaVerificada()).thenReturn(true);
		when(muestra2.getResultado()).thenReturn(TipoDeOpinion.VINCHUCA_INFESTANS);
		when(muestra2.estaVerificada()).thenReturn(false);
		
		repositorio.agregarMuestra(muestra1);
		repositorio.agregarMuestra(muestra2);

		var filtros = List.of(
			new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS),
			new FiltroPorNivelVerificacion(true)
		);
		var resultado = repositorio.buscarMuestras(filtros);
		
		assertEquals(1, resultado.size());
		assertTrue(resultado.contains(muestra1));
	}

	@Test
	public void testBuscarMuestrasConListaVacia() {
		var resultado = repositorio.buscarMuestras(new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS));
		
		assertTrue(resultado.isEmpty());
	}

	@Test
	public void testRepositorioVacio() {
		assertEquals(0, repositorio.cantidadMuestras());
		assertEquals(0, repositorio.cantidadMuestrasVerificadas());
		assertTrue(repositorio.getMuestras().isEmpty());
		assertTrue(repositorio.getMuestrasVerificadas().isEmpty());
		assertTrue(repositorio.getMuestrasNoVerificadas().isEmpty());
	}
} 