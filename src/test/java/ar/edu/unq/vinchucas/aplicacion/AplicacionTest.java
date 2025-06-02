package ar.edu.unq.vinchucas.aplicacion;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.unq.vinchucas.usuario.Usuario;

class AplicacionTest {
	private Aplicacion aplicacion;
	private Usuario usuario;

	@BeforeEach
	void setUp() {
		usuario = new Usuario("alex", "1234", null, null, null);
		aplicacion = new Aplicacion();
	}

	@Test
	void testNombreDisponibleYLuegoExcepcionPorDuplicado() throws ExcepcionesAplicacion {
		assertTrue(aplicacion.estaDisponibleElNombre("alex"));
		aplicacion.registrarUsuario(usuario);
		ExcepcionesAplicacion excepcion = assertThrows(
			ExcepcionesAplicacion.class,
			() -> aplicacion.estaDisponibleElNombre("alex")
		);
		assertEquals("Ya hay un usuario con ese nombre, utiliza otro nombre por favor", excepcion.getMessage());
	}
}
