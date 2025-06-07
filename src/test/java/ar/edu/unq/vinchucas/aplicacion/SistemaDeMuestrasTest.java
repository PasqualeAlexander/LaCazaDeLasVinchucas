package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SistemaDeMuestrasTest {

	private SistemaDeMuestras sistema;
	private Muestra muestra1;
	private Muestra muestra2;

	@BeforeEach
	public void setUp() {
		sistema = new SistemaDeMuestras();
		muestra1 = mock(Muestra.class);
		muestra2 = mock(Muestra.class);
	}

	@Test
	public void testRegistrarMuestraUnaVez() {
		sistema.registrarMuestra(muestra1);
		assertEquals(1, sistema.getMuestras().size());
		assertTrue(sistema.getMuestras().contains(muestra1));
	}

	@Test
	public void testRegistrarMultiplesMuestras() {
		sistema.registrarMuestra(muestra1);
		sistema.registrarMuestra(muestra2);
		assertEquals(2, sistema.getMuestras().size());
		assertTrue(sistema.getMuestras().contains(muestra1));
		assertTrue(sistema.getMuestras().contains(muestra2));
	}
}
