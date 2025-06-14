package ar.edu.unq.vinchucas.filtros;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OperadorStrategyFactoryTest {

    @Test
    public void testCrearOperadorAND() {
        OperadorStrategy operador = OperadorStrategyFactory.crearOperador(OperadorLogico.AND);
        
        assertNotNull(operador);
        assertTrue(operador instanceof OperadorAND);
    }

    @Test
    public void testCrearOperadorOR() {
        OperadorStrategy operador = OperadorStrategyFactory.crearOperador(OperadorLogico.OR);
        
        assertNotNull(operador);
        assertTrue(operador instanceof OperadorOR);
    }

    @Test
    public void testCrearOperadorConNull() {
        // Este test cubre el caso donde se pasa null
        assertThrows(NullPointerException.class, () -> {
            OperadorStrategyFactory.crearOperador(null);
        });
    }
} 