package ar.edu.unq.vinchucas.muestra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.edu.unq.vinchucas.usuario.Usuario;

public class SistemaDeOpiniones implements Votable {
	private List<Opinion> opiniones;
	private Map<TipoDeOpinion, Integer> votos;
	private List <String> historial;
							   //Integer: cantidad de votos.
	
	public SistemaDeOpiniones() {
		this.opiniones = new ArrayList<>();
		this.votos = new HashMap<>();
		this.historial = new ArrayList<>();
	}
	
	public void agregarOpinion(Opinion opinion) {
		TipoDeOpinion voto = opinion.getTipoDeOpinion();
		Usuario usuario = opinion.getUsuario();
		
		if (usuario.esNivelExperto() && !this.hayVotoExperto()) {
			votos = new HashMap<>();
			votos.put(voto, 1);
			opiniones.add(opinion);
		}
		else {
	        votos.put(voto, votos.getOrDefault(voto, 0) + 1);
	        opiniones.add(opinion);
	    }
	    
	    historial.add("El Usuario "+opinion.getUsuario().getNombreUsuario()+" ha votado: "+voto);
	}
	
	private boolean hayVotoExperto() {
		for (Opinion opinion : opiniones) {
			if (opinion.getUsuario().esNivelExperto()){
				return true;
			}
		}
		return false;
	}

	public boolean admiteOpinionDeUsuario(Usuario usuario) {
		return (!this.correspondeVerificar() && this.puedeOpinarElUsuario(usuario));
	}
	
	public boolean correspondeVerificar() {
		int votosExperto = 0;
		for (Opinion opinion : opiniones) {
			if (opinion.getUsuario().esNivelExperto()) {
				votosExperto ++;
			}
		}
		return votosExperto == 2;
	}

	private boolean puedeOpinarElUsuario(Usuario usuario) {
		// No chequea verificacion ya que se chequea antes en admiteOpiniones.
		if (usuario.esNivelBasico() && this.hayVotoExperto()) {
			return false;
		} 
		return true;
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
}
