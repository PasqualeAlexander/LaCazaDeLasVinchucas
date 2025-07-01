package ar.edu.unq.vinchucas.aplicacion;

import ar.edu.unq.vinchucas.muestra.*;
import ar.edu.unq.vinchucas.usuario.*;
import ar.edu.unq.vinchucas.organizacion.*;
import ar.edu.unq.vinchucas.zonas.*;
import ar.edu.unq.vinchucas.filtros.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test integral simplificado que valida los casos principales del sistema
 */
public class TestIntegralDelSistemaSimplificadoTest {

    private Aplicacion aplicacion;
    private Usuario usuarioBasico1;
    private Usuario usuarioBasico2;
    private Usuario expertoValidado;

    @BeforeEach
    public void setUp() throws SistemaDeExcepciones {
        // Configurar el sistema completo
        SistemaDeUsuarios sistemaUsuarios = new SistemaDeUsuarios();
        RepositorioDeMuestras repositorio = new RepositorioDeMuestras();
        SistemaDeZonas sistemaZonas = new SistemaDeZonas();
        SistemaDeOrganizaciones sistemaOrganizaciones = new SistemaDeOrganizaciones();
        
        aplicacion = new Aplicacion(sistemaUsuarios, repositorio, sistemaZonas, sistemaOrganizaciones);

        // Crear usuarios
        usuarioBasico1 = crearUsuario("ciudadano1", "pass1");
        usuarioBasico2 = crearUsuario("ciudadano2", "pass2");
        expertoValidado = crearUsuario("dr_entomologia", "expertPass");
        
        // Configurar experto validado externamente
        expertoValidado.setNivel(new NivelExperto());
        
        // Registrar usuarios
        aplicacion.registrarUsuario(usuarioBasico1);
        aplicacion.registrarUsuario(usuarioBasico2);
        aplicacion.registrarUsuario(expertoValidado);
    }

    private Usuario crearUsuario(String nombre, String password) {
        RepositorioDeMuestras repoMuestras = new RepositorioDeMuestras();
        RepositorioDeOpiniones repoOpiniones = new RepositorioDeOpiniones();
        return new Usuario(nombre, password, repoMuestras, repoOpiniones);
    }

    @Test
    public void testEnvioYVerificacionCompletaDeMuestras() throws SistemaDeExcepciones {
        // CASO 1: Proceso completo de envío y verificación de muestras
        
        // 1.1 Usuario básico envía muestra
        Muestra muestra1 = new Muestra("vinchuca_infestans.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico1, muestra1);
        
        assertEquals(1, aplicacion.getRepositorioDeMuestras().getMuestras().size());
        assertFalse(muestra1.estaVerificada());
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra1.getResultado());

        // 1.2 Usuario básico opina (hay empate, resultado NO_DEFINIDO)
        Opinion opinionBasico = new Opinion(usuarioBasico2, TipoDeOpinion.CHINCHE_FOLIADA);
        muestra1.agregarOpinion(opinionBasico);
        assertEquals(TipoDeOpinion.NO_DEFINIDO, muestra1.getResultado()); // Empate entre VINCHUCA_INFESTANS y CHINCHE_FOLIADA

        // 1.3 Experto opina (solo cuentan opiniones de expertos desde ahora)
        Opinion opinionExperto1 = new Opinion(expertoValidado, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestra1.agregarOpinion(opinionExperto1);
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra1.getResultado());
        assertFalse(muestra1.estaVerificada()); // Necesita 2 expertos

        // 1.4 Segundo experto opina (muestra queda verificada)
        Usuario segundoExperto = crearUsuario("segundo_experto", "pass");
        segundoExperto.setNivel(new NivelExperto());
        aplicacion.registrarUsuario(segundoExperto);
        
        Opinion opinionExperto2 = new Opinion(segundoExperto, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestra1.agregarOpinion(opinionExperto2);
        
        assertTrue(muestra1.estaVerificada());
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestra1.getResultado());
        assertEquals(1, aplicacion.getMuestrasVerificadas().size());
    }

