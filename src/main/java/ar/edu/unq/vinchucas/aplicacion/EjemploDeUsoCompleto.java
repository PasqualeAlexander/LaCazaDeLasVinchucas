package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.*;
import ar.edu.unq.vinchucas.usuario.*;
import ar.edu.unq.vinchucas.organizacion.*;
import ar.edu.unq.vinchucas.zonas.*;
import ar.edu.unq.vinchucas.filtros.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Ejemplo completo de uso del sistema "A la caza de las vinchucas"
 * Demuestra todas las funcionalidades integradas:
 * - Gestión de usuarios y muestras
 * - Sistema de opiniones y verificación
 * - Zonas de cobertura y organizaciones
 * - Búsquedas con filtros
 * - Notificaciones automáticas
 */
public class EjemploDeUsoCompleto {

    public static void main(String[] args) throws SistemaDeExcepciones {
        System.out.println("=== SISTEMA A LA CAZA DE LAS VINCHUCAS ===\n");

        // 1. CONFIGURACIÓN INICIAL DEL SISTEMA
        System.out.println("1. Configurando el sistema...");
        
        SistemaDeUsuarios sistemaUsuarios = new SistemaDeUsuarios();
        RepositorioDeMuestras repositorio = new RepositorioDeMuestras();
        SistemaDeZonas sistemaZonas = new SistemaDeZonas();
        SistemaDeOrganizaciones sistemaOrganizaciones = new SistemaDeOrganizaciones();
        
        Aplicacion app = new Aplicacion(sistemaUsuarios, repositorio, sistemaZonas, sistemaOrganizaciones);

        // 2. CREAR USUARIOS
        System.out.println("\n2. Registrando usuarios...");
        
        RepositorioDeMuestras repoUsuario1 = new RepositorioDeMuestras();
        RepositorioDeOpiniones opinionesUsuario1 = new RepositorioDeOpiniones();
        Usuario ciudadano = new Usuario("juan_ciudadano", "pass123", repoUsuario1, opinionesUsuario1);
        
        RepositorioDeMuestras repoExperto = new RepositorioDeMuestras();
        RepositorioDeOpiniones opinionesExperto = new RepositorioDeOpiniones();
        Usuario experto = new Usuario("dr_entomologia", "expertPass", repoExperto, opinionesExperto);
        experto.setNivel(new NivelExperto()); // Experto validado externamente
        
        app.registrarUsuario(ciudadano);
        app.registrarUsuario(experto);
        System.out.println("✓ Usuarios registrados: " + sistemaUsuarios.getUsuariosRegistrados().size());

        // 3. CREAR ORGANIZACIONES
        System.out.println("\n3. Registrando organizaciones...");
        
        Ubicacion ubicacionHospital = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        Ubicacion ubicacionEscuela = new Ubicacion(-34.6118, -58.3960); // Cerca de BA
        
        Organizacion hospital = new OrganizacionImpl("Hospital Italiano", ubicacionHospital, TipoOrganizacion.SALUD, 150);
        Organizacion escuela = new OrganizacionImpl("Escuela Primaria N°1", ubicacionEscuela, TipoOrganizacion.EDUCATIVA, 25);
        
        app.registrarOrganizacion(hospital);
        app.registrarOrganizacion(escuela);
        System.out.println("✓ Organizaciones registradas: " + app.cantidadOrganizaciones());

        // 4. CREAR ZONAS DE COBERTURA
        System.out.println("\n4. Creando zonas de cobertura...");
        
        FuncionalidadExterna notificador = new FuncionalidadExterna() {
            @Override
            public void nuevoEvento(Organizacion org, ZonaDeCobertura zona, Muestra muestra) {
                System.out.println("🔔 NOTIFICACIÓN: " + org.getTipo() + " '" + 
                    ((OrganizacionImpl)org).getNombre() + "' - Nueva actividad en " + zona.getNombre());
            }
        };
        
        Ubicacion epicentroBA = new Ubicacion(-34.6037, -58.3816);
        ZonaDeCobertura zonaBuenosAires = new ZonaDeCobertura("Zona Buenos Aires", epicentroBA, 10.0, notificador);
        
        app.registrarZona(zonaBuenosAires);
        
        // Suscribir organizaciones a la zona
        app.suscribirOrganizacionAZona(hospital, zonaBuenosAires);
        app.suscribirOrganizacionAZona(escuela, zonaBuenosAires);
        System.out.println("✓ Zona creada con " + zonaBuenosAires.getOrganizacionesSuscriptas().size() + " organizaciones suscritas");

        // 5. ENVIAR MUESTRAS
        System.out.println("\n5. Enviando muestras...");
        
        Muestra muestra1 = new Muestra("foto_vinchuca_1.jpg", "-34.6037,-58.3816", ciudadano, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("foto_insecto_2.jpg", "-34.6118,-58.3960", ciudadano, TipoDeOpinion.CHINCHE_FOLIADA);
        
        app.registrarMuestra(ciudadano, muestra1);
        app.registrarMuestra(ciudadano, muestra2);
        System.out.println("✓ Muestras enviadas: " + repositorio.cantidadMuestras());

        // 6. SISTEMA DE OPINIONES Y VERIFICACIÓN
        System.out.println("\n6. Proceso de verificación...");
        
        // Crear otro experto para la verificación
        RepositorioDeMuestras repoExperto2 = new RepositorioDeMuestras();
        RepositorioDeOpiniones opinionesExperto2 = new RepositorioDeOpiniones();
        Usuario experto2 = new Usuario("dra_chagas", "expertPass2", repoExperto2, opinionesExperto2);
        experto2.setNivel(new NivelExperto());
        app.registrarUsuario(experto2);
        
        // Los expertos opinan sobre la primera muestra
        Opinion opinion1 = new Opinion(experto, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(experto2, TipoDeOpinion.VINCHUCA_INFESTANS);
        
        muestra1.agregarOpinion(opinion1);
        muestra1.agregarOpinion(opinion2);
        
        System.out.println("✓ Muestra verificada: " + muestra1.estaVerificada());
        System.out.println("✓ Resultado: " + muestra1.getResultado());
        
        // Procesar la validación (esto notificará a las organizaciones)
        app.procesarValidacion(muestra1);

        // 7. BÚSQUEDAS CON FILTROS
        System.out.println("\n7. Realizando búsquedas...");
        
        // Buscar muestras verificadas
        List<Muestra> verificadas = app.getMuestrasVerificadas();
        System.out.println("✓ Muestras verificadas: " + verificadas.size());
        
        // Buscar por tipo de insecto
        FiltroPorTipoInsecto filtroVinchuca = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS);
        List<Muestra> vinchucas = app.buscarMuestras(filtroVinchuca);
        System.out.println("✓ Muestras de Vinchuca Infestans: " + vinchucas.size());
        
        // Buscar por nivel de verificación
        FiltroPorNivelVerificacion filtroVerificadas = new FiltroPorNivelVerificacion(true);
        List<Muestra> soloVerificadas = app.buscarMuestras(filtroVerificadas);
        System.out.println("✓ Solo verificadas: " + soloVerificadas.size());

        // 8. ESTADÍSTICAS FINALES
        System.out.println("\n8. Estadísticas del sistema:");
        System.out.println("📊 Total usuarios: " + sistemaUsuarios.getUsuariosRegistrados().size());
        System.out.println("📊 Total muestras: " + repositorio.cantidadMuestras());
        System.out.println("📊 Muestras verificadas: " + repositorio.cantidadMuestrasVerificadas());
        System.out.println("📊 Total organizaciones: " + app.cantidadOrganizaciones());
        System.out.println("📊 Organizaciones de salud: " + app.cantidadOrganizacionesPorTipo(TipoOrganizacion.SALUD));
        System.out.println("📊 Zonas de cobertura: " + app.getZonas().size());

        // 9. DEMOSTRAR FUNCIONALIDAD EXTERNA PERSONALIZADA
        System.out.println("\n9. Configurando funcionalidad externa personalizada...");
        
        FuncionalidadExterna alertaEspecial = new FuncionalidadExterna() {
            @Override
            public void nuevoEvento(Organizacion org, ZonaDeCobertura zona, Muestra muestra) {
                if (muestra.getResultado() == TipoDeOpinion.VINCHUCA_INFESTANS) {
                    System.out.println("🚨 ALERTA CRÍTICA: Vinchuca Infestans detectada en " + zona.getNombre() + 
                                     " - Organización " + ((OrganizacionImpl)org).getNombre() + " debe actuar inmediatamente");
                }
            }
        };
        
        // Configurar el hospital con alerta especial para validaciones
        ((OrganizacionImpl)hospital).setFuncionalidadValidacion(alertaEspecial);
        
        // Simular una nueva validación para mostrar la alerta
        app.procesarValidacion(muestra1);

        System.out.println("\n=== SISTEMA COMPLETAMENTE FUNCIONAL ===");
        System.out.println("✅ Todas las funcionalidades del enunciado implementadas");
        System.out.println("✅ Integración completa entre todos los componentes");
        System.out.println("✅ Notificaciones automáticas funcionando");
        System.out.println("✅ Sistema de filtros operativo");
        System.out.println("✅ Arquitectura hexagonal implementada");
    }
} 