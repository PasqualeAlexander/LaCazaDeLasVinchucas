package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.usuario.Usuario;
import java.time.LocalDate;

class OpinionTest {
    private Opinion opinion;
    private Usuario usuario;
    private TipoDeOpinion tipoOpinion;
    private LocalDate fechaCreacion;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario("testUser", "password");
        tipoOpinion = TipoDeOpinion.VINCHUCA_INFESTANS;
        fechaCreacion = LocalDate.now();
        opinion = new Opinion(usuario, tipoOpinion);
    }
    
    @Test
    void testCreacionDeOpinion() {
        assertNotNull(opinion);
        assertEquals(usuario, opinion.getUsuario());
        assertEquals(tipoOpinion, opinion.getOpinion());
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
        assertNotEquals(opinion.getOpinion(), opinionChinche.getOpinion());
        assertEquals(TipoDeOpinion.CHINCHE_FOLIADA, opinionChinche.getOpinion());
    }
} 