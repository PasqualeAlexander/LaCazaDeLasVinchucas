package ar.edu.unq.vinchucas;

import java.util.List;

public class Usuario {
	private final String nombreUsuario;
	private String contraseña;
	private Programa programa;
	private NivelDeUsuario nivel;
	private List<Opinion> opinionesEnviadas;
	private List<Muestra> muestrasEnviadas;
	
	public Usuario(String nombre, String contraseña) {
		this.nombreUsuario=nombre;
		this.contraseña=contraseña;
		this.programa = null;
	}
	
	public void opinar(Muestra muestra, Opinion opinion) {
		muestra.agregarOpinion(opinion);
	}
	
	public void enviarMuestra(Muestra muestraAEnviar) {
		Programa.agregarNuevaMuestra(muestraAEnviar);
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public Programa getPrograma() {
		return programa;
	}

	public GestorDeCategoria getCategoria() {
		return categoria;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}
	
	public void actualizarNivel() {
		nivel.actualizarNivel(null);
	}

	public void setNivel(NivelDeUsuario nivelUsuario) {
		this.nivel=nivelUsuario;
	}

	public List<Muestra> getMuestrasEnviadas() {
		return muestrasEnviadas;;
	}
	
	public List<Opinion> getOpinionesEnviadas() {
		return opinionesEnviadas;;
	}
}
