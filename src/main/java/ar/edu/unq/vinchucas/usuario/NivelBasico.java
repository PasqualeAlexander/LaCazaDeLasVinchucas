package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;

public class NivelBasico implements NivelUsuario {
    @Override
    public void opinar(Muestra muestra, Opinion opinion) {
        muestra.agregarOpinion(opinion);
    }

    @Override
    public boolean puedeVerificar(Muestra muestra) {
        return false;
    }

    @Override
    public NivelUsuario evaluarCambioDeNivel(Usuario usuario) {
        if (usuario.cumpleRequisitosParaSerExperto()) {
            return new NivelExperto();
        }
        return this;
    }
} 