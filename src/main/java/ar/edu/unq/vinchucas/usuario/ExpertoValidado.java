package ar.edu.unq.vinchucas.usuario;

public class ExpertoValidado extends NivelExperto {
    @Override
    public NivelUsuario evaluarCambioDeNivel(Usuario usuario) {
        if (!usuario.cumpleRequisitosParaSerExperto()) {
            return new NivelBasico();
        }
        return this;
    }
} 