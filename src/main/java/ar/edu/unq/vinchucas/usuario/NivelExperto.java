package ar.edu.unq.vinchucas.usuario;

public class NivelExperto extends NivelDeUsuario {

	@Override
	public boolean puedeVerificar() {
		return true;
	}
	
	@Override
	public String getNombreNivel() {
		return "Nivel Experto";
	}
	
	@Override
	public boolean esNivelExperto() {
		return true;
	}
}
