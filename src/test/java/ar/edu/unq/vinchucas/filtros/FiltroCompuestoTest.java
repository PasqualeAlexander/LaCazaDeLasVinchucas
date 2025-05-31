package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FiltroCompuestoTest {
    private FiltroCompuesto filtroAND;
    private FiltroCompuesto filtroOR;
    private Muestra muestra1;
    private Muestra muestra2;
    private Muestra muestra3;
    private List<Muestra> muestras;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        // Configuración de filtros compuestos
        filtroAND = new FiltroCompuesto(OperadorLogico.AND);
        filtroOR = new FiltroCompuesto(OperadorLogico.OR);

        // Creación de usuario y muestras de prueba
        usuario = new Usuario("test", false);
        
        // Muestra1: Vinchuca con última votación ayer
        muestra1 = new Muestra("foto1.jpg", "-34.5,58.5", usuario);
        muestra1.agregarOpinion(TipoDeOpinion.VINCHUCA, usuario);
        muestra1.setFechaUltimaVotacion(LocalDate.now().minusDays(1));
        
        // Muestra2: Chinche con última votación hace una semana, verificada
        muestra2 = new Muestra("foto2.jpg", "-32.5,58.5", usuario);
        muestra2.agregarOpinion(TipoDeOpinion.CHINCHE, usuario);
        muestra2.setFechaUltimaVotacion(LocalDate.now().minusWeeks(1));
        muestra2.setVerificada(true);
        
        // Muestra3: Vinchuca sin votaciones
        muestra3 = new Muestra("foto3.jpg", "-34.6,58.4", usuario);
        muestra3.agregarOpinion(TipoDeOpinion.VINCHUCA, usuario);
        
        muestras = Arrays.asList(muestra1, muestra2, muestra3);
    }

    @Test
    void filtroCompuestoVacioDevuelveMismasMuestras() {
        // Un filtro compuesto sin filtros debe devolver todas las muestras
        assertEquals(muestras, filtroAND.filtrar(muestras));
        assertEquals(muestras, filtroOR.filtrar(muestras));
    }

    @Test
    void filtroCompuestoANDConDosFiltrosDevuelveMuestrasCorrectas() {
        // Creamos dos filtros: uno por tipo de insecto y otro por fecha de última votación
        FiltroPorTipoInsecto filtroVinchuca = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA);
        FiltroPorUltimaVotacion filtroUltimaVotacion = new FiltroPorUltimaVotacion(
            LocalDate.now().minusDays(2), 
            LocalDate.now()
        );

        filtroAND.agregarFiltro(filtroVinchuca);
        filtroAND.agregarFiltro(filtroUltimaVotacion);

        List<Muestra> resultado = filtroAND.filtrar(muestras);

        // Debería devolver solo muestra1 (Vinchuca con votación reciente)
        assertTrue(resultado.contains(muestra1));
        assertFalse(resultado.contains(muestra2)); // Es Chinche
        assertFalse(resultado.contains(muestra3)); // No tiene votación reciente
        assertEquals(1, resultado.size());
    }

    @Test
    void filtroCompuestoORDevuelveMuestrasQueCumplenAlMenosUnFiltro() {
        // Creamos dos filtros: uno por tipo de insecto y otro por nivel de verificación
        FiltroPorTipoInsecto filtroVinchuca = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA);
        FiltroPorNivelVerificacion filtroVerificada = new FiltroPorNivelVerificacion(true);

        filtroOR.agregarFiltro(filtroVinchuca);
        filtroOR.agregarFiltro(filtroVerificada);

        List<Muestra> resultado = filtroOR.filtrar(muestras);

        // Debería devolver muestra1 y muestra2 y muestra3 (Vinchucas o verificadas)
        assertTrue(resultado.contains(muestra1)); // Es Vinchuca
        assertTrue(resultado.contains(muestra2)); // Está verificada
        assertTrue(resultado.contains(muestra3)); // Es Vinchuca
        assertEquals(3, resultado.size());
    }

    @Test
    void filtroCompuestoANDNoDevuelveMuestrasSiNoSeCumplenTodosLosFiltros() {
        // Creamos dos filtros incompatibles
        FiltroPorTipoInsecto filtroVinchuca = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA);
        FiltroPorTipoInsecto filtroChinche = new FiltroPorTipoInsecto(TipoDeOpinion.CHINCHE);

        filtroAND.agregarFiltro(filtroVinchuca);
        filtroAND.agregarFiltro(filtroChinche);

        List<Muestra> resultado = filtroAND.filtrar(muestras);

        // Ninguna muestra puede ser Vinchuca y Chinche a la vez
        assertTrue(resultado.isEmpty());
    }

    @Test
    void filtroCompuestoORDevuelveResultadosUnicos() {
        // Creamos dos filtros que se solapan para la misma muestra
        FiltroPorTipoInsecto filtroVinchuca = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA);
        FiltroPorFecha filtroFechaCreacion = new FiltroPorFecha(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)
        );

        filtroOR.agregarFiltro(filtroVinchuca);
        filtroOR.agregarFiltro(filtroFechaCreacion);

        List<Muestra> resultado = filtroOR.filtrar(muestras);

        // Aunque una muestra cumpla ambos filtros, no debería aparecer duplicada
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra2));
        assertTrue(resultado.contains(muestra3));
    }
} 