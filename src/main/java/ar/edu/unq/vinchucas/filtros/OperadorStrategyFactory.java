package ar.edu.unq.vinchucas.filtros;

public class OperadorStrategyFactory {
    
    public static OperadorStrategy crearOperador(OperadorLogico operador) {
        return switch (operador) {
            case AND -> new OperadorAND();
            case OR -> new OperadorOR();
        };
    }
} 