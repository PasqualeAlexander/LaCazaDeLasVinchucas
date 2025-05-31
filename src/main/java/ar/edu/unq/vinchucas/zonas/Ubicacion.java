package ar.edu.unq.vinchucas.zonas;

public class Ubicacion {
    private final double latitud;
    private final double longitud;

    public Ubicacion(String coordenadas) {
        String[] partes = coordenadas.split(",");
        this.latitud = Double.parseDouble(partes[0].trim());
        this.longitud = Double.parseDouble(partes[1].trim());
    }

    public Ubicacion(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public double distanciaHasta(Ubicacion otra) {
        // Fórmula de Haversine para calcular distancia entre dos puntos en la Tierra
        double radioTierra = 6371; // Radio de la Tierra en kilómetros
        
        double dLat = Math.toRadians(otra.latitud - this.latitud);
        double dLon = Math.toRadians(otra.longitud - this.longitud);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(this.latitud)) * Math.cos(Math.toRadians(otra.latitud)) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        
        return radioTierra * c;
    }
} 