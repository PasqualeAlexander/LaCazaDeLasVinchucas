package ar.edu.unq.vinchucas.muestra.estado;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.usuario.Usuario;

public abstract class EstadoAbstracto implements IEstadoMuestra {
    
    @Override
    public final void agregarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
        // Template Method: define el algoritmo general
        validarOpinion(muestra, opinion);
        muestra.almacenarOpinion(opinion); // la muestra y su repositorio se encargan de validación fuera de los estados
        procesarOpinion(muestra, opinion);
    }
    
    // Validaciones comunes a todos los estados
    protected void validarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
        
        Usuario usuario = opinion.getUsuario();
        
        // Validación 1: El usuario no puede opinar sobre su propia muestra
        if (usuario.equals(muestra.getUsuario())) {
            throw new SistemaDeExcepciones("El usuario no puede opinar sobre su propia muestra");
        }
        
        // Validación 2: Validaciones específicas del estado
        if (!puedeOpinarUsuario(usuario, muestra)) {
            throw new SistemaDeExcepciones("El usuario no puede opinar en el estado actual de la muestra");
        }
    }
    
    // Método abstracto que cada estado debe implementar para su lógica específica
    protected abstract void procesarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones; // primitive operation
    
    // Método por defecto para verificar si un usuario puede opinar
    // Los estados específicos pueden sobreescribir este método
    @Override
    public abstract boolean puedeOpinarUsuario(Usuario usuario, Muestra muestra);
    
} 