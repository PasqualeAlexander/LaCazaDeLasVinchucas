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

    public void agregarOpinionInicial(Opinion opinion) {
        opiniones.add(opinion);
    }

    public void agregarOpinion(Opinion opinion) {
        opiniones.add(opinion);
    }

    public boolean yaOpinoElUsuario(Usuario usuario) {
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

