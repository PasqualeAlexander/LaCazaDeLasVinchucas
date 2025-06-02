package ar.edu.unq.vinchucas.usuario;

public interface NivelDeUsuario {

	boolean puedeVerificar();

	void actualizarNivel(Usuario usuario);

	String getNombreNivel();
}
