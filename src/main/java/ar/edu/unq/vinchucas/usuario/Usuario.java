package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;

import java.util.List;

public class Usuario {
	private final String nombreUsuario;
	private String contraseña;
	private NivelDeUsuario nivel;
	private final RepositorioDeMuestras muestras;
	private final RepositorioDeOpiniones opiniones;
	
	public Usuario(String nombre, String contraseña, RepositorioDeMuestras muestras, RepositorioDeOpiniones opiniones) {
		this.nombreUsuario = nombre;
		this.contraseña = contraseña;
		this.nivel = new NivelBasico();
		this.muestras = muestras;
		this.opiniones = opiniones;
	}
	
	public void opinar(Muestra muestra, Opinion opinion) {
		opiniones.agregarOpinion(muestra, opinion);
		nivel.actualizarNivel(this);
	}
	
	public void enviarMuestra(Muestra muestraAEnviar) {
		muestras.agregarMuestra(muestraAEnviar);
		nivel.actualizarNivel(this);
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public RepositorioDeMuestras getRepositorio() {
		return muestras;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	public void actualizarNivel() {
		nivel.actualizarNivel(this);
	}

	public NivelDeUsuario getNivel() {
		return this.nivel;
	}
	
	public void setNivel(NivelDeUsuario nivelUsuario) {
		this.nivel = nivelUsuario;
	}

	public List<Muestra> getMuestrasEnviadas() {
		return muestras.getMuestras();
	}
	
	public List<Opinion> getOpinionesEnviadas() {
		return opiniones.getOpiniones();
	}
}
