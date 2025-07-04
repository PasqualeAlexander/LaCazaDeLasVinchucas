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

public class EstadoExpertoTest {

    private EstadoExperto estadoExperto;
    private Opinion opinionInicial;
    private Usuario usuarioBasico;
    private Usuario usuarioExperto1;
    private Usuario usuarioExperto2;
    private Muestra muestra;
    private SistemaDeOpiniones sistemaOpiniones;

    @BeforeEach
    public void setUp() {
        // Configmocks
        usuarioBasico = mock(Usuario.class);
        usuarioExperto1 = mock(Usuario.class);
        usuarioExperto2 = mock(Usuario.class);
        muestra = mock(Muestra.class);
        sistemaOpiniones = mock(SistemaDeOpiniones.class);
        
        // Config comportamientos
        when(usuarioBasico.esNivelExperto()).thenReturn(false);
        when(usuarioExperto1.esNivelExperto()).thenReturn(true);
        when(usuarioExperto2.esNivelExperto()).thenReturn(true);
        when(muestra.getUsuario()).thenReturn(usuarioBasico);
        
        // Crear opinión inicial de experto
        opinionInicial = new Opinion(usuarioExperto1, TipoDeOpinion.VINCHUCA_INFESTANS);
        estadoExperto = new EstadoExperto(opinionInicial);
    }

    @Test
    public void testCreacionEstadoExperto() {
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, estadoExperto.getResultado());
        assertFalse(estadoExperto.esVerificada());
    }

    @Test
    public void testSoloExpertosPuedenOpinar() {
        assertTrue(estadoExperto.puedeOpinarUsuario(usuarioExperto2, muestra));
        assertFalse(estadoExperto.puedeOpinarUsuario(usuarioBasico, muestra));
    }

    @Test
    public void testCreadorExpertoNoPuedeOpinar() {
        Usuario expertoPropietario = mock(Usuario.class);
        when(expertoPropietario.esNivelExperto()).thenReturn(true);
        when(muestra.getUsuario()).thenReturn(expertoPropietario);
        
        // puedeOpinarUsuario() solo verifica si es experto, la validación de creador se hace en EstadoAbstracto
        assertTrue(estadoExperto.puedeOpinarUsuario(expertoPropietario, muestra));
    }

    @Test
    public void testExpertoQueYaOpinoNoPuedeOpinarDeNuevo() {
        // Esta validación ahora se hace en SistemaDeOpiniones, no aquí
        // puedeOpinarUsuario() solo verifica si es experto
        assertTrue(estadoExperto.puedeOpinarUsuario(usuarioExperto2, muestra));
    }

    @Test
    public void testSegundoExpertoConMismaOpinionVerificaMuestra() throws SistemaDeExcepciones {
        Opinion segundaOpinion = new Opinion(usuarioExperto2, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        estadoExperto.agregarOpinion(muestra, segundaOpinion);
        
        // Verificar que se cambió el estado a verificada
        verify(muestra).setEstado(any(EstadoVerificada.class));
    }

    @Test
    public void testSegundoExpertoConOpinionDiferenteGeneraNoDefinido() throws SistemaDeExcepciones {
        Opinion segundaOpinion = new Opinion(usuarioExperto2, TipoDeOpinion.CHINCHE_FOLIADA);
        
        estadoExperto.agregarOpinion(muestra, segundaOpinion);
        
        // El resultado debería cambiar a NO_DEFINIDO
        assertEquals(TipoDeOpinion.NO_DEFINIDO, estadoExperto.getResultado());
        // NO debería cambiar a verificada
        verify(muestra, never()).setEstado(any(EstadoVerificada.class));
    }

    @Test
    public void testUsuarioBasicoNoPuedeOpinarLanzaExcepcion() {
        Opinion opinionBasica = new Opinion(usuarioBasico, TipoDeOpinion.CHINCHE_FOLIADA);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            estadoExperto.agregarOpinion(muestra, opinionBasica);
        });
    }

    @Test
    public void testExpertoQueYaOpinoNoPuedeOpinarLanzaExcepcion() throws SistemaDeExcepciones {
        Opinion opinion = new Opinion(usuarioExperto1, TipoDeOpinion.CHINCHE_FOLIADA);
        
        // Configurar el mock para que simule que el usuario ya opinó
        doThrow(new SistemaDeExcepciones("El usuario ya ha opinado sobre esta muestra"))
            .when(muestra).almacenarOpinion(opinion);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            estadoExperto.agregarOpinion(muestra, opinion);
        });
    }

    @Test
    public void testTercerExpertoConMismaOpinionQueSegundoVerificaMuestra() throws SistemaDeExcepciones {
        // Primer experto ya opinó VINCHUCA_INFESTANS
        // Segundo experto opina CHINCHE_FOLIADA (resultado = NO_DEFINIDO)
        Opinion segundaOpinion = new Opinion(usuarioExperto2, TipoDeOpinion.CHINCHE_FOLIADA);
        estadoExperto.agregarOpinion(muestra, segundaOpinion);
        
        assertEquals(TipoDeOpinion.NO_DEFINIDO, estadoExperto.getResultado());
        
        // Tercer experto opina igual que el primero
        Usuario usuarioExperto3 = mock(Usuario.class);
        when(usuarioExperto3.esNivelExperto()).thenReturn(true);
        
        Opinion terceraOpinion = new Opinion(usuarioExperto3, TipoDeOpinion.VINCHUCA_INFESTANS);
        estadoExperto.agregarOpinion(muestra, terceraOpinion);
        
        // Ahora debería verificar con VINCHUCA_INFESTANS
        verify(muestra).setEstado(any(EstadoVerificada.class));
    }
}
