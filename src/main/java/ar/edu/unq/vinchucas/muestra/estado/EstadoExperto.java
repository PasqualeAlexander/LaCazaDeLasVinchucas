package ar.edu.unq.vinchucas.muestra.estado;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import java.util.ArrayList;
import java.util.List;

public class EstadoExperto implements IEstadoMuestra {
    private TipoDeOpinion resultado;
    private final List<TipoDeOpinion> opinionesExpertos;
    
    public EstadoExperto(Opinion opinionInicial) {
        this.opinionesExpertos = new ArrayList<>();
        this.resultado = opinionInicial.getTipoDeOpinion();
        this.opinionesExpertos.add(this.resultado);
    }
    
    @Override
    public void agregarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
        Usuario usuario = opinion.getUsuario();
        
        if (!puedeOpinarUsuario(usuario, muestra)) {
            throw new SistemaDeExcepciones("El usuario no puede opinar en estado experto");
        }
        
        TipoDeOpinion tipoOpinion = opinion.getTipoDeOpinion();
        
        // Verificar si ya existe una opinión del mismo tipo
        if (opinionesExpertos.contains(tipoOpinion)) {
            // Ya hay 2 expertos coincidiendo, ir a verificada
            EstadoVerificada nuevoEstado = new EstadoVerificada(tipoOpinion);
            muestra.setEstado(nuevoEstado);
            return;
        }
        
        // Si no existe, agregar la nueva opinión
        opinionesExpertos.add(tipoOpinion);
        // Si llegamos aquí, hay mas de 1 opinion y son todas diferentes
        this.resultado = TipoDeOpinion.NO_DEFINIDO;
    }
    
    @Override
    public boolean puedeOpinarUsuario(Usuario usuario, Muestra muestra) {
        // En estado experto, solo expertos pueden opinar
        // y no pueden opinar sobre sus propias muestras
        return usuario.esNivelExperto() && 
               !usuario.equals(muestra.getUsuario()) &&
               !muestra.getSistemaDeOpiniones().yaOpinoElUsuario(usuario);
    }
    
    @Override
    public TipoDeOpinion getResultado() {
        return resultado;
    }
} 