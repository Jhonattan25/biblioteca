package co.edu.uniquindio.biblioteca.controller;

import co.edu.uniquindio.biblioteca.dto.LibroDTO;
import co.edu.uniquindio.biblioteca.dto.LibroGet;
import co.edu.uniquindio.biblioteca.dto.Respuesta;
import co.edu.uniquindio.biblioteca.servicios.LibroServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libro")
@AllArgsConstructor
public class LibroController {

    private final LibroServicio libroServicio;

    @PostMapping
    public ResponseEntity<Respuesta<LibroGet>> save(@RequestBody LibroDTO libroDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body( new Respuesta<>("Libro creado correctamente", libroServicio.save(libroDTO)) );
    }

    @GetMapping
    public ResponseEntity<Respuesta<List<LibroGet>>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("OK", libroServicio.findAll()) );
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<Respuesta<LibroGet>> findByIsbn(@PathVariable String isbn){
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("OK", libroServicio.findById(isbn)) );
    }

    @PutMapping("/{isbn}")
    public ResponseEntity<Respuesta<LibroGet>> update(@PathVariable String isbn, @RequestBody LibroDTO libroDTO) {
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("Libro correctamente", libroServicio.update(isbn, libroDTO)) );
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Respuesta<String>> delete(@PathVariable String isbn) {
        libroServicio.delete(isbn);
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("Libro eliminado correctamente") );
    }
}