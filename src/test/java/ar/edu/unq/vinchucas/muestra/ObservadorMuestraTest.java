package ar.edu.unq.vinchucas.muestra;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.organizacion.Organizacion;
import ar.edu.unq.vinchucas.organizacion.OrganizacionImpl;
import ar.edu.unq.vinchucas.organizacion.TipoOrganizacion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import ar.edu.unq.vinchucas.zonas.FuncionalidadExterna;
import ar.edu.unq.vinchucas.zonas.Ubicacion;
import ar.edu.unq.vinchucas.zonas.ZonaDeCobertura;

/**
 * Test que verifica el comportamiento del patrón Observer cuando 
 * una muestra se verifica y debe notificar a las zonas suscritas
 */
public class ObservadorMuestraTest {

    private Muestra muestra;
    private Usuario usuarioBasico;
    private Usuario expertoValidado1;
    private Usuario expertoValidado2;
    private ZonaDeCobertura zona;
    private Organizacion organizacion;
    private FuncionalidadExterna funcionalidadMock;

    @BeforeEach
    public void setUp() throws SistemaDeExcepciones {
        // Config usuarios
        usuarioBasico = mock(Usuario.class);
        expertoValidado1 = mock(Usuario.class);
        expertoValidado2 = mock(Usuario.class);
        
        when(usuarioBasico.esNivelExperto()).thenReturn(false);
        when(expertoValidado1.esNivelExperto()).thenReturn(true);
        when(expertoValidado2.esNivelExperto()).thenReturn(true);
        
        // config muestrra
        muestra = new Muestra("foto.jpg", "-34.6037,-58.3816", usuarioBasico, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        // config de zona y organización
        funcionalidadMock = mock(FuncionalidadExterna.class);
        Ubicacion ubicacion = new Ubicacion(-34.6037, -58.3816);
        zona = new ZonaDeCobertura("Zona Test", ubicacion, 10.0, funcionalidadMock);
        organizacion = new OrganizacionImpl("Hospital Test", ubicacion, TipoOrganizacion.SALUD, 100);
        
        // Suscribir organización a la zona
        zona.suscribirOrganizacion(organizacion);
        
        // registrar muestra en zona para que la zona se suscriba como observador
        zona.registrarMuestra(muestra);
    }

    @Test
    public void testMuestraNotificaAlVerificarse() throws SistemaDeExcepciones {
        // Verificar que la muestra inicialmente no está verificada
        assertFalse(muestra.estaVerificada());
        
        // Verificar que la zona está suscrita como observador
        assertTrue(zona.getMuestrasReportadas().contains(muestra));
        
        // Agg dos opiniones de expertos para verificar la muestra
        Opinion opinion1 = new Opinion(expertoValidado1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(expertoValidado2, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        muestra.agregarOpinion(opinion1);
        muestra.agregarOpinion(opinion2);
        
        // Verificar que la muestra se verificó
        assertTrue(muestra.estaVerificada());
        
        // Verificar que se notifico a la zona (que es observador) sobre la verificación
        verify(funcionalidadMock).muestraVerificada(organizacion, zona, muestra);
    }

    @Test
    public void testMultiplesObservadoresRecibenenNotificacion() throws SistemaDeExcepciones {
        // config segunda zona y organización
        FuncionalidadExterna funcionalidad2Mock = mock(FuncionalidadExterna.class);
        Ubicacion ubicacion2 = new Ubicacion(-34.6037, -58.3816);
        ZonaDeCobertura zona2 = new ZonaDeCobertura("Zona Test 2", ubicacion2, 10.0, funcionalidad2Mock);
        Organizacion organizacion2 = new OrganizacionImpl("Escuela Test", ubicacion2, TipoOrganizacion.EDUCATIVA, 50);
        
        zona2.suscribirOrganizacion(organizacion2);
        zona2.registrarMuestra(muestra);
        
        // verificar la muestra
        Opinion opinion1 = new Opinion(expertoValidado1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(expertoValidado2, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        muestra.agregarOpinion(opinion1);
        muestra.agregarOpinion(opinion2);
        
        // Verificar que ambas zonas fueron notificadas
        verify(funcionalidadMock).muestraVerificada(organizacion, zona, muestra);
        verify(funcionalidad2Mock).muestraVerificada(organizacion2, zona2, muestra);
    }

    @Test
    public void testNoSeNotificaSiMuestraNoSeVerifica() throws SistemaDeExcepciones {
        // Agg solo una opinión de experto (no se verifica)
        Opinion opinion1 = new Opinion(expertoValidado1, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestra.agregarOpinion(opinion1);
        
        // verificar que la muestra NO se verificó
        assertFalse(muestra.estaVerificada());
        
        // verificar que NO se notificó sobre verificación
        verify(funcionalidadMock, never()).muestraVerificada(any(), any(), any());
    }

    @Test
    public void testRemoverObservadorNormalmenteNoReciebeNotificacion() throws SistemaDeExcepciones {
        // rmover la zona como observador
        muestra.removerObservador(zona);
        
        // verificar la muestra
        Opinion opinion1 = new Opinion(expertoValidado1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(expertoValidado2, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        muestra.agregarOpinion(opinion1);
        muestra.agregarOpinion(opinion2);
        
        // Verificar que la muestra se verificó pero la zona no fue notificada
        assertTrue(muestra.estaVerificada());
        verify(funcionalidadMock, never()).muestraVerificada(any(), any(), any());
    }

    @Test
    public void testOpinionesBasicasNoGeneranNotificacionDeVerificacion() throws SistemaDeExcepciones {
        // Config usuarios básicos adicionales
        Usuario basico2 = mock(Usuario.class);
        Usuario basico3 = mock(Usuario.class);
        when(basico2.esNivelExperto()).thenReturn(false);
        when(basico3.esNivelExperto()).thenReturn(false);
        
        // Ag varias opiniones básicas
        Opinion opinion1 = new Opinion(basico2, TipoDeOpinion.CHINCHE_FOLIADA);
        Opinion opinion2 = new Opinion(basico3, TipoDeOpinion.VINCHUCA_SORDIDA);
        
        muestra.agregarOpinion(opinion1);
        muestra.agregarOpinion(opinion2);
        
        // Verificar que la muestra NO se verificó
        assertFalse(muestra.estaVerificada());
        
        // Verificar que NO se notificó sobre verificación
        verify(funcionalidadMock, never()).muestraVerificada(any(), any(), any());
    }
}
