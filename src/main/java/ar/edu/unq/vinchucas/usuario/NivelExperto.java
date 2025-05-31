package ar.edu.unq.vinchucas.usuario;

public class NivelExperto extends NivelVariable {

	@Override
	public boolean puedeVerificar() {
		return true;
	}
	
	@Override
	public String getNombreNivel() {
		return "Nivel Experto";
	}
}
