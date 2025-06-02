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
	private final List<Opinion> opiniones;
	private EstadoMuestra estado;
	// Se saco la variable que guarda resultado ya que la misma se calcula en el
	// momento que se llama.

	public Muestra(String foto, String ubicacion, Usuario usuario, Opinion opinion) {
		this.foto = foto;
		this.ubicacion = ubicacion;
		this.usuario = usuario;
		this.fechaCreacion = LocalDate.now();
		this.opiniones = new ArrayList<>();
		this.estado = EstadoMuestra.NO_VERIFICADA;
		this.agregarOpinion(opinion);
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
		if (this.admiteOpiniones()) {
			opiniones.add(opinion);
			verificarEstado();
		}
	}

	private boolean admiteOpiniones() {

		return true;
	}

	private void verificarEstado() {
		// Lógica para verificar el estado según las opiniones
		// Se implementará más adelante
	}

	public boolean estaVerificada() {
		// TODO Auto-generated method stub
		return false;
	}

	public TipoDeOpinion getResultado() {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalDate getFechaUltimaVotacion() { // TODO: Implement
		// TODO Auto-generated method stub
		return null;
	}

	public TipoDeOpinion resultadoActual() {
		java.util.Map<TipoDeOpinion, Integer> conteo = new java.util.HashMap<>();

		for (Opinion opinion : opiniones) {
			TipoDeOpinion tipo = opinion.getTipoDeOpinion();
			conteo.put(tipo, conteo.getOrDefault(tipo, 0) + 1);
		}

		TipoDeOpinion masVotado = null;
		int maxVotos = 0;

		for (java.util.Map.Entry<TipoDeOpinion, Integer> entrada : conteo.entrySet()) {
			if (entrada.getValue() > maxVotos) {
				masVotado = entrada.getKey();
				maxVotos = entrada.getValue();
			}
		}
		return masVotado;
	}
}
