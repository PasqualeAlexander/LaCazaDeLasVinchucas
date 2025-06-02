package ar.edu.unq.vinchucas.aplicacion;

import java.util.ArrayList;
import java.util.List;
import ar.edu.unq.vinchucas.usuario.*;

public class Aplicacion {
    List<Usuario> usuarios;

    public Aplicacion() {
        this.usuarios = new ArrayList<>();
    }

    public boolean estaDisponibleElNombre(String nombre) throws ExcepcionesAplicacion {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombre)) {
                throw new ExcepcionesAplicacion("Ya hay un usuario con ese nombre, utiliza otro nombre por favor");
            }
        }
        return true;
    }

    public String getMessage() {
    	return "Ya hay un usuario con ese nombre, utiliza otro nombre por favor";
    }
    
    public void registrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}