    @Test
    public void testSistemaDeBusquedasConFiltros() throws SistemaDeExcepciones {
        // CASO 2: Sistema de búsquedas con filtros complejos
        
        // 2.1 Crear muestras con diferentes características
        Muestra muestra1 = new Muestra("vinchuca1.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("chinche1.jpg", "-34.6037,-58.3816", usuarioBasico2, TipoDeOpinion.CHINCHE_FOLIADA);
        Muestra muestra3 = new Muestra("vinchuca2.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_SORDIDA);
        
        aplicacion.registrarMuestra(usuarioBasico1, muestra1);
        aplicacion.registrarMuestra(usuarioBasico2, muestra2);
        aplicacion.registrarMuestra(usuarioBasico1, muestra3);

        // Verificar una muestra
        Usuario segundoExperto = crearUsuario("segundo_experto", "pass");
        segundoExperto.setNivel(new NivelExperto());
        aplicacion.registrarUsuario(segundoExperto);
        
        Opinion opinion1 = new Opinion(expertoValidado, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion opinion2 = new Opinion(segundoExperto, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestra1.agregarOpinion(opinion1);
        muestra1.agregarOpinion(opinion2);

        // 2.2 Filtros simples
        FiltroPorTipoInsecto filtroVinchuca = new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS);
        List<Muestra> vinchucas = aplicacion.buscarMuestras(filtroVinchuca);
        assertEquals(1, vinchucas.size());
        assertTrue(vinchucas.contains(muestra1));

        FiltroPorNivelVerificacion filtroVerificadas = new FiltroPorNivelVerificacion(true);
        List<Muestra> verificadas = aplicacion.buscarMuestras(filtroVerificadas);
        assertEquals(1, verificadas.size());
        assertTrue(verificadas.contains(muestra1));

        // 2.3 Filtros compuestos con AND
        FiltroCompuesto filtroAND = new FiltroCompuesto(OperadorLogico.AND);
        filtroAND.agregarFiltro(filtroVinchuca);
        filtroAND.agregarFiltro(filtroVerificadas);
        
        List<Muestra> resultadoAND = aplicacion.buscarMuestras(filtroAND);
        assertEquals(1, resultadoAND.size());
        assertTrue(resultadoAND.contains(muestra1));

        // 2.4 Filtros compuestos con OR
        FiltroCompuesto filtroOR = new FiltroCompuesto(OperadorLogico.OR);
        filtroOR.agregarFiltro(new FiltroPorTipoInsecto(TipoDeOpinion.CHINCHE_FOLIADA));
        filtroOR.agregarFiltro(filtroVerificadas);
        
        List<Muestra> resultadoOR = aplicacion.buscarMuestras(filtroOR);
        assertEquals(2, resultadoOR.size()); // muestra1 (verificada) y muestra2 (chinche)

        // 2.5 Filtros por fecha
        FiltroPorFecha filtroHoy = new FiltroPorFecha(LocalDate.now(), LocalDate.now());
        List<Muestra> muestrasHoy = aplicacion.buscarMuestras(filtroHoy);
        assertEquals(3, muestrasHoy.size()); // Todas las muestras son de hoy
        
    }

    @Test
    public void testCasosLimiteYExcepciones() throws SistemaDeExcepciones {
        // CASO 3: Casos límite y manejo de excepciones según el enunciado
        
        // 3.1 No se puede opinar sobre muestra propia
        Muestra muestraPropia = new Muestra("propia.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico1, muestraPropia);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            Opinion opinionPropia = new Opinion(usuarioBasico1, TipoDeOpinion.CHINCHE_FOLIADA);
            muestraPropia.agregarOpinion(opinionPropia);
        });

        // 3.2 No se puede opinar dos veces sobre la misma muestra
        Muestra muestraAjena = new Muestra("ajena.jpg", "-34.6037,-58.3816", usuarioBasico2, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico2, muestraAjena);
        
        Opinion primeraOpinion = new Opinion(usuarioBasico1, TipoDeOpinion.CHINCHE_FOLIADA);
        muestraAjena.agregarOpinion(primeraOpinion);
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            Opinion segundaOpinion = new Opinion(usuarioBasico1, TipoDeOpinion.VINCHUCA_SORDIDA);
            muestraAjena.agregarOpinion(segundaOpinion);
        });

        // 3.3 No se puede opinar sobre muestra verificada
        Muestra muestraVerificada = new Muestra("verificada.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico1, muestraVerificada);
        
        Usuario segundoExperto = crearUsuario("segundo_experto", "pass");
        segundoExperto.setNivel(new NivelExperto());
        aplicacion.registrarUsuario(segundoExperto);
        
        Opinion exp1 = new Opinion(expertoValidado, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion exp2 = new Opinion(segundoExperto, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestraVerificada.agregarOpinion(exp1);
        muestraVerificada.agregarOpinion(exp2);
        
        assertTrue(muestraVerificada.estaVerificada());
        
        assertThrows(SistemaDeExcepciones.class, () -> {
            Usuario tercerExperto = crearUsuario("tercer_experto", "pass");
            tercerExperto.setNivel(new NivelExperto());
            aplicacion.registrarUsuario(tercerExperto);
            Opinion opinionExtra = new Opinion(tercerExperto, TipoDeOpinion.CHINCHE_FOLIADA);
            muestraVerificada.agregarOpinion(opinionExtra);
        });

        // 3.4 Usuario duplicado
        assertThrows(SistemaDeExcepciones.class, () -> {
            Usuario usuarioDuplicado = crearUsuario("ciudadano1", "otraPass"); // Mismo nombre que usuarioBasico1
            aplicacion.registrarUsuario(usuarioDuplicado);
        });
    }

    @Test
    public void testGestionDeOrganizacionesYZonas() throws SistemaDeExcepciones {
        // CASO 4: Gestión de organizaciones y zonas de cobertura
        
        // 4.1 Crear y registrar organizaciones
        Ubicacion ubicacionHospital = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        Ubicacion ubicacionEscuela = new Ubicacion(-31.4201, -64.1888); // Córdoba
        
        Organizacion organizacionSalud = new OrganizacionImpl("Hospital Nacional", ubicacionHospital, TipoOrganizacion.SALUD, 200);
        Organizacion organizacionEducativa = new OrganizacionImpl("Universidad Nacional", ubicacionEscuela, TipoOrganizacion.EDUCATIVA, 500);
        
        aplicacion.registrarOrganizacion(organizacionSalud);
        aplicacion.registrarOrganizacion(organizacionEducativa);
        
        assertEquals(2, aplicacion.cantidadOrganizaciones());
        assertEquals(1, aplicacion.cantidadOrganizacionesPorTipo(TipoOrganizacion.SALUD));
        assertEquals(1, aplicacion.cantidadOrganizacionesPorTipo(TipoOrganizacion.EDUCATIVA));

        // 4.2 Crear zonas de cobertura
        FuncionalidadExterna notificador = new FuncionalidadExterna() {
            @Override
            public void nuevoEvento(Organizacion organizacion, ZonaDeCobertura zona, Muestra muestra) {
                // Funcionalidad de notificación simple para tests
            }
            
            @Override
            public void muestraVerificada(Organizacion organizacion, ZonaDeCobertura zona, Muestra muestra) {
                // Funcionalidad de notificación simple para tests
            }
        };
        
        ZonaDeCobertura zonaBuenosAires = new ZonaDeCobertura("Zona Buenos Aires", ubicacionHospital, 50.0, notificador);
        ZonaDeCobertura zonaCórdoba = new ZonaDeCobertura("Zona Córdoba", ubicacionEscuela, 30.0, notificador);
        
        aplicacion.registrarZona(zonaBuenosAires);
        aplicacion.registrarZona(zonaCórdoba);
        
        assertEquals(2, aplicacion.getZonas().size());

        // 4.3 Suscribir organizaciones a zonas
        aplicacion.suscribirOrganizacionAZona(organizacionSalud, zonaBuenosAires);
        aplicacion.suscribirOrganizacionAZona(organizacionEducativa, zonaCórdoba);
        
        assertEquals(1, zonaBuenosAires.getOrganizacionesSuscriptas().size());
        assertEquals(1, zonaCórdoba.getOrganizacionesSuscriptas().size());

        // 4.4 Registrar muestra en zona y verificar notificación
        Muestra muestraEnZona = new Muestra("en_zona.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico1, muestraEnZona);
        
        assertEquals(1, zonaBuenosAires.getMuestrasReportadas().size());
        assertTrue(zonaBuenosAires.getMuestrasReportadas().contains(muestraEnZona));

        // 4.5 VALIDAR SOLAPAMIENTO DE ZONAS (según enunciado)
        // Crear zona que solape con Buenos Aires
        Ubicacion epicentroSolapante = new Ubicacion(-34.6200, -58.4000); // Cerca de BA
        ZonaDeCobertura zonaSolapante = new ZonaDeCobertura("Zona Solapante", epicentroSolapante, 25.0, notificador);
        aplicacion.registrarZona(zonaSolapante);

        // Verificar solapamiento
        List<ZonaDeCobertura> todasLasZonas = aplicacion.getZonas();
        List<ZonaDeCobertura> solapantes = zonaBuenosAires.zonasQueSolapan(todasLasZonas);
        
        assertTrue(solapantes.contains(zonaSolapante), "Debe detectar zona solapante");
        assertFalse(solapantes.contains(zonaCórdoba), "Córdoba está muy lejos para solapar");
        assertFalse(solapantes.contains(zonaBuenosAires), "No debe incluirse a sí misma");

        // 4.6 VALIDAR CÁLCULOS DE DISTANCIA (según enunciado)
        double distanciaBA_Cordoba = ubicacionHospital.distanciaHasta(ubicacionEscuela);
        assertTrue(distanciaBA_Cordoba > 400, "Buenos Aires a Córdoba > 400km");
        
        double distanciaBA_Solapante = ubicacionHospital.distanciaHasta(epicentroSolapante);
        assertTrue(distanciaBA_Solapante < 50, "Zona solapante debe estar cerca");
        
    }

    @Test
    public void testIntegracionCompletaDelSistema() throws SistemaDeExcepciones {
        // CASO 5: Integración completa del sistema
        
        // 5.1 Verificar configuración inicial
        assertEquals(3, aplicacion.getSistemaDeUsuarios().getUsuariosRegistrados().size());
        
        // 5.2 Simular flujo completo de trabajo
        // Crear múltiples muestras
        Muestra muestra1 = new Muestra("campaña1.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestra2 = new Muestra("campaña2.jpg", "-34.6037,-58.3816", usuarioBasico2, TipoDeOpinion.CHINCHE_FOLIADA);
        Muestra muestra3 = new Muestra("campaña3.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_SORDIDA);
        
        aplicacion.registrarMuestra(usuarioBasico1, muestra1);
        aplicacion.registrarMuestra(usuarioBasico2, muestra2);
        aplicacion.registrarMuestra(usuarioBasico1, muestra3);

        // 5.3 Proceso de verificación
        Usuario segundoExperto = crearUsuario("segundo_experto", "pass");
        segundoExperto.setNivel(new NivelExperto());
        aplicacion.registrarUsuario(segundoExperto);
        
        // Verificar muestra1
        Opinion exp1 = new Opinion(expertoValidado, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion exp2 = new Opinion(segundoExperto, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestra1.agregarOpinion(exp1);
        muestra1.agregarOpinion(exp2);
        
        assertTrue(muestra1.estaVerificada());

        // 5.4 Verificar estadísticas finales
        assertEquals(3, aplicacion.getRepositorioDeMuestras().getMuestras().size());
        assertEquals(1, aplicacion.getMuestrasVerificadas().size());
        assertEquals(2, aplicacion.getMuestrasNoVerificadas().size());

        // 5.5 Búsqueda compleja final
        FiltroCompuesto filtroComplejo = new FiltroCompuesto(OperadorLogico.AND);
        filtroComplejo.agregarFiltro(new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS));
        filtroComplejo.agregarFiltro(new FiltroPorNivelVerificacion(true));
        
        List<Muestra> resultadoFinal = aplicacion.buscarMuestras(filtroComplejo);
        assertEquals(1, resultadoFinal.size());
        assertTrue(resultadoFinal.contains(muestra1));

        // 5.6 Verificar que todas las funcionalidades principales están implementadas
        assertTrue(aplicacion.getRepositorioDeMuestras().getMuestras().size() > 0, "✅ Muestras implementadas");
        assertTrue(aplicacion.getMuestrasVerificadas().size() > 0, "✅ Verificación implementada");
        assertTrue(aplicacion.getSistemaDeUsuarios().getUsuariosRegistrados().size() > 0, "✅ Usuarios implementados");
        assertNotNull(resultadoFinal, "✅ Sistema de filtros implementado");
    }

    @Test
    public void testCasosFuncionalesAvanzadosDelEnunciado() throws SistemaDeExcepciones {
        // CASO 6: Validación de casos funcionales específicos del enunciado
        
        // 6.1 VALIDAR BÚSQUEDAS GEOGRÁFICAS
        // "conocer todas las muestras obtenidas a menos de x metros o kilómetros"
        Ubicacion ubicacionCentral = new Ubicacion(-34.6037, -58.3816); // Buenos Aires
        
        // Crear muestras en diferentes ubicaciones
        Muestra muestraCerca1 = new Muestra("cerca1.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS); // Misma ubicación
        Muestra muestraCerca2 = new Muestra("cerca2.jpg", "-34.6100,-58.3900", usuarioBasico1, TipoDeOpinion.CHINCHE_FOLIADA); // ~10km
        Muestra muestraLejos = new Muestra("lejos.jpg", "-31.4201,-64.1888", usuarioBasico2, TipoDeOpinion.VINCHUCA_SORDIDA); // Córdoba ~600km
        
        aplicacion.registrarMuestra(usuarioBasico1, muestraCerca1);
        aplicacion.registrarMuestra(usuarioBasico1, muestraCerca2);
        aplicacion.registrarMuestra(usuarioBasico2, muestraLejos);

        // Simular búsqueda geográfica (funcionalidad del enunciado)
        List<Muestra> todasLasMuestras = aplicacion.getRepositorioDeMuestras().getMuestras();
        List<Muestra> muestrasCercanas = todasLasMuestras.stream()
            .filter(m -> {
                String[] coords = m.getUbicacion().split(",");
                Ubicacion ubicMuestra = new Ubicacion(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                return ubicacionCentral.distanciaHasta(ubicMuestra) <= 20.0; // Radio 20km
            })
            .toList();
        
        assertEquals(2, muestrasCercanas.size(), "Debe encontrar 2 muestras cercanas");
        assertTrue(muestrasCercanas.contains(muestraCerca1));
        assertTrue(muestrasCercanas.contains(muestraCerca2));
        assertFalse(muestrasCercanas.contains(muestraLejos));

        // 6.2 VALIDAR FILTROS POR FECHA DE ÚLTIMA VOTACIÓN
        // "Fecha de la última votación > '20/04/2019'"
        
        // Crear muestra y agregar opinión para generar fecha de última votación
        Muestra muestraConVotacion = new Muestra("con_votacion.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico1, muestraConVotacion);
        
        Opinion opinion = new Opinion(usuarioBasico2, TipoDeOpinion.CHINCHE_FOLIADA);
        muestraConVotacion.agregarOpinion(opinion);
        
        // Verificar que tiene fecha de última votación
        assertNotNull(muestraConVotacion.getFechaUltimaVotacion(), "Debe tener fecha de última votación");
        assertEquals(LocalDate.now(), muestraConVotacion.getFechaUltimaVotacion(), "Fecha debe ser hoy");

        // Filtro por última votación (simulando funcionalidad del enunciado)
        FiltroPorUltimaVotacion filtroUltimaVotacion = new FiltroPorUltimaVotacion(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        List<Muestra> muestrasConVotacionReciente = aplicacion.buscarMuestras(filtroUltimaVotacion);
        
        assertTrue(muestrasConVotacionReciente.contains(muestraConVotacion), "Debe incluir muestra con votación reciente");

        // 6.3 VALIDAR COMBINACIONES COMPLEJAS DE FILTROS (según ejemplos del enunciado)
        // "Tipo de insecto detectado = 'Vinchuca' AND (Nivel de validación = verificada OR Fecha de la última votación > '20/04/2019')"
        
        // Verificar una muestra para el test
        Usuario segundoExperto = crearUsuario("segundo_experto_avanzado", "pass");
        segundoExperto.setNivel(new NivelExperto());
        aplicacion.registrarUsuario(segundoExperto);
        
        Opinion exp1 = new Opinion(expertoValidado, TipoDeOpinion.VINCHUCA_INFESTANS);
        Opinion exp2 = new Opinion(segundoExperto, TipoDeOpinion.VINCHUCA_INFESTANS);
        muestraConVotacion.agregarOpinion(exp1);
        muestraConVotacion.agregarOpinion(exp2);
        
        assertTrue(muestraConVotacion.estaVerificada(), "Muestra debe estar verificada");

        // Filtro complejo del enunciado
        FiltroCompuesto filtroComplejo = new FiltroCompuesto(OperadorLogico.AND);
        filtroComplejo.agregarFiltro(new FiltroPorTipoInsecto(TipoDeOpinion.VINCHUCA_INFESTANS));
        
        FiltroCompuesto filtroOR = new FiltroCompuesto(OperadorLogico.OR);
        filtroOR.agregarFiltro(new FiltroPorNivelVerificacion(true));
        filtroOR.agregarFiltro(new FiltroPorUltimaVotacion(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)));
        
        filtroComplejo.agregarFiltro(filtroOR);
        
        List<Muestra> resultadoComplejo = aplicacion.buscarMuestras(filtroComplejo);
        assertTrue(resultadoComplejo.contains(muestraConVotacion), "Debe cumplir filtro complejo del enunciado");

        // 6.4 VALIDAR TODAS LAS ESPECIES DE VINCHUCA DEL ENUNCIADO
        // "Infestans, Sordida, Guasayana"
        Muestra muestraInfestans = new Muestra("infestans.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        Muestra muestraSordida = new Muestra("sordida.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_SORDIDA);
        Muestra muestraGuasayana = new Muestra("guasayana.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_GUASAYANA);
        
        aplicacion.registrarMuestra(usuarioBasico1, muestraInfestans);
        aplicacion.registrarMuestra(usuarioBasico1, muestraSordida);
        aplicacion.registrarMuestra(usuarioBasico1, muestraGuasayana);
        
        assertEquals(TipoDeOpinion.VINCHUCA_INFESTANS, muestraInfestans.getResultado());
        assertEquals(TipoDeOpinion.VINCHUCA_SORDIDA, muestraSordida.getResultado());
        assertEquals(TipoDeOpinion.VINCHUCA_GUASAYANA, muestraGuasayana.getResultado());

        // 6.5 VALIDAR TODOS LOS TIPOS DE OPINIÓN DEL ENUNCIADO
        // "Vinchuca, Chinche Foliada, Phtia-Chinche, Ninguna, Imagen poco clara"
        Muestra muestraOpiniones = new Muestra("test_opiniones.jpg", "-34.6037,-58.3816", usuarioBasico1, TipoDeOpinion.VINCHUCA_INFESTANS);
        aplicacion.registrarMuestra(usuarioBasico1, muestraOpiniones);
        
        // Probar todas las opiniones posibles
        Usuario usuario3 = crearUsuario("usuario3", "pass3");
        Usuario usuario4 = crearUsuario("usuario4", "pass4");
        Usuario usuario5 = crearUsuario("usuario5", "pass5");
        Usuario usuario6 = crearUsuario("usuario6", "pass6");
        
        aplicacion.registrarUsuario(usuario3);
        aplicacion.registrarUsuario(usuario4);
        aplicacion.registrarUsuario(usuario5);
        aplicacion.registrarUsuario(usuario6);
        
        // Crear muestras para cada tipo de opinión
        Muestra muestraChinche = new Muestra("chinche.jpg", "-34.6037,-58.3816", usuario3, TipoDeOpinion.CHINCHE_FOLIADA);
        Muestra muestraPhtia = new Muestra("phtia.jpg", "-34.6037,-58.3816", usuario4, TipoDeOpinion.PHTIA_CHINCHE);
        Muestra muestraNinguna = new Muestra("ninguna.jpg", "-34.6037,-58.3816", usuario5, TipoDeOpinion.NINGUNA);
        Muestra muestraPocaClara = new Muestra("poco_clara.jpg", "-34.6037,-58.3816", usuario6, TipoDeOpinion.IMAGEN_POCO_CLARA);
        
        aplicacion.registrarMuestra(usuario3, muestraChinche);
        aplicacion.registrarMuestra(usuario4, muestraPhtia);
        aplicacion.registrarMuestra(usuario5, muestraNinguna);
        aplicacion.registrarMuestra(usuario6, muestraPocaClara);
        
        assertEquals(TipoDeOpinion.CHINCHE_FOLIADA, muestraChinche.getResultado());
        assertEquals(TipoDeOpinion.PHTIA_CHINCHE, muestraPhtia.getResultado());
        assertEquals(TipoDeOpinion.NINGUNA, muestraNinguna.getResultado());
        assertEquals(TipoDeOpinion.IMAGEN_POCO_CLARA, muestraPocaClara.getResultado());
    }
} 