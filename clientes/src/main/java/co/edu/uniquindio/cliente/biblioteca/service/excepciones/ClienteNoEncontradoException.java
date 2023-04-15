package co.edu.uniquindio.cliente.biblioteca.service.excepciones;

public class ClienteNoEncontradoException extends RuntimeException{

    public ClienteNoEncontradoException(String mensaje){
        super(mensaje);
    }

}
