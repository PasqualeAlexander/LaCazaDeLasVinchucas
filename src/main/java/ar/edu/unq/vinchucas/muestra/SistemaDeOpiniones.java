package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class SistemaDeOpiniones implements Votable {

    private List<Opinion> opiniones;
    private Map<TipoDeOpinion, Integer> votos;
    private List<String> historial;

    public SistemaDeOpiniones(TipoDeOpinion votoInicial) { // Se cambia el consturcot para que tambien gestione el voto inicial y sea responsabilidad pura del sistema.
        this.opiniones = new ArrayList<>();
        this.votos = new HashMap<>();
        this.historial = new ArrayList<>();
        this.contabilizarVotoInicial(votoInicial);
    }

    public void agregarOpinion(Opinion opinion) throws SistemaDeExcepciones {
        this.validarQueUsuarioPuedeOpinar(opinion.getUsuario());

        TipoDeOpinion voto = opinion.getTipoDeOpinion();
        Usuario usuario = opinion.getUsuario();

        if (usuario.esNivelExperto() && !this.hayVotoExperto()) {
            this.reiniciarVotos();
        }

        this.computarVoto(voto);
        opiniones.add(opinion);
        historial.add("El Usuario " + usuario.getNombreUsuario() + " ha votado: " + voto);
    }

    public boolean admiteOpinionDeUsuario(Usuario usuario) {
        return !this.correspondeVerificar() && this.puedeOpinarElUsuario(usuario);
    }

    public boolean correspondeVerificar() {
        Map<TipoDeOpinion, Integer> conteoExpertos = new HashMap<>();

        for (Opinion opinion : opiniones) {
            if (opinion.eraExpertoAlOpinar()) {
                TipoDeOpinion tipo = opinion.getTipoDeOpinion();
                conteoExpertos.put(tipo, conteoExpertos.getOrDefault(tipo, 0) + 1);
                if (conteoExpertos.get(tipo) == 2) {
                    return true;
                }
            }
        }

        return false;
    }

    public TipoDeOpinion getResultado() {
        int maxVotos = 0;
        TipoDeOpinion resultado = null;
        boolean empate = false;

        for (Map.Entry<TipoDeOpinion, Integer> entry : votos.entrySet()) {
            if (entry.getValue() > maxVotos) {
                maxVotos = entry.getValue();
                resultado = entry.getKey();
                empate = false;
            } else if (entry.getValue() == maxVotos && resultado != null) {
                empate = true;
            }
        }

        return empate ? TipoDeOpinion.NO_DEFINIDO : resultado;
    }

    public String getHistorial() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTORIAL DE VOTACIONES ===\n");
        for (String registro : historial) {
            sb.append(registro).append("\n");
        }
        sb.append("Total votos: ").append(historial.size()).append("\n");
        sb.append("=== FIN ===");

        return sb.toString();
    }
    
	public List<Opinion> getOpiniones() {
		return opiniones;
	}
	
	public LocalDate getFechaUltimaOpinion() {
	    if (opiniones.isEmpty()) {
	        return null;
	    }
	    return getUltimaOpinion().getFecha();
	}
	
	public void contabilizarVotoInicial(TipoDeOpinion tipo) {
	    votos.put(tipo, votos.getOrDefault(tipo, 0) + 1);
	}
	
	private void computarVoto(TipoDeOpinion voto) {
        votos.put(voto, votos.getOrDefault(voto, 0) + 1);
    }

	private Opinion getUltimaOpinion() {
		return opiniones.get(opiniones.size() - 1);
	}

	private void validarQueUsuarioPuedeOpinar(Usuario usuario) throws SistemaDeExcepciones {
	    if (this.yaOpinoElUsuario(usuario)) {
	        throw new SistemaDeExcepciones("El usuario ya opino sobre esta muestra.");
	    }
	    if (usuario.esNivelBasico() && this.hayVotoExperto()) {
	        throw new SistemaDeExcepciones("Un usuario de nivel básico no puede opinar si ya hay al menos un voto de usuario experto.");
	    }
	}

	private boolean puedeOpinarElUsuario(Usuario usuario) {
	    return !yaOpinoElUsuario(usuario) && !(usuario.esNivelBasico() && hayVotoExperto());
	}

    private boolean yaOpinoElUsuario(Usuario usuario) {
        return opiniones.stream().anyMatch(op -> op.getUsuario().equals(usuario));
    }

    private boolean hayVotoExperto() {
        return opiniones.stream().anyMatch(Opinion::eraExpertoAlOpinar);
    }

    private void reiniciarVotos() {
        votos = new HashMap<>();
    }

}
