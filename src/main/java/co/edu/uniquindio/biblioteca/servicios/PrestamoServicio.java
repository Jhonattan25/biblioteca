package co.edu.uniquindio.biblioteca.servicios;

import co.edu.uniquindio.biblioteca.dto.*;
import co.edu.uniquindio.biblioteca.entity.Autor;
import co.edu.uniquindio.biblioteca.entity.Cliente;
import co.edu.uniquindio.biblioteca.entity.Libro;
import co.edu.uniquindio.biblioteca.entity.Prestamo;
import co.edu.uniquindio.biblioteca.repo.ClienteRepo;
import co.edu.uniquindio.biblioteca.repo.LibroRepo;
import co.edu.uniquindio.biblioteca.repo.PrestamoRepo;
import co.edu.uniquindio.biblioteca.servicios.excepciones.ClienteNoEncontradoException;
import co.edu.uniquindio.biblioteca.servicios.excepciones.LibroNoEncontradoException;
import co.edu.uniquindio.biblioteca.servicios.excepciones.PrestamoNoEncontrado;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PrestamoServicio {

    private final PrestamoRepo prestamoRepo;
    private final ClienteRepo clienteRepo;
    private final LibroRepo libroRepo;

    public PrestamoGet save(PrestamoDTO prestamoDTO) {
        return convertir(prestamoRepo.save(convertir(prestamoDTO)));
    }

    public PrestamoGet findById(long codigoPrestamo) {
        return convertir(obtenerPrestamo(codigoPrestamo));
    }

    public List<PrestamoGet> findAll() {
        return prestamoRepo.findAll()
                .stream()
                .map(p -> convertir(p))
                .collect(Collectors.toList());
    }

    public PrestamoGet update(long codigo, PrestamoDTO prestamo) {
        obtenerPrestamo(codigo);

        Prestamo prestamoNuevo = convertir(prestamo);
        prestamoNuevo.setCodigo(codigo);
        return convertir(prestamoNuevo);
    }

    public void delete(long codigo) {
        obtenerPrestamo(codigo);
        prestamoRepo.deleteById(codigo);
    }


    private PrestamoGet convertir(Prestamo prestamo) {
        return PrestamoGet.builder()
                .cliente(convertirClient(prestamo.getCliente()))
                .fechaPrestamo(prestamo.getFechaPrestamo())
                .fechaDevolucion(prestamo.getFechaDevolucion())
                .libros(convertirLibros(prestamo.getLibros()))
                .build();
    }

    public List<LibroGet> convertirLibros(List<Libro> libros) {
        return libros.stream()
                .map(l -> convertirLibro(l))
                .collect(Collectors.toList());
    }

    private LibroGet convertirLibro(Libro libro) {
        return LibroGet.builder()
                .isbn(libro.getIsbn())
                .nombre(libro.getNombre())
                .genero(libro.getGenero())
                .unidades(libro.getUnidades())
                .autores(convertirAutores(libro.getAutor()))
                .fechaPublicacion(libro.getFechaPublicacion())
                .build();
    }

    private List<AutorGet> convertirAutores(List<Autor> autores) {
        return autores.stream()
                .map(a -> convertirAutor(a))
                .collect(Collectors.toList());
    }

    private AutorGet convertirAutor(Autor autor) {
        return AutorGet.builder()
                .nombre(autor.getNombre())
                .build();
    }

    private ClienteGet convertirClient(Cliente cliente) {
        return new ClienteGet(cliente.getCodigo(), cliente.getNombre(), cliente.getEmail(), cliente.getTelefono());
    }

    private Prestamo convertir(PrestamoDTO prestamoDTO) {
        return Prestamo.builder()
                .cliente(obtenerCliente(prestamoDTO.clienteID()))
                .fechaPrestamo(LocalDateTime.now())
                .fechaDevolucion(prestamoDTO.fechaDevolucion())
                .libros(obtenerLibros(prestamoDTO.isbnLibros()))
                .build();
    }

    private List<Libro> obtenerLibros(List<String> isbnLibros) {
        List<Libro> libros = libroRepo.findAllById(isbnLibros);

        if (libros.size() != isbnLibros.size()) {
            String noEncontrados = isbnLibros
                    .stream()
                    .filter(isbn -> !libros.stream()
                            .map(Libro::getIsbn).toList()
                            .contains(isbn))
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            throw new LibroNoEncontradoException("Los libros " + noEncontrados + " no existen");
        }
        return libros;
    }

    private Prestamo obtenerPrestamo(long codigo) {
        return prestamoRepo.findById(codigo).orElseThrow(() -> new PrestamoNoEncontrado("El prestamo no existe"));
    }

    private Cliente obtenerCliente(long codigo) {
        return clienteRepo.findById(codigo).orElseThrow(() -> new ClienteNoEncontradoException("El cliente no existe"));
    }
}