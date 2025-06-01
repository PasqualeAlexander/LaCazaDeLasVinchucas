package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.vinchucas.usuario.Usuario;

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
        if(this.admiteOpiniones()) {
        	opiniones.add(opinion);
            verificarEstado();
        }
    }

    private boolean admiteOpiniones() {
		// TODO Auto-generated method stub
		return false;
	}

	private void verificarEstado() {
        // Lógica para verificar el estado según las opiniones
        // Se implementará más adelante
    }

	public boolean estaVerificada() {
		// TODO Auto-generated method stub
		return false;
	}

	public TipoDeOpinion getResultado() {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalDate getFechaUltimaVotacion() { // TODO: Implement
		// TODO Auto-generated method stub
		return null;
	}
}
