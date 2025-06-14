package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;

public class NivelInvestigadorTest {

    @Test
    public void testPuedeVerificarDebeSerTrue() {
        NivelInvestigador nivel = new NivelInvestigador();
        assertTrue(nivel.puedeVerificar(), "NivelInvestigador debe poder verificar");
    }

    @Test
    public void testActualizarNivelNoCambiaElNivelDelUsuario() {
        NivelInvestigador nivel = new NivelInvestigador();
        Usuario usuario = new Usuario("user", "pass", mock(RepositorioDeMuestras.class), mock(RepositorioDeOpiniones.class));
        usuario.setNivel(nivel);

        nivel.actualizarNivel(usuario);

        assertEquals("Nivel Investigador", usuario.getNivel().getNombreNivel());
    }

    @Test
    public void testGetNombreNivelDebeDevolverCorrectamente() {
        NivelInvestigador nivel = new NivelInvestigador();
        assertEquals("Nivel Investigador", nivel.getNombreNivel());
    }

    @Test
    public void testEsNivelExpertoDebeSerTrue() {
        NivelInvestigador nivel = new NivelInvestigador();
        assertTrue(nivel.esNivelExperto(), "NivelInvestigador debe ser nivel experto");
    }
}
