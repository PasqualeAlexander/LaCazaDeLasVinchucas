package ar.edu.unq.vinchucas.usuario;

public interface INivelDeUsuario {

	boolean puedeVerificar();

	void actualizarNivel(Usuario usuario);

	String getNombreNivel();

	boolean esNivelBasico();

	boolean esNivelExperto();
}
