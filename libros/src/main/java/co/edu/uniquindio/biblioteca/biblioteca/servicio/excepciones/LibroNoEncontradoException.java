package co.edu.uniquindio.biblioteca.biblioteca.servicio.excepciones;

public class LibroNoEncontradoException extends RuntimeException {

    public LibroNoEncontradoException(String message) {
        super(message);
    }
}