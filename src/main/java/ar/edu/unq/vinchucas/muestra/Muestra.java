package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import java.util.List;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class Muestra {
    private final String foto;
    private final String ubicacion;
    private final LocalDate fechaCreacion;
    private final Usuario usuario;
    private final SistemaDeOpiniones sistemaDeOpiniones;
    private EstadoMuestra estado;

    public Muestra(String foto, String ubicacion, Usuario usuario, TipoDeOpinion votoInicial) throws SistemaDeExcepciones {
        if (foto == null || foto.isBlank()) {
            throw new SistemaDeExcepciones("La foto no puede estar vacía");
        }
        if (ubicacion == null || ubicacion.isBlank()) {
            throw new SistemaDeExcepciones("La ubicación no puede estar vacía");
        }
        if (usuario == null) {
            throw new SistemaDeExcepciones("El usuario no puede ser nulo");
        }
        if (votoInicial == null) {
            throw new SistemaDeExcepciones("El voto inicial no puede ser nulo");
        }

        this.foto = foto;
        this.ubicacion = ubicacion;
        this.usuario = usuario;
        this.fechaCreacion = LocalDate.now();
        this.estado = EstadoMuestra.NO_VERIFICADA;
        this.sistemaDeOpiniones = new SistemaDeOpiniones();
        this.sistemaDeOpiniones.contabilizarVotoInicial(votoInicial);
    }

    public String getFoto() {
        return foto;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public EstadoMuestra getEstado() {
        return estado;
    }

    public String getNombreUsuario() {
        return usuario.getNombreUsuario();
    }

    public TipoDeOpinion getResultado() {
        return sistemaDeOpiniones.getResultado();
    }

    public LocalDate getFechaUltimaVotacion() {
        return sistemaDeOpiniones.getFechaUltimaOpinion();
    }

    public List<Opinion> getOpiniones() {
        return sistemaDeOpiniones.getOpiniones();
    }

    public void agregarOpinion(Opinion opinion) throws SistemaDeExcepciones {
        if (!admiteOpinionDeUsuario(opinion.getUsuario())) {
            throw new SistemaDeExcepciones("El usuario no puede opinar sobre esta muestra.");
        }
        sistemaDeOpiniones.agregarOpinion(opinion);
        verificarEstado();
    }

    public boolean estaVerificada() {
        return estado == EstadoMuestra.VERIFICADA;
    }

    private boolean admiteOpinionDeUsuario(Usuario usuario) {
        if (usuario.equals(this.usuario)) {
            return false;
        }
        return sistemaDeOpiniones.admiteOpinionDeUsuario(usuario);
    }

    private void verificarEstado() {
        if (correspondeVerificar()) {
            estado = EstadoMuestra.VERIFICADA;
        }
    }

    private boolean correspondeVerificar() {
        return sistemaDeOpiniones.correspondeVerificar();
    }
}
