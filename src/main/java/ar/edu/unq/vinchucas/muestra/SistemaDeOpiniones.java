package ar.edu.unq.vinchucas.muestra;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.vinchucas.aplicacion.SistemaDeExcepciones;
import ar.edu.unq.vinchucas.usuario.Usuario;

public class SistemaDeOpiniones {

    private List<Opinion> opiniones;

    public SistemaDeOpiniones() {
        this.opiniones = new ArrayList<>();
    }

    public void agregarOpinion(Opinion opinion) throws SistemaDeExcepciones {
        if (opinion == null) {
            throw new SistemaDeExcepciones("La opinión no puede ser nula");
        }
        
        if (opinion.getUsuario() == null) {
            throw new SistemaDeExcepciones("El usuario de la opinión no puede ser nulo");
        }
        
        // Validar que el usuario no haya opinado antes
        if (yaOpinoElUsuario(opinion.getUsuario())) {
            throw new SistemaDeExcepciones("El usuario ya ha opinado sobre esta muestra");
        }
        
        opiniones.add(opinion);
    }

    private boolean yaOpinoElUsuario(Usuario usuario) {
        return opiniones.stream()
                .anyMatch(opinion -> opinion.getUsuario().equals(usuario));
    }

    public List<Opinion> getOpiniones() {
        return opiniones;
    }

    public LocalDate getFechaUltimaOpinion() {
        if (opiniones.isEmpty()) {
            return null;
        }
        return opiniones.get(opiniones.size() - 1).getFecha();
    }
}

