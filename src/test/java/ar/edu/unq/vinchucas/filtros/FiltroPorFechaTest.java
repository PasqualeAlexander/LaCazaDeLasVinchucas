package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FiltroPorFechaTest {
    @Test
    public void testFiltraPorFechaDeCreacion() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        Muestra muestra3 = new Muestra("foto3.jpg", "loc3", usuario, TipoDeOpinion.VINCHUCA_SORDIDA);

        // Simulamos fechas de creación usando reflection (no hay setter)
        setFechaCreacion(muestra1, LocalDate.now().minusDays(5));
        setFechaCreacion(muestra2, LocalDate.now().minusDays(2));
        setFechaCreacion(muestra3, LocalDate.now());

        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        LocalDate desde = LocalDate.now().minusDays(3);
        LocalDate hasta = LocalDate.now();
        Filtro filtro = new FiltroPorFecha(desde, hasta);
        List<Muestra> resultado = filtro.filtrar(muestras);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(muestra2));
        assertTrue(resultado.contains(muestra3));
        assertFalse(resultado.contains(muestra1));
    }

    // Utilidad para setear la fecha de creación usando reflection
    private void setFechaCreacion(Muestra muestra, LocalDate fecha) {
        try {
            java.lang.reflect.Field field = Muestra.class.getDeclaredField("fechaCreacion");
            field.setAccessible(true);
            field.set(muestra, fecha);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 