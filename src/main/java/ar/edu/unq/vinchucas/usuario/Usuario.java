package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.aplicacion.Aplicacion;
import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import ar.edu.unq.vinchucas.muestra.RepositorioDeOpiniones;

import java.util.List;

public class Usuario {
	private final String nombreUsuario;
	private String contraseña;
	private INivelDeUsuario nivel;
	private final RepositorioDeMuestras muestras;
	private final RepositorioDeOpiniones opiniones;
	
	public Usuario(String nombre, String contraseña, RepositorioDeMuestras muestras, RepositorioDeOpiniones opiniones) {
		this.nombreUsuario = nombre;
		this.contraseña = contraseña;
		this.nivel = new NivelBasico();
		this.muestras = muestras;
		this.opiniones = opiniones;
	}
	
	public void opinar(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
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

	public void cambiarContraseña(String contraseñaActual, String nuevaContraseña) throws SistemaDeExcepciones {
	    if (!this.contraseña.equals(contraseñaActual)) {
	        throw new SistemaDeExcepciones("Contraseña actual incorrecta");
	    }
	    // Aca podriamso poner validaciones para requisitos de contraseña o algo por el estilo.
	    this.contraseña = nuevaContraseña;
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

	public INivelDeUsuario getNivel() {
		return this.nivel;
	}
	
	public void setNivel(INivelDeUsuario nivelUsuario) {
		this.nivel = nivelUsuario;
	}

	public List<Muestra> getMuestrasEnviadas() {
		return muestras.getMuestras();
	}
	
	public List<Opinion> getOpinionesEnviadas() {
		return opiniones.getOpiniones();
	}
	
	public boolean esNivelBasico() {
		return this.getNivel().esNivelBasico();
	}
	
	public boolean esNivelExperto() {
		return this.getNivel().esNivelExperto();
	}

	public String getContraseña() {
		return this.contraseña;
	}
}
