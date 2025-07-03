package ar.edu.unq.vinchucas.muestra.estado;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class EstadoVerificada extends EstadoAbstracto {
    private final TipoDeOpinion resultado;
    
    public EstadoVerificada(TipoDeOpinion resultado) {
        this.resultado = resultado;
    }
    
    @Override
    protected void procesarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
        // Las muestras verificadas no permiten opiniones adicionales
        // Esta excepción nunca debería lanzarse ya que puedeOpinarUsuario devuelve false
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
    
    @Override
    public boolean esVerificada(){
        return true;
    }
} 