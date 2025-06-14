package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FiltroPorTipoInsectoTest {
    @Test
    public void testFiltraPorTipoDeInsecto() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        Muestra muestra3 = new Muestra("foto3.jpg", "loc3", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);

        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        Filtro filtro = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS);
        List<Muestra> resultado = filtro.filtrar(muestras);

        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra3));
        assertFalse(resultado.contains(muestra2));
    }
} 