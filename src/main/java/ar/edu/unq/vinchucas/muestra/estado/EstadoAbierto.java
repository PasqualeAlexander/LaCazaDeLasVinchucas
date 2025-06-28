package ar.edu.unq.vinchucas.muestra.estado;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoDeOpinion;
import ar.edu.unq.vinchucas.usuario.Usuario;
import java.util.HashMap;
import java.util.Map;

public class EstadoAbierto implements IEstadoMuestra {
    private TipoDeOpinion resultado;
    private final Map<TipoDeOpinion, Integer> votos;
    
    public EstadoAbierto(Opinion opinionInicial) {
        this.votos = new HashMap<>();
        this.resultado = opinionInicial.getTipoDeOpinion();
        this.votos.put(this.resultado, 1);
    }
    
    @Override
    public void agregarOpinion(Muestra muestra, Opinion opinion) throws SistemaDeExcepciones {
        Usuario usuario = opinion.getUsuario();
        
        if (!puedeOpinarUsuario(usuario, muestra)) {
            throw new SistemaDeExcepciones("El usuario no puede opinar en estado abierto");
        }
        
        // Si es un experto, cambiar inmediatamente a estado experto
        if (usuario.esNivelExperto()) {
            EstadoExperto nuevoEstado = new EstadoExperto(opinion);
            muestra.setEstado(nuevoEstado);
            return; // No actualizamos votos en estado abierto
        }
        
        // Solo si NO es experto, actualizar votos y recalcular resultado
        actualizarVotos(opinion.getTipoDeOpinion());
        recalcularResultado();
    }
    
    @Override
    public boolean puedeOpinarUsuario(Usuario usuario, Muestra muestra) {
        // En estado abierto, TODOS los usuarios pueden opinar
        // excepto el que subió la muestra y los que ya opinaron
        return !usuario.equals(muestra.getUsuario()) &&
               !muestra.getSistemaDeOpiniones().yaOpinoElUsuario(usuario);
    }
    
    @Override
    public TipoDeOpinion getResultado() {
        return resultado;
    }
    
    private void actualizarVotos(TipoDeOpinion tipo) {
        votos.put(tipo, votos.getOrDefault(tipo, 0) + 1);
    }
    
    private void recalcularResultado() {
        int maxVotos = 0;
        TipoDeOpinion nuevoResultado = null;
        boolean empate = false;

        for (Map.Entry<TipoDeOpinion, Integer> entry : votos.entrySet()) {
            if (entry.getValue() > maxVotos) {
                maxVotos = entry.getValue();
                nuevoResultado = entry.getKey();
                empate = false;
            } else if (entry.getValue() == maxVotos && nuevoResultado != null) {
                empate = true;
            }
        }

        this.resultado = empate ? TipoDeOpinion.NO_DEFINIDO : nuevoResultado;
    }

	@Override
	public boolean esVerificada() {
		return false;
	}
} 