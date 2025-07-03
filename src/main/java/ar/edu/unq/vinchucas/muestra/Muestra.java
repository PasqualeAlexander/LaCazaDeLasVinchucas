package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import java.util.List;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.estado.EstadoAbierto;
import ar.edu.unq.vinchucas.muestra.estado.IEstadoMuestra;
import ar.edu.unq.vinchucas.usuario.Usuario;
import java.util.ArrayList;

public class Muestra {
    private final String foto;
    private final String ubicacion;
    private final LocalDate fechaCreacion;
    private final Usuario usuario;
    private final SistemaDeOpiniones sistemaDeOpiniones;
    private IEstadoMuestra estado;
    private final List<ObservadorMuestra> observadores;
    
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
        this.sistemaDeOpiniones = new SistemaDeOpiniones();
        this.observadores = new ArrayList<>();
        // Crear la opinión inicial del usuario que sube la muestra
        Opinion opinionInicial = new Opinion(usuario, votoInicial);
        this.sistemaDeOpiniones.agregarOpinionInicial(opinionInicial);
        
        // Inicializar el estado con la opinión inicial
        this.estado = new EstadoAbierto(opinionInicial);
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

    public SistemaDeOpiniones getSistemaDeOpiniones() {
        return sistemaDeOpiniones;
    }

    public String getNombreUsuario() {
        return usuario.getNombreUsuario();
    }

    public TipoDeOpinion getResultado() {
        return estado.getResultado();
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
        // Delegar la lógica al estado
        estado.agregarOpinion(this, opinion);

        // Agregar al historial
        sistemaDeOpiniones.agregarOpinion(opinion);
    }

    public boolean estaVerificada() {
        return estado.esVerificada();
    }

    private boolean admiteOpinionDeUsuario(Usuario usuario) {
        if (usuario.equals(this.usuario)) {
            return false;
        }
        return estado.puedeOpinarUsuario(usuario, this);
    }

    // Método para cambiar estado (solo accesible desde los estados)
    public void setEstado(IEstadoMuestra nuevoEstado) {
        this.estado = nuevoEstado;
        
        if (this.estaVerificada()) {
            notificarMuestraVerificada();
        }
    }
    
    public void agregarObservador(ObservadorMuestra observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorMuestra observador) {
        observadores.remove(observador);
    }

    private void notificarMuestraVerificada() {
        for (ObservadorMuestra observador : observadores) {
            observador.muestraVerificada(this);
        }
    }
}
