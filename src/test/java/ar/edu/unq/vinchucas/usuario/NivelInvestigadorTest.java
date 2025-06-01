package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NivelInvestigadorTest {

	@Test
	public void testPuedeVerificarDebeSerTrue() {
		NivelInvestigador nivel = new NivelInvestigador();
		assertTrue(nivel.puedeVerificar(), "NivelInvestigador debe poder verificar");
	}

	@Test
	public void testActualizarNivelNoCambiaElNivelDelUsuario() {
		NivelInvestigador nivel = new NivelInvestigador();
		Usuario usuario = new Usuario("user", "pass", null);
		usuario.setNivel(nivel);

		nivel.actualizarNivel(usuario);

		assertEquals("Nivel Investigador", usuario.getNivel().getNombreNivel());
	}

	@Test
	public void testGetNombreNivelDebeDevolverCorrectamente() {
		NivelInvestigador nivel = new NivelInvestigador();
		assertEquals("Nivel Investigador", nivel.getNombreNivel());
	}
}
