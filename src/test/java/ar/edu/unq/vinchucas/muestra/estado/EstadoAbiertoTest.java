package ar.edu.unq.vinchucas.muestra.estado;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.SistemaDeOpiniones;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class EstadoAbiertoTest {

    private EstadoAbierto estadoAbierto;
    private Opinion opinionInicial;
    private Usuario usuarioBasico;
    private Usuario usuarioExperto;
    private Muestra muestra;
    private SistemaDeOpiniones sistemaOpiniones;

    @BeforeEach
    public void setUp() {
        // Configurar mocks
        usuarioBasico = mock(Usuario.class);
        usuarioExperto = mock(Usuario.class);
        muestra = mock(Muestra.class);
        sistemaOpiniones = mock(SistemaDeOpiniones.class);
        
        // Configurar comportamientos
        when(usuarioBasico.esNivelExperto()).thenReturn(false);
        when(usuarioExperto.esNivelExperto()).thenReturn(true);
        when(muestra.getUsuario()).thenReturn(usuarioBasico);
        
        // Crear opinión inicial
        opinionInicial = new Opinion(usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        estadoAbierto = new EstadoAbierto(opinionInicial);
    }

    @Test
    public void testCreacionEstadoAbierto() {
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, estadoAbierto.getResultado());
        assertFalse(estadoAbierto.esVerificada());
    }

    @Test
    public void testUsuarioBasicoPuedeOpinar() {
        Usuario otroUsuario = mock(Usuario.class);
        
        // En EstadoAbierto, cualquier usuario puede opinar (las validaciones comunes se hacen en otro lugar)
        assertTrue(estadoAbierto.puedeOpinarUsuario(otroUsuario, muestra));
    }

    @Test
    public void testCreadorNoPuedeOpinar() {
        // Esta validación ahora se hace en validarOpinion() del template method
        // puedeOpinarUsuario() en EstadoAbierto siempre retorna true
        assertTrue(estadoAbierto.puedeOpinarUsuario(usuarioBasico, muestra));
    }

    @Test
    public void testUsuarioQueYaOpinoNoPuedeOpinarDeNuevo() {
        Usuario otroUsuario = mock(Usuario.class);
        
        // Esta validación ahora se hace en validarOpinion() del template method
        // puedeOpinarUsuario() en EstadoAbierto siempre retorna true
        assertTrue(estadoAbierto.puedeOpinarUsuario(otroUsuario, muestra));
    }

    @Test
    public void testOpinionExpertoCambiaEstadoAExperto() throws SistemaDeExcepciones {
        Opinion opinionExperto = new Opinion(usuarioExperto, TipoDeOpinion.CHINCHE_FOLIADA);
        
        estadoAbierto.agregarOpinion(muestra, opinionExperto);
        
        // Verificar que se cambió el estado
        verify(muestra).setEstado(any(EstadoExperto.class));
        // Verificar que se almacenó la opinión
        verify(muestra).almacenarOpinion(opinionExperto);
    }

    @Test
    public void testOpinionBasicaActualizaResultado() throws SistemaDeExcepciones {
        Usuario otroBasico = mock(Usuario.class);
        when(otroBasico.esNivelExperto()).thenReturn(false);
        
        Opinion opinionBasica = new Opinion(otroBasico, TipoDeOpinion.CHINCHE_FOLIADA);
        
        // Estado inicial: VINCHUCA_INFESTANS (1 voto)
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, estadoAbierto.getResultado());
        
        // Agregar voto por CHINCHE_FOLIADA
        estadoAbierto.agregarOpinion(muestra, opinionBasica);
        
        // Ahora hay empate, debería ser NO_DEFINIDO
        assertEquals(TipoDeOpinion.NO_DEFINIDO, estadoAbierto.getResultado());
        
        // Verificar que se almacenó la opinión
        verify(muestra).almacenarOpinion(opinionBasica);
    }

    @Test
    public void testUsuarioNoPuedeOpinarLanzaExcepcion() {
        Opinion opinion = new Opinion(usuarioBasico, TipoDeOpinion.CHINCHE_FOLIADA);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            estadoAbierto.agregarOpinion(muestra, opinion);
        });
    }

    @Test
    public void testCreadorNoPuedeOpinarConTemplateMethod() {
        // Ahora probamos el comportamiento completo a través del template method
        Opinion opinion = new Opinion(usuarioBasico, TipoDeOpinion.CHINCHE_FOLIADA);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            estadoAbierto.agregarOpinion(muestra, opinion);
        });
    }

    @Test
    public void testUsuarioQueYaOpinoNoPuedeOpinarConTemplateMethod() throws SistemaDeExcepciones {
        Usuario otroUsuario = mock(Usuario.class);
        when(otroUsuario.esNivelExperto()).thenReturn(false);
        
        Opinion opinion = new Opinion(otroUsuario, TipoDeOpinion.CHINCHE_FOLIADA);
        
        // Configurar el mock para que simule que el usuario ya opinó
        doThrow(new SistemaDeExcepciones("El usuario ya ha opinado sobre esta muestra"))
            .when(muestra).almacenarOpinion(opinion);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            estadoAbierto.agregarOpinion(muestra, opinion);
        });
    }

    @Test
    public void testResultadoConMayoriaDeVotos() throws SistemaDeExcepciones {
        // Agregar dos votos más por VINCHUCA_INFESTANS
        Usuario usuario1 = mock(Usuario.class);
        Usuario usuario2 = mock(Usuario.class);
        
        when(usuario1.esNivelExperto()).thenReturn(false);
        when(usuario2.esNivelExperto()).thenReturn(false);
        
        Opinion opinion1 = new Opinion(usuario1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(usuario2, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        estadoAbierto.agregarOpinion(muestra, opinion1);
        estadoAbierto.agregarOpinion(muestra, opinion2);
        
        // Debería mantenerse VINCHUCA_INFESTANS con 3 votos
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, estadoAbierto.getResultado());
    }
}
