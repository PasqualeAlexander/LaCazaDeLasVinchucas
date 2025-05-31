package ar.edu.unq.vinchucas.muestra;

import ar.edu.unq.vinchucas.usuario.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Muestra {
    private final String foto;
    private final String ubicacion;
    private final LocalDate fechaCreacion;
    private final Usuario usuario;
    private final List<Opinion> opiniones;
    private EstadoMuestra estado;

    public Muestra(String foto, String ubicacion, Usuario usuario) {
        this.foto = foto;
        this.ubicacion = ubicacion;
        this.usuario = usuario;
        this.fechaCreacion = LocalDate.now();
        this.opiniones = new ArrayList<>();
        this.estado = EstadoMuestra.NO_VERIFICADA;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public EstadoMuestra getEstado() {
        return estado;
    }

    public void agregarOpinion(Opinion opinion) {
        opiniones.add(opinion);
        verificarEstado();
    }

    private void verificarEstado() {
        // Lógica para verificar el estado según las opiniones
        // Se implementará más adelante
    }
} 