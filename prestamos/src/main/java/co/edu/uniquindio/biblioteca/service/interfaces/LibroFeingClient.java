package co.edu.uniquindio.biblioteca.service.interfaces;

import co.edu.uniquindio.biblioteca.dto.LibroISBNDTO;
import co.edu.uniquindio.biblioteca.dto.Respuesta;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "libro-service", path = "/api/libro")
public interface LibroFeingClient {
    @PostMapping("/isbns")
    Respuesta<Boolean> isbnsExist(@RequestBody LibroISBNDTO libroISBNDTO);
}
