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

public class NivelExpertoTest {

    private NivelExperto nivelExperto;
    private Usuario usuario;
    private RepositorioDeMuestras repoMuestras;
    private RepositorioDeOpiniones repoOpiniones;

    @BeforeEach
    public void setUp() {
        nivelExperto = new NivelExperto();
        repoMuestras = new RepositorioDeMuestras();
        repoOpiniones = new RepositorioDeOpiniones();
        usuario = new Usuario("expertoTest", "password", repoMuestras, repoOpiniones);
        usuario.setNivel(nivelExperto);
    }

    @Test
    public void testPuedeVerificarDevuelveTrue() {
        assertTrue(nivelExperto.puedeVerificar());
    }

    @Test
    public void testNombreNivelEsCorrecto() {
        assertEquals("Nivel Experto", nivelExperto.getNombreNivel());
    }

    @Test
    public void testExpertoSeMantieneComoExpertoCuandoCumpleRequisitos() throws SistemaDeExcepciones {
        // Simular actividad suficiente para mantener nivel experto
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        
        // Enviar 20 muestras
        for (int i = 0; i < 20; i++) {
            Muestra muestra = new Muestra("foto" + i + ".jpg", "ubicacion", usuario, tipo);
            usuario.enviarMuestra(muestra);
        }
        
        // Opinar en 20 muestras de otros usuarios
        for (int i = 0; i < 20; i++) {
            Usuario otroUsuario = new Usuario("otro" + i, "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
            Muestra muestra = new Muestra("foto" + i + ".jpg", "ubicacion", otroUsuario, tipo);
            usuario.opinar(muestra, new Opinion(usuario, tipo));
        }
        
        // Verificar que mantiene nivel experto
        assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
        assertTrue(usuario.esNivelExperto());
        assertFalse(usuario.esNivelBasico());
    }

    @Test
    public void testExpertoBajaABasicoCuandoNoTieneSuficienteActividad() throws SistemaDeExcepciones {
        // Usuario experto con poca actividad (menos de 20 muestras y opiniones)
        assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
        
        // Actividad insuficiente: solo 5 muestras y 5 opiniones
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        
        for (int i = 0; i < 5; i++) {
            Muestra muestra = new Muestra("foto" + i + ".jpg", "ubicacion", usuario, tipo);
            usuario.enviarMuestra(muestra);
            
            Usuario otroUsuario = new Usuario("otro" + i, "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
            Muestra muestraAjena = new Muestra("ajena" + i + ".jpg", "ubicacion", otroUsuario, tipo);
            usuario.opinar(muestraAjena, new Opinion(usuario, tipo));
        }
        
        // Al actualizar nivel, debería bajar a básico
        usuario.actualizarNivel();
        
        assertEquals("Nivel Basico", usuario.getNivel().getNombreNivel());
        assertFalse(usuario.esNivelExperto());
        assertTrue(usuario.esNivelBasico());
    }

    @Test
    public void testExpertoBajaABasicoCuandoNoTieneSuficientesMuestras() throws SistemaDeExcepciones {
        // Experto con suficientes opiniones pero pocas muestras
        assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
        
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        
        // Solo 10 muestras (insuficiente)
        for (int i = 0; i < 10; i++) {
            Muestra muestra = new Muestra("foto" + i + ".jpg", "ubicacion", usuario, tipo);
            usuario.enviarMuestra(muestra);
        }
        
        // Pero 25 opiniones (suficiente)
        for (int i = 0; i < 25; i++) {
            Usuario otroUsuario = new Usuario("otro" + i, "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
            Muestra muestraAjena = new Muestra("ajena" + i + ".jpg", "ubicacion", otroUsuario, tipo);
            usuario.opinar(muestraAjena, new Opinion(usuario, tipo));
        }
        
        // Debería bajar a básico por falta de muestras
        usuario.actualizarNivel();
        
        assertEquals("Nivel Basico", usuario.getNivel().getNombreNivel());
        assertFalse(usuario.esNivelExperto());
        assertTrue(usuario.esNivelBasico());
    }

    @Test
    public void testExpertoBajaABasicoCuandoNoTieneSuficientesOpiniones() throws SistemaDeExcepciones {
        // Experto con suficientes muestras pero pocas opiniones
        assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
        
        TipoDeOpinion tipo = TipoDeOpinion.VINCHUCA_INFESTANS;
        
        // 25 muestras (suficiente)
        for (int i = 0; i < 25; i++) {
            Muestra muestra = new Muestra("foto" + i + ".jpg", "ubicacion", usuario, tipo);
            usuario.enviarMuestra(muestra);
        }
        
        // Solo 10 opiniones (insuficiente)
        for (int i = 0; i < 10; i++) {
            Usuario otroUsuario = new Usuario("otro" + i, "pass", new RepositorioDeMuestras(), new RepositorioDeOpiniones());
            Muestra muestraAjena = new Muestra("ajena" + i + ".jpg", "ubicacion", otroUsuario, tipo);
            usuario.opinar(muestraAjena, new Opinion(usuario, tipo));
        }
        
        // Debería bajar a básico por falta de opiniones
        usuario.actualizarNivel();
        
        assertEquals("Nivel Basico", usuario.getNivel().getNombreNivel());
        assertFalse(usuario.esNivelExperto());
        assertTrue(usuario.esNivelBasico());
    }

    @Test
    public void testEsNivelExpertoDevuelveTrue() {
        assertTrue(nivelExperto.esNivelExperto());
        assertFalse(nivelExperto.esNivelBasico());
    }
}
