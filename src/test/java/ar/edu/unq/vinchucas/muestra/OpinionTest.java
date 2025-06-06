package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class OpinionTest {
    private Opinion opinion;
    private Usuario usuario;
    private TipoDeOpinion tipoOpinion;
    private LocalDate fechaCreacion;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario("testUser", "password", null, null);
        tipoOpinion = TipoDeOpinion.VINCHUCA_INFESTANS;
        fechaCreacion = LocalDate.now();
        opinion = new Opinion(usuario, tipoOpinion);
    }
    
    @Test
    void testCreacionDeOpinion() {
        assertNotNull(opinion);
        assertEquals(usuario, opinion.getUsuario());
        assertEquals(tipoOpinion, opinion.getTipoDeOpinion());
        assertEquals(fechaCreacion, opinion.getFecha());
    }
    
    @Test
    void testFechaDeCreacionEsHoy() {
        LocalDate hoy = LocalDate.now();
        assertEquals(hoy, opinion.getFecha());
    }
    
    @Test
    void testDosOpinionesEnDiferentesDiasNoTienenLaMismaFecha() {
        Opinion opinionAntigua = new Opinion(usuario, tipoOpinion);
        LocalDate fechaAntigua = opinionAntigua.getFecha().minusDays(1);
        assertNotEquals(fechaAntigua, opinion.getFecha());
    }
    
    @Test
    void testDosOpinionesCreadasEnElMismoDiaTienenLaMismaFecha() {
        Opinion otraOpinion = new Opinion(usuario, tipoOpinion);
        assertEquals(opinion.getFecha(), otraOpinion.getFecha());
    }
    
    @Test
    void testOpinionConDiferenteTipoDeOpinion() {
        Opinion opinionChinche = new Opinion(usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        assertNotEquals(opinion.getTipoDeOpinion(), opinionChinche.getTipoDeOpinion());
        assertEquals(TipoDeOpinion.CHINCHE_FOLIADA, opinionChinche.getTipoDeOpinion());
    }
    
    @Test
    void testOpinionExpertaSigueSiendoExpertaSiUsuarioSeVuelveBasico() {
        Usuario usuarioExperto = mock(Usuario.class);
        when(usuarioExperto.esNivelBasico()).thenReturn(false);
        when(usuarioExperto.esNivelExperto()).thenReturn(true);

        Opinion opinionExperta = new Opinion(usuarioExperto, TipoDeOpinion.VINCHUCA_SORDIDA);

        when(usuarioExperto.esNivelBasico()).thenReturn(true);
        when(usuarioExperto.esNivelExperto()).thenReturn(false);

        assertTrue(opinionExperta.eraExpertoAlOpinar());
    }
    
    @Test
    void testOpinionBasicaSigueSiendoBasicaSiUsuarioSeVuelveExperto() throws SistemaDeExcepciones {
        Usuario otroBasico = mock(Usuario.class);
        when(otroBasico.esNivelBasico()).thenReturn(true);
        when(otroBasico.esNivelExperto()).thenReturn(false);

        Opinion opinionBasica = new Opinion(otroBasico, TipoDeOpinion.CHINCHE_FOLIADA);

        when(otroBasico.esNivelBasico()).thenReturn(false);
        when(otroBasico.esNivelExperto()).thenReturn(true);

        assertFalse(opinionBasica.eraExpertoAlOpinar());
    }

    
    
} 