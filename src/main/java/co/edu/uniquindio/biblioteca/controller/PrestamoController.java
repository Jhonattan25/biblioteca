package co.edu.uniquindio.biblioteca.controller;

import co.edu.uniquindio.biblioteca.dto.PrestamoDTO;
import co.edu.uniquindio.biblioteca.dto.PrestamoGet;
import co.edu.uniquindio.biblioteca.dto.Respuesta;
import co.edu.uniquindio.biblioteca.servicios.PrestamoServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prestamo")
@AllArgsConstructor
public class PrestamoController {

    private final PrestamoServicio prestamoServicio;

    @PostMapping
    public ResponseEntity<Respuesta<PrestamoGet>> save(@RequestBody PrestamoDTO prestamo){
        return ResponseEntity.status(HttpStatus.CREATED).body( new Respuesta<>("Prestamo creado correctamente", prestamoServicio.save(prestamo)) );
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Respuesta<PrestamoGet>> findByCodigo(@PathVariable long codigo) {
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("OK", prestamoServicio.findById(codigo)) );
    }

    @GetMapping
    public ResponseEntity<Respuesta<List<PrestamoGet>>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("OK", prestamoServicio.findAll()) );
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Respuesta<PrestamoGet>> update(@PathVariable long codigo, @RequestBody PrestamoDTO prestamo){
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("Prestamo actualizado correctamente", prestamoServicio.update(codigo, prestamo)) );
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Respuesta<String>> delete(@PathVariable long codigo) {
        return ResponseEntity.status(HttpStatus.OK).body( new Respuesta<>("Prestamo eliminado correctamente") );
    }
}
