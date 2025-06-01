package ar.edu.unq.vinchucas.muestra;

import java.util.ArrayList;
import java.util.List;

public class RepositorioDeOpiniones {
	private List<Opinion> opiniones;
	
	public RepositorioDeOpiniones() {
		opiniones = new ArrayList <>();
		
	}
    public void agregarOpinion(Opinion opinion) {
    	opiniones.add(opinion);
    }
    
    public List<Opinion> getOpiniones(){
    	return opiniones;
    }
}

