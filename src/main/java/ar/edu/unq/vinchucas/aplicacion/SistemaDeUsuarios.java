package ar.edu.unq.vinchucas.aplicacion;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unq.vinchucas.usuario.Usuario;

public class SistemaDeUsuarios {
	private List<Usuario> usuarios;
	
	public SistemaDeUsuarios() {
		usuarios = new ArrayList<>();
	}
	
	public void agregarUsuario(Usuario usuario) throws SistemaDeExcepciones {
	    if (existeUsuario(usuario.getNombreUsuario())) {
	        throw new SistemaDeExcepciones("El nombre ya está en uso");
	    }
	    usuarios.add(usuario);
	}

	private boolean existeUsuario(String nombre) {
	    for (Usuario u : usuarios) {
	        if (u.getNombreUsuario().equals(nombre)) {
	            return true;
	        }
	    }
	    return false;
	}

	public List<Usuario> getUsuariosRegistrados() {
		return usuarios;
	}

}
