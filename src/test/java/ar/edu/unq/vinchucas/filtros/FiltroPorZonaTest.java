package ar.edu.unq.vinchucas.filtros;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.FuncionalidadExterna;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FiltroPorZonaTest {

    private FiltroPorZona filtro;
    private ZonaDeCobertura zona;
    private Muestra muestra1;
    private Muestra muestra2;
    private Muestra muestra3;

    @BeforeEach
    public void setUp() {
        // Crear zona de cobertura
        Ubicacion epicentro = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        FuncionalidadExterna funcionalidad = mock(FuncionalidadExterna.class);
        zona = new ZonaDeCobertura("Zona Buenos Aires", epicentro, 10.0, funcionalidad);
        
        filtro = new FiltroPorZona(zona);
        
        // Crear muestras mock
        muestra1 = mock(Muestra.class);
        muestra2 = mock(Muestra.class);
        muestra3 = mock(Muestra.class);
        
        // Configurar ubicaciones
        when(muestra1.getUbicacion()).thenReturn("-34.6037,-58.3816"); // Dentro de la zona (centro)
        when(muestra2.getUbicacion()).thenReturn("-34.6100,-58.3900"); // Dentro de la zona (cerca)
        when(muestra3.getUbicacion()).thenReturn("-31.4201,-64.1888"); // Fuera de la zona (Córdoba)
    }

    @Test
    public void testFiltrarMuestrasEnZona() {
        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        
        List<Muestra> resultado = filtro.filtrar(muestras);
        
        assertEquals(2, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra2));
        assertFalse(resultado.contains(muestra3));
    }

    @Test
    public void testFiltrarListaVacia() {
        List<Muestra> muestrasVacias = List.of();
        
        List<Muestra> resultado = filtro.filtrar(muestrasVacias);
        
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testFiltrarTodasLasMuestrasFueraDeZona() {
        // Todas las muestras están fuera de la zona
        when(muestra1.getUbicacion()).thenReturn("-31.4201,-64.1888"); // Córdoba
        when(muestra2.getUbicacion()).thenReturn("-32.8908,-68.8272"); // Mendoza
        
        List<Muestra> muestras = List.of(muestra1, muestra2);
        
        List<Muestra> resultado = filtro.filtrar(muestras);
        
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testFiltrarTodasLasMuestrasDentroDeZona() {
        // Todas las muestras están dentro de la zona
        when(muestra1.getUbicacion()).thenReturn("-34.6037,-58.3816"); // Centro
        when(muestra2.getUbicacion()).thenReturn("-34.6100,-58.3900"); // Cerca del centro
        when(muestra3.getUbicacion()).thenReturn("-34.6000,-58.3800"); // También cerca
        
        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        
        List<Muestra> resultado = filtro.filtrar(muestras);
        
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains(muestra1));
        assertTrue(resultado.contains(muestra2));
        assertTrue(resultado.contains(muestra3));
    }

    @Test
    public void testFiltrarMuestraConUbicacionInvalida() {
        when(muestra1.getUbicacion()).thenReturn("ubicacion-invalida");
        when(muestra2.getUbicacion()).thenReturn("-34.6037,-58.3816"); // Válida
        
        List<Muestra> muestras = List.of(muestra1, muestra2);
        
        List<Muestra> resultado = filtro.filtrar(muestras);
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(muestra2));
        assertFalse(resultado.contains(muestra1));
    }


    @Test
    public void testFiltrarMuestraJustoFueraDelLimite() {
        // Muestra justo fuera del límite de la zona (>10km del epicentro)
        when(muestra1.getUbicacion()).thenReturn("-34.5000,-58.3816"); // ~11.5km al norte
        
        List<Muestra> muestras = List.of(muestra1);
        
        List<Muestra> resultado = filtro.filtrar(muestras);
        
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void testFiltroConZonaDiferente() {
        // Crear una zona diferente
        Ubicacion epicentroCordoba = new Ubicacion(-31.4201, -64.1888);
        FuncionalidadExterna funcionalidad = mock(FuncionalidadExterna.class);
        ZonaDeCobertura zonaCordoba = new ZonaDeCobertura("Zona Córdoba", epicentroCordoba, 5.0, funcionalidad);
        
        FiltroPorZona filtroCordoba = new FiltroPorZona(zonaCordoba);
        
        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        
        List<Muestra> resultado = filtroCordoba.filtrar(muestras);
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(muestra3)); // Solo la muestra de Córdoba
    }

    @Test
    public void testFiltroPreservaOrdenOriginal() {
        when(muestra1.getUbicacion()).thenReturn("-34.6037,-58.3816"); // Dentro
        when(muestra2.getUbicacion()).thenReturn("-31.4201,-64.1888"); // Fuera
        when(muestra3.getUbicacion()).thenReturn("-34.6100,-58.3900"); // Dentro
        
        List<Muestra> muestras = List.of(muestra1, muestra2, muestra3);
        
        List<Muestra> resultado = filtro.filtrar(muestras);
        
        assertEquals(2, resultado.size());
        assertEquals(muestra1, resultado.get(0)); // Mantiene el orden original
        assertEquals(muestra3, resultado.get(1));
    }
} 