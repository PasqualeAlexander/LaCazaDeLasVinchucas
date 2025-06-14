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

    @Test
    void testCambiarContraseñaConContraseñaActualIncorrecta() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            usuario.cambiarContraseña("contraseñaIncorrecta", "nuevaClave");
        });
        assertEquals("Contraseña actual incorrecta", thrown.getMessage());
    }

    @Test
    void testSetContraseña() {
        usuario.setContraseña("nuevaContraseña");
        assertEquals("nuevaContraseña", usuario.getContraseña());
    }

    @Test
    void testActualizarNivel() {
        assertEquals(NivelBasico.class, usuario.getNivel().getClass());
        
        usuario.actualizarNivel();
        // Debería seguir siendo básico si no cumple los requisitos
        assertEquals(NivelBasico.class, usuario.getNivel().getClass());
    }

    @Test
    void testSetNivel() {
        INivelDeUsuario nivelExperto = new NivelExperto();
        usuario.setNivel(nivelExperto);
        
        assertEquals(nivelExperto, usuario.getNivel());
        assertTrue(usuario.esNivelExperto());
        assertFalse(usuario.esNivelBasico());
    }

    @Test
    void testEsNivelBasico() {
        assertTrue(usuario.esNivelBasico());
        assertFalse(usuario.esNivelExperto());
    }

    @Test
    void testEsNivelExperto() {
        usuario.setNivel(new NivelExperto());
        
        assertTrue(usuario.esNivelExperto());
        assertFalse(usuario.esNivelBasico());
    }

    @Test
    void testGetMuestrasEnviadas() throws SistemaDeExcepciones {
        assertTrue(usuario.getMuestrasEnviadas().isEmpty());
        
        Muestra muestra1 = new Muestra("foto1.jpg", "ubicacion1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "ubicacion2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        
        usuario.enviarMuestra(muestra1);
        usuario.enviarMuestra(muestra2);
        
        assertEquals(2, usuario.getMuestrasEnviadas().size());
        assertTrue(usuario.getMuestrasEnviadas().contains(muestra1));
        assertTrue(usuario.getMuestrasEnviadas().contains(muestra2));
    }

    @Test
    void testGetOpinionesEnviadas() throws SistemaDeExcepciones {
        assertTrue(usuario.getOpinionesEnviadas().isEmpty());
        
        Usuario otroUsuario = new Usuario("otro", "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
        Muestra muestra = new Muestra("foto.jpg", "ubicacion", otroUsuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion = new Opinion(usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        
        usuario.opinar(muestra, opinion);
        
        assertEquals(1, usuario.getOpinionesEnviadas().size());
        assertTrue(usuario.getOpinionesEnviadas().contains(opinion));
    }

    @Test
    void testGetContraseña() {
        assertEquals("1234", usuario.getContraseña());
    }

    @Test
    void testEnviarMuestraActualizaNivel() throws SistemaDeExcepciones {
        // Verificamos que se llama actualizarNivel al enviar muestra
        assertEquals(NivelBasico.class, usuario.getNivel().getClass());
        
        Muestra muestra = new Muestra("foto.jpg", "ubicacion", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        usuario.enviarMuestra(muestra);
        
        // El nivel debería seguir siendo básico (no cumple requisitos para experto)
        assertEquals(NivelBasico.class, usuario.getNivel().getClass());
    }

    @Test
    void testOpinarActualizaNivel() throws SistemaDeExcepciones {
        Usuario otroUsuario = new Usuario("otro", "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
        Muestra muestra = new Muestra("foto.jpg", "ubicacion", otroUsuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion = new Opinion(usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        
        assertEquals(NivelBasico.class, usuario.getNivel().getClass());
        
        usuario.opinar(muestra, opinion);
        
        // El nivel debería seguir siendo básico (no cumple requisitos para experto)
        assertEquals(NivelBasico.class, usuario.getNivel().getClass());
    }
}