package ar.edu.unq.vinchucas.muestra.estado;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class EstadoVerificada implements IEstadoMuestra {
    private final TipoDeOpinion resultado;
    
    public EstadoVerificada(TipoDeOpinion resultado) {
        this.resultado = resultado;
    }
    
    @Override
    public void agregarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
        throw new SistemaDeExcepciones("No se pueden agregar opiniones a una muestra verificada");
    }
    
    @Override
    public boolean puedeOpinarUsuario(Usuario usuario, Muestra muestra) {
        return false; // Nadie puede opinar en estado verificada
    }
    
    @Override
    public TipoDeOpinion getResultado() {
        return resultado; // Resultado fijo
    }
} 