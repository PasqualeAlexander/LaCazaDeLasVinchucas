package ar.edu.unq.vinchucas.muestra;

import java.util.List;

public interface RepositorioDeMuestras {
    void agregarMuestra(Muestra muestra);
    List<Muestra> getMuestras();
    // Podemos agregar más métodos según necesitemos, como:
    // Muestra getMuestra(String id);
    // void eliminarMuestra(String id);
    // List<Muestra> buscarMuestras(Filtro filtro);
} 