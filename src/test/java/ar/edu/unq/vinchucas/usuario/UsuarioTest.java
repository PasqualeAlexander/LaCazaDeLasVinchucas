package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario usuario;
    private RepositorioDeMuestras repoMuestras;
    private RepositorioDeOpiniones repoOpiniones;

    @BeforeEach
    void setUp() {
        repoMuestras = new RepositorioDeMuestras();
        repoOpiniones = new RepositorioDeOpiniones();
        usuario = new Usuario("alex", "1234", repoMuestras, repoOpiniones);
    }

    @Test
    void testEnviarMuestraAgregaMuestraAlRepositorio() throws SistemaDeExcepciones {
        Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        usuario.enviarMuestra(muestra);
        assertEquals(1, usuario.getMuestrasEnviadas().size());
        assertTrue(usuario.getMuestrasEnviadas().contains(muestra));
    }

    @Test
    void testOpinarAgregaOpinionAlRepositorioYALaMuestra() throws SistemaDeExcepciones {
        Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Usuario otroUsuario = new Usuario("otro", "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
        Opinion opinion = new Opinion(otroUsuario, TipoDeOpinion.CHINCHE_FOLIADA);

        otroUsuario.opinar(muestra, opinion);

        assertEquals(1, otroUsuario.getOpinionesEnviadas().size());
        assertTrue(muestra.getOpiniones().contains(opinion));
    }

    @Test
    void testNoPuedeOpinarDosVecesSobreLaMismaMuestra() throws SistemaDeExcepciones {
        Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Usuario otroUsuario = new Usuario("otro", "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
        Opinion opinion = new Opinion(otroUsuario, TipoDeOpinion.CHINCHE_FOLIADA);

        otroUsuario.opinar(muestra, opinion);

        SistemaDeExcepciones excepcion = assertThrows(SistemaDeExcepciones.class, () -> {
            otroUsuario.opinar(muestra, opinion);
        });

        assertEquals("El usuario no puede opinar sobre esta muestra.", excepcion.getMessage());
    }

    @Test
    void testGetNombreUsuarioDevuelveNombreCorrecto() {
        assertEquals("alex", usuario.getNombreUsuario());
    }

    @Test
    void testCambiarContraseñaModificaLaContraseña() throws SistemaDeExcepciones {
        usuario.cambiarContraseña("1234", "nuevaClave");
        assertEquals("nuevaClave", usuario.getContraseña());
    }

    @Test
    void testGetRepositorioDevuelveElCorrecto() {
        assertEquals(repoMuestras, usuario.getRepositorio());
    }

    @Test
    void testNivelBasicoNoPuedeVerificar() {
        INivelDeUsuario nivelBasico = new NivelBasico();
        assertFalse(nivelBasico.puedeVerificar());
    }

    @Test
    void testNivelExpertoPuedeVerificar() {
        INivelDeUsuario nivelExperto = new NivelExperto();
        assertTrue(nivelExperto.puedeVerificar());
    }

    @Test
    void testNivelInvestigadorPuedeVerificar() {
        INivelDeUsuario nivelInvestigador = new NivelInvestigador();
        assertTrue(nivelInvestigador.puedeVerificar());
    }
}