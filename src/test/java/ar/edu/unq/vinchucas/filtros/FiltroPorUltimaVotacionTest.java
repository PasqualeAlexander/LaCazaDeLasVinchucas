package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FiltroPorUltimaVotacionTest {
    @Test
    public void testFiltraPorFechaDeUltimaVotacion() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Usuario otroUsuario = mock(Usuario.class);
        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        Muestra muestra3 = new Muestra("foto3.jpg", "loc3", usuario, TipoDeOpinion.VINCHUCA_SORDIDA);

        // Agregamos opiniones con fechas simuladas usando reflection
        Opinion op1 = new Opinion(otroUsuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion op2 = new Opinion(otroUsuario, TipoDeOpinion.CHINCHE_FOLIADA);
        Opinion op3 = new Opinion(otroUsuario, TipoDeOpinion.VINCHUCA_SORDIDA);
        setFechaOpinion(op1, LocalDate.now().minusDays(5));
        setFechaOpinion(op2, LocalDate.now().minusDays(2));
        setFechaOpinion(op3, LocalDate.now());
        muestra1.agregarOpinion(op1);
        muestra2.agregarOpinion(op2);
        muestra3.agregarOpinion(op3);

        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        LocalDate desde = LocalDate.now().minusDays(3);
        LocalDate hasta = LocalDate.now();
        Filtro filtro = new FiltroPorUltimaVotacion(desde, hasta);
        List<Muestra> resultado = filtro.filtrar(muestras);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(muestra2));
        assertTrue(resultado.contains(muestra3));
        assertFalse(resultado.contains(muestra1));
    }

    // Utilidad para setear la fecha de la opinión usando reflection
    private void setFechaOpinion(Opinion opinion, LocalDate fecha) {
        try {
            java.lang.reflect.Field field = Opinion.class.getDeclaredField("fecha");
            field.setAccessible(true);
            field.set(opinion, fecha);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 