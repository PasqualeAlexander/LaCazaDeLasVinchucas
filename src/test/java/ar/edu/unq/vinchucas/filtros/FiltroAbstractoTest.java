package ar.edu.unq.vinchucas.filtros;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.muestra.Muestra;

/**
 * Test que verifica el correcto funcionamiento del patrón Template Method
 * en la clase FiltroAbstracto
 */
public class FiltroAbstractoTest {

    private FiltroAbstracto filtroTest;
    private Muestra muestra1;
    private Muestra muestra2;
    private Muestra muestra3;

    @BeforeEach
    public void setUp() {
        // config muestras mock
        muestra1 = mock(Muestra.class);
        muestra2 = mock(Muestra.class);
        muestra3 = mock(Muestra.class);

        //  implementación de prueba del filtro abstracto
        filtroTest = new FiltroAbstracto() {
            @Override
            protected boolean cumpleCriterio(Muestra muestra) {
                // Criteria de prueba: solo la muestra1 cumple el criterio
                return muestra.equals(muestra1);
            }
        };
    }

    @Test
    public void testTemplateMethodFiltraCorrectamente() {
        List<Muestra> muestras = Arrays.asList(muestra1, muestra2, muestra3);

        List<Muestra> resultado = filtroTest.filtrar(muestras);

        // Solo muestra1 debería estar en el resultado
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertFalse(resultado.contains(muestra2));
        assertFalse(resultado.contains(muestra3));
    }

    @Test
    public void testTemplateMethodConListaVacia() {
        List<Muestra> muestrasVacias = Arrays.asList();

        List<Muestra> resultado = filtroTest.filtrar(muestrasVacias);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testTemplateMethodConTodasLasMuestrasCumplenCriterio() {
        // Crear filtro que acepta todas las muestras
        FiltroAbstracto filtroTodas = new FiltroAbstracto() {
            @Override
            protected boolean cumpleCriterio(Muestra muestra) {
                return true; // Todas cumplen el criterio
            }
        };

        List<Muestra> muestras = Arrays.asList(muestra1, muestra2, muestra3);

        List<Muestra> resultado = filtroTodas.filtrar(muestras);

        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra2));
        assertTrue(resultado.contains(muestra3));
    }

    @Test
    public void testTemplateMethodConNingunaMuestraCumpleCriterio() {
        // crear filtro que rechaza todas las muestras
        FiltroAbstracto filtroNinguna = new FiltroAbstracto() {
            @Override
            protected boolean cumpleCriterio(Muestra muestra) {
                return false; // Ninguna cumple el criterio
            }
        };

        List<Muestra> muestras = Arrays.asList(muestra1, muestra2, muestra3);

        List<Muestra> resultado = filtroNinguna.filtrar(muestras);

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testTemplateMethodLlamaMetodoAbstracto() {
        // test spy para verificar que se llama al método abstracto
        FiltroAbstracto filtroSpy = spy(new FiltroAbstracto() {
            @Override
            protected boolean cumpleCriterio(Muestra muestra) {
                return muestra.equals(muestra1);
            }
        });

        List<Muestra> muestras = Arrays.asList(muestra1, muestra2);

        filtroSpy.filtrar(muestras);

        // Verificar  que el metodo cumpleCriterio fue llamado para cada muestra
        verify(filtroSpy).cumpleCriterio(muestra1);
        verify(filtroSpy).cumpleCriterio(muestra2);
    }

    @Test
    public void testImplementacionConcretaFiltroPorTipoRespetaTemplateMethod() {
        // Verificar que las implementaciones concretas usan el template method
        when(muestra1.getResultado()).thenReturn(ar.edu.unq.vinchucas.muestra.TipoDeOpinion.VINCHUCA_INFESTANS);
        when(muestra2.getResultado()).thenReturn(ar.edu.unq.vinchucas.muestra.TipoDeOpinion.CHINCHE_FOLIADA);

        FiltroPorTipoInsecto filtroTipo = new FiltroPorTipoInsecto(ar.edu.unq.vinchucas.muestra.TipoDeOpinion.VINCHUCA_INFESTANS);
        List<Muestra> muestras = Arrays.asList(muestra1, muestra2);

        List<Muestra> resultado = filtroTipo.filtrar(muestras);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(muestra1));
    }

    @Test
    public void testImplementacionConcretaFiltroPorNivelRespetaTemplateMethod() {
        // vrificar que las implementaciones concretas usan el template method
        when(muestra1.estaVerificada()).thenReturn(true);
        when(muestra2.estaVerificada()).thenReturn(false);

        FiltroPorNivelVerificacion filtroNivel = new FiltroPorNivelVerificacion(true);
        List<Muestra> muestras = Arrays.asList(muestra1, muestra2);

        List<Muestra> resultado = filtroNivel.filtrar(muestras);

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(muestra1));
    }

    @Test
    public void testTemplateMethodEsinalteraableNoSePuedeOverridear() {
        // El método filtrar() está marcado como final, por lo que no se puede sobrescribir
        // Este test verifica que la estructura del template method es inmutable

        // Crear subclase que intenta cambiar el comportamiento
        FiltroAbstracto filtroPersonalizado = new FiltroAbstracto() {
            @Override
            protected boolean cumpleCriterio(Muestra muestra) {
                return true;
            }

            // No se puede override filtrar() porque es final
            // public List<Muestra> filtrar(List<Muestra> muestras) { ... } // Esto causaría error de compilación
        };

        List<Muestra> muestras = Arrays.asList(muestra1, muestra2);
        List<Muestra> resultado = filtroPersonalizado.filtrar(muestras);

        // El template method sigue funcionando correctamente
        assertEquals(2, resultado.size());
    }
}
