package ar.edu.unq.vinchucas.muestra.estado;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;

public interface IEstadoMuestra {
    void agregarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones;
    boolean puedeOpinarUsuario(Usuario usuario, Muestra muestra);
    TipoDeOpinion getResultado();
	boolean esVerificada();
} 