package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class FiltroCompuestoTest {
    @Test
    public void testFiltroCompuestoAND() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        setFechaCreacion(muestra1, LocalDate.now().minusDays(1));
        setFechaCreacion(muestra2, LocalDate.now());
        List<Muestra> muestras = List.of(muestra1, muestra2);

        Filtro filtroTipo = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS);
        Filtro filtroFecha = new FiltroPorFecha(LocalDate.now().minusDays(2), LocalDate.now());
        FiltroCompuesto filtroAND = new FiltroCompuesto(OperadorLogico.AND);
        filtroAND.agregarFiltro(filtroTipo);
        filtroAND.agregarFiltro(filtroFecha);

        List<Muestra> resultado = filtroAND.filtrar(muestras);
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(muestra1));
    }

    @Test
    public void testFiltroCompuestoOR() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        setFechaCreacion(muestra1, LocalDate.now().minusDays(1));
        setFechaCreacion(muestra2, LocalDate.now());
        List<Muestra> muestras = List.of(muestra1, muestra2);

        Filtro filtroTipo = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS);
        Filtro filtroFecha = new FiltroPorFecha(LocalDate.now().minusDays(2), LocalDate.now());
        FiltroCompuesto filtroOR = new FiltroCompuesto(OperadorLogico.OR);
        filtroOR.agregarFiltro(filtroTipo);
        filtroOR.agregarFiltro(filtroFecha);

        List<Muestra> resultado = filtroOR.filtrar(muestras);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra2));
    }

    @Test
    public void testFiltroCompuestoVacioDevuelveTodas() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        List<Muestra> muestras = List.of(muestra1, muestra2);

        FiltroCompuesto filtroVacio = new FiltroCompuesto(OperadorLogico.AND);
        // No agregamos ningún filtro

        List<Muestra> resultado = filtroVacio.filtrar(muestras);
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra2));
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