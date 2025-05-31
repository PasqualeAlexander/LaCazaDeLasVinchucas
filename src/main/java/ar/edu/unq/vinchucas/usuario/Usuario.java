package ar.edu.unq.vinchucas.usuario;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.muestra.Opinion;
import ar.edu.unq.vinchucas.muestra.TipoOpinion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private final String id;
    private NivelUsuario nivel;
    private final List<Opinion> opiniones;
    private final List<Muestra> muestrasEnviadas;
    private final boolean esInvestigador;

    public Usuario(String id, boolean esInvestigador) {
        this.id = id;
        this.esInvestigador = esInvestigador;
        this.nivel = esInvestigador ? new ExpertoPermanente() : new NivelBasico();
        this.opiniones = new ArrayList<>();
        this.muestrasEnviadas = new ArrayList<>();
    }

    public void opinar(Muestra muestra, TipoOpinion tipoOpinion) {
        Opinion opinion = new Opinion(tipoOpinion, this);
        nivel.opinar(muestra, opinion);
        opiniones.add(opinion);
        if (!esInvestigador) {
            evaluarNivel();
        }
    }

    public void enviarMuestra(String foto, String ubicacion) {
        Muestra muestra = new Muestra(foto, ubicacion, this);
        muestrasEnviadas.add(muestra);
        if (!esInvestigador) {
            evaluarNivel();
        }
    }

    private void evaluarNivel() {
        NivelUsuario nuevoNivel = nivel.evaluarCambioDeNivel(this);
        if (nuevoNivel instanceof NivelExperto && !(nivel instanceof NivelExperto)) {
            // Si pasa de básico a experto, crear el tipo correcto de experto
            nivel = new ExpertoValidado();
        } else {
            nivel = nuevoNivel;
        }
    }

    public boolean cumpleRequisitosParaSerExperto() {
        if (esInvestigador) {
            return true;
        }
        LocalDate fechaLimite = LocalDate.now().minusDays(30);
        long opinionesUltimos30Dias = opiniones.stream()
                .filter(opinion -> !opinion.getFecha().isBefore(fechaLimite))
                .count();
        
        return opinionesUltimos30Dias >= 20;
    }

    public String getId() {
        return id;
    }

    public NivelUsuario getNivel() {
        return nivel;
    }

    public boolean esInvestigador() {
        return esInvestigador;
    }
} 