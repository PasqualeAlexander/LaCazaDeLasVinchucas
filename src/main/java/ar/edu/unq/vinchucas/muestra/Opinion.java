package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class Opinion {
	private Usuario usuario;
	private TipoDeOpinion tipo;
	private LocalDate fecha;
	private boolean eraExpertoAlOpinar;


	public Opinion(Usuario usuario, TipoDeOpinion opinion) {
		this.usuario = usuario;
		this.tipo = opinion;
		fecha = LocalDate.now();
		this.eraExpertoAlOpinar = usuario.esNivelExperto();

	}

	public LocalDate getFecha() {
		return fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public TipoDeOpinion getTipoDeOpinion() {
		return tipo;
	}
	
	public boolean eraExpertoAlOpinar() {
		return eraExpertoAlOpinar;
	}
}
