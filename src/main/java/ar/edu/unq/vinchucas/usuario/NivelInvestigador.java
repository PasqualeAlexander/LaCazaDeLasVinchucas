package ar.edu.unq.vinchucas.usuario;

public class NivelInvestigador extends NivelDeUsuario {

	@Override
	public boolean puedeVerificar() {
		return true;
	}

	@Override
	public void actualizarNivel(Usuario usuario) {
	}
	
	@Override
	public String getNombreNivel() {
		return "Nivel Investigador";
	}
	
	@Override
	public boolean esNivelExperto() {
		return true;
	}
}
