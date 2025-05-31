package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;

public abstract class NivelExperto implements NivelUsuario {
    @Override
    public void opinar(Muestra muestra, Opinion opinion) {
        if (puedeVerificar(muestra)) {
            muestra.agregarOpinion(opinion);
        }
    }

    @Override
    public boolean puedeVerificar(Muestra muestra) {
        return true;
    }

    // Cada subclase implementará su propia lógica de evaluación
    @Override
    public abstract NivelUsuario evaluarCambioDeNivel(Usuario usuario);
} 