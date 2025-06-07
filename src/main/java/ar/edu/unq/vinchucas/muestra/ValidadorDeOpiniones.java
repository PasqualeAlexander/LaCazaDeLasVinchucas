package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;
import java.util.List;

public class ValidadorDeOpiniones {

	public ValidadorDeOpiniones() {

	}

	public void validarQueUsuarioPuedeOpinar(Usuario usuario, List<Opinion> opiniones, boolean correspondeVerificar)
			throws SistemaDeExcepciones {
		if (correspondeVerificar) {
			throw new SistemaDeExcepciones("La muestra ya está verificada");
		}

		if (!puedeOpinarElUsuario(usuario, opiniones)) {
			throw new SistemaDeExcepciones("El usuario ya ha opinado sobre esta muestra");
		}
	}

	public boolean puedeOpinarElUsuario(Usuario usuario, List<Opinion> opiniones) {
		return !yaOpinoElUsuario(usuario, opiniones);
	}
	
	private boolean yaOpinoElUsuario(Usuario usuario, List<Opinion> opiniones) {
	    return opiniones.stream()
	            .anyMatch(opinion -> opinion.getUsuario().equals(usuario));
	}

}
