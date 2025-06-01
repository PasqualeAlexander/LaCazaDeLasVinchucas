package ar.edu.unq.vinchucas.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;

public class NivelBasicoTest {
	private RepositorioDeMuestras muestrasMock;
	private RepositorioDeOpiniones opinionesMock;
	
	@BeforeEach
	public void setUp() {
		muestrasMock = mock(RepositorioDeMuestras.class);
		opinionesMock = mock(RepositorioDeOpiniones.class);
	}

    @Test
    public void testPuedeVerificarDevuelveFalse() {
        NivelBasico nivel = new NivelBasico();
        assertFalse(nivel.puedeVerificar());
    }

    @Test
    public void testActualizarNivelCambiaANivelExpertoCuandoCumpleRequisitos() {
        NivelBasico nivel = new NivelBasico();
        Usuario usuario = new Usuario("usuarioTest", "password", muestrasMock, opinionesMock);
        usuario.setNivel(nivel);
        
        List<Muestra> muestras = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Muestra muestra = new Muestra("foto" + i, "loc" + i, usuario) {
            };
            usuario.enviarMuestra(muestra);
        }

        List<Opinion> opiniones = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            opiniones.add(new Opinion(usuario, TipoDeOpinion.VINCHUCA_INFESTANS));
        }
        usuario.getMuestrasEnviadas().addAll(muestras); // Al resultanted el get le agrega que envia las 20 muestras
        usuario.getOpinionesEnviadas().addAll(opiniones); // Al resultanted el get le agrega que envia las 20 opiniones
        nivel.actualizarNivel(usuario);

        assertEquals("Nivel Experto", usuario.getNivel().getNombreNivel());
    }

    @Test
    public void testActualizarNivelPermaneceNivelBasicoCuandoNoCumpleRequisitos() {
        NivelBasico nivel = new NivelBasico();
        Usuario usuario = new Usuario("usuarioTest", "password", null, null);
        usuario.setNivel(nivel);

        List<Muestra> muestras = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Muestra muestra = new Muestra("foto" + i, "loc" + i, usuario) {
            };
            usuario.enviarMuestra(muestra);
        }

        List<Opinion> opiniones = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            usuario.opinar(muestraMock, new Opinion(usuario, TipoDeOpinion.VINCHUCA_INFESTANS));
        }

        usuario.getMuestrasEnviadas().addAll(muestras);
        usuario.getOpinionesEnviadas().addAll(opiniones);

        nivel.actualizarNivel(usuario);

        assertEquals("Nivel Basico", usuario.getNivel().getNombreNivel()); // getNombreNivel es un metodo nuevo para no utilizar InstanceOF o isKindOf para comparar si es de la clase, simplemente de esta forma comparamos por strings
    }
}
