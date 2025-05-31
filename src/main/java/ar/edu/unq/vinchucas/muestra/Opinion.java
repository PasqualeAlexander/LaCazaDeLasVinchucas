package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.usuario.Usuario;
import java.time.LocalDate;

public class Opinion {
    private final TipoOpinion tipo;
    private final Usuario usuario;
    private final LocalDate fecha;

    public Opinion(TipoOpinion tipo, Usuario usuario) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.fecha = LocalDate.now();
    }

    public TipoOpinion getTipo() {
        return tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }
} 