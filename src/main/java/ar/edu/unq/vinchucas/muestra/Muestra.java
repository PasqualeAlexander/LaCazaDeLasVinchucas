package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.vinchucas.usuario.Usuario;

public class Muestra {
	private final String foto;
	private final String ubicacion;
	private final LocalDate fechaCreacion;
	private final Usuario usuario; // Cambio Usuario por su nombre para no sobrecargar
	private final SistemaDeOpiniones sistemaDeOpiniones;
	private EstadoMuestra estado;
	// Se saco la variable que guarda resultado ya que la misma se calcula en el
	// momento que se llama.

	public Muestra(String foto, String ubicacion, Usuario usuario, Opinion opinion) {
		this.foto = foto;
		this.ubicacion = ubicacion;
		this.usuario = usuario;
		this.fechaCreacion = LocalDate.now();
		this.sistemaDeOpiniones = new SistemaDeOpiniones();
		this.agregarOpinion(opinion);
		this.estado = EstadoMuestra.NO_VERIFICADA;
		;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public EstadoMuestra getEstado() {
		return estado;
	}

	public String getNombreUsuario() {
		return usuario.getNombreUsuario();
	}

	public void agregarOpinion(Opinion opinion) {
			sistemaDeOpiniones.agregarOpinion(opinion);
			verificarEstado();
		}


	private void verificarEstado() {
		if (this.correspondeVerificar()) {
			estado = EstadoMuestra.VERIFICADA;
		}
	}
	
	private boolean correspondeVerificar() {
		return sistemaDeOpiniones.correspondeVerificar();
	}

	public boolean estaVerificada() {
		return estado==EstadoMuestra.VERIFICADA;
	}

	public TipoDeOpinion getResultado() {
		return sistemaDeOpiniones.getResultado();
	}

	public LocalDate getFechaUltimaVotacion() { 
		return null;
	}

}
