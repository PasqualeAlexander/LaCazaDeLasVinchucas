package ar.edu.unq.vinchucas;

public class NivelInvestigador implements NivelDeUsuario {

	@Override
	public boolean puedeVerificar() {
		return true;
	}

	@Override
	public void actualizarNivel(Usuario usuario) {
	}
	
}
