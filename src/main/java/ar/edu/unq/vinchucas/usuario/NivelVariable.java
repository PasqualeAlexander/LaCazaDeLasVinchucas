package ar.edu.unq.vinchucas.usuario;

import java.time.LocalDate;

public abstract class NivelVariable implements NivelDeUsuario {

	@Override
	public abstract boolean puedeVerificar();

	@Override
	public void actualizarNivel(Usuario usuario) {
		if (this.cumpleRequisitosParaSerExperto(usuario)) {
			usuario.setNivel(new NivelExperto());
		} else {
			usuario.setNivel(new NivelBasico());
		}
	}

	public boolean cumpleRequisitosParaSerExperto(Usuario usuario) {
		return this.cumpleCondicionesDeMuestras(usuario) && this.cumpleCondicionDeRevisones(usuario);
	}

	private boolean cumpleCondicionDeRevisones(Usuario usuario) {
		LocalDate fechaLimite = LocalDate.now().minusDays(30);
		long opinionesUltimos30Dias = usuario.getOpinionesEnviadas().stream()
				.filter(opinion -> !opinion.getFecha().isBefore(fechaLimite)).count();
		return opinionesUltimos30Dias >= 20;
	}

	private boolean cumpleCondicionesDeMuestras(Usuario usuario) {
		LocalDate fechaLimite = LocalDate.now().minusDays(30);
		long muestrasUltimos30Dias = usuario.getMuestrasEnviadas().stream()
				.filter(muestra -> !muestra.getFechaCreacion().isBefore(fechaLimite)).count();
		return muestrasUltimos30Dias >= 20;
	}
}
