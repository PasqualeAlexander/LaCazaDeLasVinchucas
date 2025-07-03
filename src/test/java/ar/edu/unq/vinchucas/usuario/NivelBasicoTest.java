package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NivelBasicoTest {

    private Usuario usuario;
    private RepositorioDeMuestras repoMuestras;
    private RepositorioDeOpiniones repoOpiniones;

    @BeforeEach
    public void setUp() {
        repoMuestras = new RepositorioDeMuestras();
        repoOpiniones = new RepositorioDeOpiniones();
        usuario = new Usuario("usuarioTest", "password", repoMuestras, repoOpiniones);
    }

    @Test
    public void testPuedeVerificarDevuelveFalse() {
        NivelBasico nivel = new NivelBasico();
        assertFalse(nivel.puedeVerificar());
    }

    @Test
    public void testActualizarNivelPermaneceNivelBasicoCuandoNoCumpleRequisitos() throws SistemaDeExcepciones {
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        for (int i = 0; i < 10; i++) {
            Usuario otroUsuario = new Usuario("otro" + i, "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
            Muestra muestra = new Muestra("foto.jpg", "loc", otroUsuario, tipo);
            usuario.opinar(muestra, new Opinion(usuario, tipo));
        }

        assertEquals("Nivel Basico", usuario.getNivel().getNombreNivel());
    }

    @Test
    public void testActualizarNivelCambiaANivelExpertoCuandoCumpleRequisitos() throws SistemaDeExcepciones {
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        for (int i = 0; i < 20; i++) {
            Usuario otroUsuario = new Usuario("otro" + i, "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
            Muestra muestra = new Muestra("foto" + i + ".jpg", "loc", otroUsuario, tipo);
            usuario.enviarMuestra(new Muestra("foto" + i, "loc", usuario, tipo));
            usuario.opinar(muestra, new Opinion(usuario, tipo));
        }

        assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
    }

    @Test
    public void testNoPuedeOpinarDosVecesSobreLaMismaMuestra() throws SistemaDeExcepciones {
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        Usuario otroUsuario = new Usuario("creador", "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
        Muestra muestra = new Muestra("foto.jpg", "loc", otroUsuario, tipo);

        usuario.opinar(muestra, new Opinion(usuario, tipo));

        SistemaDeExcepciones ex = assertThrows(SistemaDeExcepciones.class, () ->
            usuario.opinar(muestra, new Opinion(usuario, tipo))
        );

        assertEquals("El usuario ya ha opinado sobre esta muestra", ex.getMessage());
    }

}