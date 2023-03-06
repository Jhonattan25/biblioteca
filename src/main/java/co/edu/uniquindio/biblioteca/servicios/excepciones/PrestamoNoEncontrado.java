package co.edu.uniquindio.biblioteca.servicios.excepciones;

public class PrestamoNoEncontrado extends RuntimeException {
    public PrestamoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}

