package ar.edu.unq.vinchucas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/* 
 OBSERVACIONES: No tenia sentido que la muestra guarde como colaborador interno al resultado actual,
 ya que el mismo se calcula al momento entonces seria calcularlo, guardarlo, y retornarlo. El paso de guardarlo, sobra.
 Tambien, la muestra tiene que guardar el tipo de vinchuca que indica la persona que sube la muestra, ya que esta se tiene que
 tener en cuenta al realizar la votacion para saber el resultado actual.
 El metodo actual agregarOpinion, deberia recibir como parametro al usuario, o al tipo del usuario 
 que envia el comentario ya que necesita validarlo debido a que una vez que un usuario experto envia una opinion,
 la muestra pasa a ser opinable solo para usuarios expertos, y no para usuarios basicos.
 */

public class Muestra {
    private final String foto;
    private final String ubicacion;
    private final LocalDate fechaCreacion;
    private final String nombreUsuario;
    private final List<Opinion> opiniones;
    private EstadoMuestra estado;
    private TipoDeOpinion vinchucaFotografiada;

    public Muestra(String foto, String ubicacion, String nombreUsuario) {
        this.foto = foto;
        this.ubicacion = ubicacion;
        this.nombreUsuario = nombreUsuario;
        this.fechaCreacion = LocalDate.now();
        this.opiniones = new ArrayList<>();
        this.estado = EstadoMuestra.NO_VERIFICADA;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public EstadoMuestra getEstado() {
        return estado;
    }

    /* Modificacion de agregarOpinion: se agrego el parametro usuario */
    
    public void agregarOpinion(Usuario usuario, Opinion opinion) {
        if(this.admiteOpiniones() && this.puedeOpinarElUsuario(Usuario usuario)) {
        	opiniones.add(opinion);
            verificarSiCorresponde();
        }
    }

    private boolean admiteOpiniones() {
		// TODO Auto-generated method stub
		return false;
	}

	private void resultadoActual() {
        
    }
}
