package ar.edu.unq.vinchucas.usuario;

public class NivelBasico extends NivelDeUsuario {

	@Override
	public boolean puedeVerificar() {
		return false;
	}

	@Override
	public String getNombreNivel() {
		return "Nivel Basico";
	}
	
	@Override
	public boolean esNivelBasico() {
		return true;
	}
}
