package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;

public interface NivelUsuario {
    void opinar(Muestra muestra, Opinion opinion);
    boolean puedeVerificar(Muestra muestra);
    NivelUsuario evaluarCambioDeNivel(Usuario usuario);
} 