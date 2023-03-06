package co.edu.uniquindio.biblioteca.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PrestamoGet(ClienteGet cliente, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion,
                          List<LibroGet> libros) {
}
