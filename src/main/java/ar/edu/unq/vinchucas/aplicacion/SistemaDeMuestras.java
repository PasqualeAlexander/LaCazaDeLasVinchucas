package ar.edu.unq.vinchucas.aplicacion;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.vinchucas.muestra.Muestra;

public class SistemaDeMuestras {
	private List<Muestra> muestras;
	
	public SistemaDeMuestras() {
		muestras = new ArrayList<>();
	}

	public void registrarMuestra(Muestra muestra) {
		muestras.add(muestra);
	}
	
	public List<Muestra> getMuestras(){
		return this.muestras;
	}

}
