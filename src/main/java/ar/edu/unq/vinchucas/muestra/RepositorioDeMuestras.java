package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.aplicacion.IRepositorioDeMuestras;
import ar.edu.unq.vinchucas.filtros.Filtro;
import java.util.ArrayList;
import java.util.List;

public class RepositorioDeMuestras implements IRepositorioDeMuestras {
	private List<Muestra> muestras;

	public RepositorioDeMuestras() {
		muestras = new ArrayList<>();
	}

	@Override
	public void agregarMuestra(Muestra muestra) {
		muestras.add(muestra);
	}

	@Override
	public List<Muestra> getMuestras() {
		return muestras;
	}

	// === FUNCIONALIDADES DE FILTRADO ===
	
	@Override
	public List<Muestra> buscarMuestras(Filtro filtro) {
		return filtro.filtrar(getMuestras());
	}

	@Override
	public List<Muestra> buscarMuestras(List<Filtro> filtros) {
		List<Muestra> resultado = getMuestras();
		for (Filtro filtro : filtros) {
			resultado = filtro.filtrar(resultado);
		}
		return resultado;
	}

	@Override
	public List<Muestra> getMuestrasVerificadas() {
		return muestras.stream()
				.filter(Muestra::estaVerificada)
				.toList();
	}

	@Override
	public List<Muestra> getMuestrasNoVerificadas() {
		return muestras.stream()
				.filter(muestra -> !muestra.estaVerificada())
				.toList();
	}

	// === ESTADÍSTICAS ===
	
	@Override
	public int cantidadMuestras() {
		return muestras.size();
	}

	@Override
	public int cantidadMuestrasVerificadas() {
		return (int) muestras.stream()
				.filter(Muestra::estaVerificada)
				.count();
	}
}