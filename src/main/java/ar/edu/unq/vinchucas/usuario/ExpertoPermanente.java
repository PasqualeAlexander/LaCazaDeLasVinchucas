package ar.edu.unq.vinchucas.usuario;

public class ExpertoPermanente extends NivelExperto {
    @Override
    public NivelUsuario evaluarCambioDeNivel(Usuario usuario) {
        // Un experto permanente nunca cambia de nivel
        return this;
    }
} 