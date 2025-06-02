package ar.edu.unq.vinchucas.muestra;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeMuestras {
	private List<Muestra> muestras;

	public RepositorioDeMuestras() {
		muestras = new ArrayList<>();
	}

	public void agregarMuestra(Muestra muestra) {
		muestras.add(muestra);
	}

	public List<Muestra> getMuestras() {
		return muestras;
	}
	// Podemos agregar más métodos según necesitemos, como:
	// Muestra getMuestra(String id);
	// void eliminarMuestra(String id);
	// List<Muestra> buscarMuestras(Filtro filtro);
}