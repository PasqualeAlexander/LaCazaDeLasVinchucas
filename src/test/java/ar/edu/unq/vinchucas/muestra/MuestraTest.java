package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class MuestraTest {

    private Muestra muestra;
    private Usuario usuarioBasico;
    private Usuario usuarioExperto1;
    private Usuario usuarioExperto2;
    private Opinion opinionExperta1;
    private Opinion opinionExperta2;

    @BeforeEach
    void setUp() throws SistemaDeExcepciones {
        usuarioBasico = mock(Usuario.class);
        usuarioExperto1 = mock(Usuario.class);
        usuarioExperto2 = mock(Usuario.class);

        when(usuarioBasico.esNivelBasico()).thenReturn(true);
        when(usuarioBasico.esNivelExperto()).thenReturn(false);

        when(usuarioExperto1.esNivelBasico()).thenReturn(false);
        when(usuarioExperto1.esNivelExperto()).thenReturn(true);

        when(usuarioExperto2.esNivelBasico()).thenReturn(false);
        when(usuarioExperto2.esNivelExperto()).thenReturn(true);

        muestra = new Muestra("foto.jpg", "Buenos Aires", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);

        opinionExperta1 = new Opinion(usuarioExperto1, TipoDeOpinion.VINCHUCA_SORDIDA);
        opinionExperta2 = new Opinion(usuarioExperto2, TipoDeOpinion.VINCHUCA_SORDIDA);
    }

    @Test
    public void testResultadoActualEsElEsperado() throws SistemaDeExcepciones {
        Usuario user1 = mock(Usuario.class);
        Usuario user2 = mock(Usuario.class);
        Usuario user3 = mock(Usuario.class);
        Usuario user4 = mock(Usuario.class);
        Usuario user5 = mock(Usuario.class);
        Usuario user6 = mock(Usuario.class);

        muestra.agregarOpinion(new Opinion(user1, TipoDeOpinion.VINCHUCA_INFESTANS));
        muestra.agregarOpinion(new Opinion(user2, TipoDeOpinion.VINCHUCA_INFESTANS));
        muestra.agregarOpinion(new Opinion(user3, TipoDeOpinion.NINGUNA));
        muestra.agregarOpinion(new Opinion(user4, TipoDeOpinion.CHINCHE_FOLIADA));
        muestra.agregarOpinion(new Opinion(user5, TipoDeOpinion.VINCHUCA_INFESTANS));
        muestra.agregarOpinion(new Opinion(user6, TipoDeOpinion.CHINCHE_FOLIADA));

        TipoDeOpinion resultado = muestra.getResultado();

        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, resultado);
    }

    @Test
    public void testResultadoInicialEsVotoDelCreador() {
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra.getResultado());
    }

    @Test
    void testVotoExpertoInvalidaVotosBasicos() throws SistemaDeExcepciones {
    	Usuario otroBasico = mock(Usuario.class);
        when(otroBasico.esNivelBasico()).thenReturn(true);

        Opinion opinionBasica = new Opinion(otroBasico, TipoDeOpinion.IMAGEN_POCO_CLARA);
        
        muestra.agregarOpinion(opinionBasica);
        muestra.agregarOpinion(opinionExperta1);

        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestra.getResultado());
    }

    @Test
    void testMuestraSeVerificaConDosExpertos() throws SistemaDeExcepciones {
        muestra.agregarOpinion(opinionExperta1);
        assertFalse(muestra.estaVerificada());

        muestra.agregarOpinion(opinionExperta2);
        assertTrue(muestra.estaVerificada());
    }

    @Test
    void testNoSePuedeOpinarSobreMuestraVerificada() throws SistemaDeExcepciones {
        muestra.agregarOpinion(opinionExperta1);
        muestra.agregarOpinion(opinionExperta2);

        Opinion nuevaOpinion = new Opinion(usuarioExperto1, TipoDeOpinion.CHINCHE_FOLIADA);

        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            muestra.agregarOpinion(nuevaOpinion);
        });
        assertEquals("El usuario no puede opinar sobre esta muestra.", thrown.getMessage());

        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestra.getResultado());
    }

    @Test
    void testCreadorNoPuedeOpinarPeroSuVotoCuenta() throws SistemaDeExcepciones {
        Opinion opinionCreador = new Opinion(usuarioBasico, TipoDeOpinion.CHINCHE_FOLIADA);

        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            muestra.agregarOpinion(opinionCreador);
        });
        assertEquals("El usuario no puede opinar sobre esta muestra.", thrown.getMessage());

        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra.getResultado());
    }

    @Test
    void testOpinionExpertaSigueSiendoExpertaSiUsuarioSeVuelveBasico() throws SistemaDeExcepciones {
        muestra.agregarOpinion(opinionExperta1);

        when(usuarioExperto1.esNivelBasico()).thenReturn(true);
        when(usuarioExperto1.esNivelExperto()).thenReturn(false);

        assertTrue(muestra.getOpiniones().get(0).eraExpertoAlOpinar());
    }

    @Test
    void testCreacionMuestraConFotoVacia() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            new Muestra("", "Buenos Aires", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        });
        assertEquals("La foto no puede estar vacía", thrown.getMessage());
    }

    @Test
    void testCreacionMuestraConFotoNull() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            new Muestra(null, "Buenos Aires", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        });
        assertEquals("La foto no puede estar vacía", thrown.getMessage());
    }

    @Test
    void testCreacionMuestraConUbicacionVacia() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            new Muestra("foto.jpg", "", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        });
        assertEquals("La ubicación no puede estar vacía", thrown.getMessage());
    }

    @Test
    void testCreacionMuestraConUbicacionNull() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            new Muestra("foto.jpg", null, usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        });
        assertEquals("La ubicación no puede estar vacía", thrown.getMessage());
    }

    @Test
    void testCreacionMuestraConUsuarioNull() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            new Muestra("foto.jpg", "Buenos Aires", null, TipoDeOpinion.VINCHUCA_INFESTANS);
        });
        assertEquals("El usuario no puede ser nulo", thrown.getMessage());
    }

    @Test
    void testCreacionMuestraConVotoInicialNull() {
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            new Muestra("foto.jpg", "Buenos Aires", usuarioBasico, null);
        });
        assertEquals("El voto inicial no puede ser nulo", thrown.getMessage());
    }

    @Test
    void testGettersBasicos() throws SistemaDeExcepciones {
        Muestra muestraTest = new Muestra("foto_test.jpg", "Córdoba", usuarioBasico, TipoDeOpinion.CHINCHE_FOLIADA);
        
        assertEquals("foto_test.jpg", muestraTest.getFoto());
        assertEquals("Córdoba", muestraTest.getUbicacion());
        assertEquals(usuarioBasico, muestraTest.getUsuario());
        assertEquals(EstadoMuestra.NO_VERIFICADA, muestraTest.getEstado());
        assertNotNull(muestraTest.getFechaCreacion());
    }

    @Test
    void testGetNombreUsuario() throws SistemaDeExcepciones {
        when(usuarioBasico.getNombreUsuario()).thenReturn("juan123");
        
        Muestra muestraTest = new Muestra("foto.jpg", "Buenos Aires", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        assertEquals("juan123", muestraTest.getNombreUsuario());
    }

    @Test
    void testGetOpiniones() throws SistemaDeExcepciones {
        assertTrue(muestra.getOpiniones().isEmpty()); // Inicialmente vacía (solo cuenta el voto inicial del sistema)
        
        muestra.agregarOpinion(opinionExperta1);
        assertEquals(1, muestra.getOpiniones().size());
        assertTrue(muestra.getOpiniones().contains(opinionExperta1));
    }

    @Test
    void testGetFechaUltimaVotacion() throws SistemaDeExcepciones {
        assertNull(muestra.getFechaUltimaVotacion()); // Sin opiniones adicionales
        
        muestra.agregarOpinion(opinionExperta1);
        assertNotNull(muestra.getFechaUltimaVotacion());
    }

    @Test
    void testAdmiteOpinionIndirectamente() throws SistemaDeExcepciones {
        Usuario otroUsuario = mock(Usuario.class);
        when(otroUsuario.esNivelBasico()).thenReturn(true);
        
        // Probamos indirectamente a través de agregarOpinion
        Opinion opinionOtroUsuario = new Opinion(otroUsuario, TipoDeOpinion.CHINCHE_FOLIADA);
        assertDoesNotThrow(() -> muestra.agregarOpinion(opinionOtroUsuario));
        
        // Después de verificar, no admite más opiniones
        muestra.agregarOpinion(opinionExperta1);
        muestra.agregarOpinion(opinionExperta2);
        
        Usuario tercerUsuario = mock(Usuario.class);
        when(tercerUsuario.esNivelBasico()).thenReturn(true);
        Opinion opinionTercero = new Opinion(tercerUsuario, TipoDeOpinion.NINGUNA);
        
        SistemaDeExcepciones thrown = assertThrows(SistemaDeExcepciones.class, () -> {
            muestra.agregarOpinion(opinionTercero);
        });
        assertEquals("El usuario no puede opinar sobre esta muestra.", thrown.getMessage());
    }

    @Test
    void testEstadoMuestraSeActualizaAlVerificar() throws SistemaDeExcepciones {
        assertEquals(EstadoMuestra.NO_VERIFICADA, muestra.getEstado());
        
        muestra.agregarOpinion(opinionExperta1);
        assertEquals(EstadoMuestra.NO_VERIFICADA, muestra.getEstado());
        
        muestra.agregarOpinion(opinionExperta2);
        assertEquals(EstadoMuestra.VERIFICADA, muestra.getEstado());
    }
    
}

