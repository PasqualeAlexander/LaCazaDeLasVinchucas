package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.RepositorioDeMuestras;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
	private final String nombreUsuario;
	private String contraseña;
	private final RepositorioDeMuestras repositorio;
	private NivelDeUsuario nivel;
	private List<Opinion> opinionesEnviadas;
	private List<Muestra> muestrasEnviadas;
	
	public Usuario(String nombre, String contraseña, RepositorioDeMuestras repositorio) {
		this.nombreUsuario = nombre;
		this.contraseña = contraseña;
		this.repositorio = repositorio;
		this.opinionesEnviadas = new ArrayList<>();
		this.muestrasEnviadas = new ArrayList<>();
	}
	
	public void opinar(Muestra muestra, Opinion opinion) {
		muestra.agregarOpinion(opinion);
	}
	
	public void enviarMuestra(Muestra muestraAEnviar) {
		repositorio.agregarMuestra(muestraAEnviar);
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public RepositorioDeMuestras getRepositorio() {
		return repositorio;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	public void actualizarNivel() {
		nivel.actualizarNivel(null);
	}

	public void setNivel(NivelDeUsuario nivelUsuario) {
		this.nivel = nivelUsuario;
	}

	public List<Muestra> getMuestrasEnviadas() {
		return muestrasEnviadas;
	}
	
	public List<Opinion> getOpinionesEnviadas() {
		return opinionesEnviadas;
	}
}
