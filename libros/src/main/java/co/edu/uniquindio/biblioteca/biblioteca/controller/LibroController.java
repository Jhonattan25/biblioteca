package co.edu.uniquindio.biblioteca.biblioteca.controller;

import co.edu.uniquindio.biblioteca.biblioteca.dto.LibroDTO;
import co.edu.uniquindio.biblioteca.biblioteca.dto.LibroISBNDTO;
import co.edu.uniquindio.biblioteca.biblioteca.dto.Respuesta;
import co.edu.uniquindio.biblioteca.biblioteca.servicio.LibroServicio;
import co.edu.uniquindio.biblioteca.biblioteca.model.Libro;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libro")
public class LibroController {
    public LibroController(LibroServicio libroServicio) {
        this.libroServicio = libroServicio;
    }

    @Value("${eureka.instance.instance-id}")
    private String port;

    private final LibroServicio libroServicio;

    @PostMapping
    public ResponseEntity<Respuesta<Libro>> save(@RequestBody LibroDTO libroDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body( new Respuesta<>("Libro creado correctamente", libroServicio.save(libroDTO)) );
    }

    @GetMapping
    public ResponseEntity<Respuesta<List<Libro>>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("", libroServicio.findAll()) );
    }

    @PutMapping("/{isbnLibro}")
    public ResponseEntity<Respuesta<Libro>> update(@PathVariable String isbnLibro,
                                                   @RequestBody LibroDTO libroDTO) {
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>(
                "Libro actualizado correctamente", libroServicio.update(isbnLibro, libroDTO)) );
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Respuesta<String>> delete(@PathVariable String isbn) {
        libroServicio.delete(isbn);
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>(
                "Libro eliminado correctamente") );
    }

    @PostMapping("/isbns")
    public ResponseEntity<Respuesta<Boolean>> isbnsExist(@RequestBody LibroISBNDTO libroISBNDTO){
        System.out.println(port);

        return ResponseEntity.status(HttpStatus.CREATED).body( new Respuesta<>(
                "ISBNs existentes", libroServicio.isbnsExist(libroISBNDTO)) );
    }
}
