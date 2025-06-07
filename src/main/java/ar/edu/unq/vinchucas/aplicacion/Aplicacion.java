package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.Muestra;
import ar.edu.unq.vinchucas.usuario.*;

public class Aplicacion {
    private SistemaDeUsuarios sistemaDeUsuarios;
    private SistemaDeMuestras sistemaDeMuestras;


    public Aplicacion(SistemaDeUsuarios sistemaDeUsuarios, SistemaDeMuestras sistemaDeMuestras) {
        this.sistemaDeUsuarios = sistemaDeUsuarios;
        this.sistemaDeMuestras = sistemaDeMuestras;
    }

    // Metodo para registrar usuario.
    public void registrarUsuario(Usuario usuario) throws SistemaDeExcepciones {
    	this.validarDatosBasicosDeUsuario(usuario);
        this.sistemaDeUsuarios.agregarUsuario(usuario);

    }
    
    // Metodo para validar datos basicos de usuario, las reglas de negocio se delegan al sistema.
    public void validarDatosBasicosDeUsuario(Usuario usuario) throws SistemaDeExcepciones {
    	if (usuario == null) {
            throw new SistemaDeExcepciones("El usuario no puede ser nulo");
        }
        if (usuario.getNombreUsuario().isEmpty() || usuario.getNombreUsuario().isBlank()){
            throw new SistemaDeExcepciones("El nombre es obligatorio");
        }
    }
    
    
    public void registrarMuestra(Usuario usuario, Muestra muestra) {
    	usuario.enviarMuestra(muestra);
    	sistemaDeMuestras.registrarMuestra(muestra);
    }
}

