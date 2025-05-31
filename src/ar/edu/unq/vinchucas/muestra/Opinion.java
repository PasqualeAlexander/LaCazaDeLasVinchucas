package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class Opinion {
	private Usuario usuario;
	private TipoDeOpinion opinion;
	private LocalDate fecha;
	
	public Opinion(Usuario usuario, TipoDeOpinion opinion) {
		this.usuario=usuario;
		this.opinion=opinion;
		fecha= LocalDate.now();
	}

	public LocalDate getFecha() {
		return fecha;
	} 
}
