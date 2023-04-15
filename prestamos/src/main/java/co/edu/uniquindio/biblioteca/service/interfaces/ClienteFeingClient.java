package co.edu.uniquindio.biblioteca.service.interfaces;

import co.edu.uniquindio.biblioteca.dto.ClienteGetDTO;
import co.edu.uniquindio.biblioteca.dto.Respuesta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "cliente-service", url = "http://gateway-service:8080/api/cliente")
public interface ClienteFeingClient {
    @GetMapping("/{idCliente}")
    Respuesta<ClienteGetDTO> findById(@PathVariable String idCliente);

}
