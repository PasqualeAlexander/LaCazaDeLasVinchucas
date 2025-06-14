package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FiltroPorNivelVerificacionTest {
    @Test
    public void testFiltraPorNivelDeVerificacion() throws SistemaDeExcepciones {
        Usuario usuario = mock(Usuario.class);
        Usuario experto1 = mock(Usuario.class);
        Usuario experto2 = mock(Usuario.class);
        when(experto1.esNivelExperto()).thenReturn(true);
        when(experto2.esNivelExperto()).thenReturn(true);
        when(usuario.esNivelExperto()).thenReturn(false);

        Muestra muestra1 = new Muestra("foto1.jpg", "loc1", usuario, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto2.jpg", "loc2", usuario, TipoDeOpinion.CHINCHE_FOLIADA);
        // Verificamos muestra2 agregando dos opiniones de expertos diferentes
        Opinion op1 = new Opinion(experto1, TipoDeOpinion.CHINCHE_FOLIADA);
        Opinion op2 = new Opinion(experto2, TipoDeOpinion.CHINCHE_FOLIADA);
        muestra2.agregarOpinion(op1);
        muestra2.agregarOpinion(op2);

        List<Muestra> muestras = List.of(muestra1, muestra2);
        Filtro filtroVerificadas = new FiltroPorNivelVerificacion(true);
        Filtro filtroNoVerificadas = new FiltroPorNivelVerificacion(false);

        List<Muestra> verificadas = filtroVerificadas.filtrar(muestras);
        List<Muestra> noVerificadas = filtroNoVerificadas.filtrar(muestras);

        assertEquals(1, verificadas.size());
        assertTrue(verificadas.contains(muestra2));
        assertFalse(verificadas.contains(muestra1));

        assertEquals(1, noVerificadas.size());
        assertTrue(noVerificadas.contains(muestra1));
        assertFalse(noVerificadas.contains(muestra2));
    }
} 