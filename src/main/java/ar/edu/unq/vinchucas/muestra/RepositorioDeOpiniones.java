package ar.edu.unq.vinchucas.muestra;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;

public class RepositorioDeOpiniones {
	private List<Opinion> opiniones; 


	public RepositorioDeOpiniones() {
		opiniones = new ArrayList<>();
	}

	public void agregarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
		muestra.agregarOpinion(opinion);
		opiniones.add(opinion);
	}

	public List<Opinion> getOpiniones() {
		return opiniones;
	}
}
