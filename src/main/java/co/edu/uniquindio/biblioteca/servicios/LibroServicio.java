package co.edu.uniquindio.biblioteca.servicios;

import co.edu.uniquindio.biblioteca.dto.AutorGet;
import co.edu.uniquindio.biblioteca.dto.LibroDTO;
import co.edu.uniquindio.biblioteca.dto.LibroGet;
import co.edu.uniquindio.biblioteca.entity.Autor;
import co.edu.uniquindio.biblioteca.entity.Libro;
import co.edu.uniquindio.biblioteca.repo.AutorRepo;
import co.edu.uniquindio.biblioteca.repo.LibroRepo;
import co.edu.uniquindio.biblioteca.servicios.excepciones.AutorNoEncontradoException;
import co.edu.uniquindio.biblioteca.servicios.excepciones.LibroNoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LibroServicio {

    private final LibroRepo libroRepo;
    private final AutorRepo autorRepo;

    public LibroGet save(LibroDTO libro){

        Optional<Libro> guardado = libroRepo.findById(libro.isbn());

        if(guardado.isPresent()){
            throw new RuntimeException("El libro con el isbn "+libro.isbn()+" ya existe");
        }
        if (libro.idAutores().isEmpty()){
            throw new RuntimeException("El libro no tiene ningun autor");
        }
        return convertir(libroRepo.save(convertir(libro)));
    }

    public LibroGet findById(String isbn){
        return convertir(obtenerLibro(isbn));
    }

    public List<LibroGet> findAll(){
        return libroRepo.findAll()
                .stream()
                .map(l -> convertir(l))
                .collect(Collectors.toList());
    }

    public LibroGet update(String isbn, LibroDTO libro){
        obtenerLibro(isbn);

        Libro libroNuevo = convertir(libro);
        libroNuevo.setIsbn(isbn);
        return convertir(libroRepo.save(libroNuevo));
    }

    public void delete(String isbn) {
        obtenerLibro(isbn);
        libroRepo.deleteById(isbn);
    }

    private Libro obtenerLibro(String isbn) {
        return libroRepo.findById(isbn).orElseThrow(()-> new LibroNoEncontradoException("El libro no existe"));
    }

    private Libro convertir(LibroDTO libro){
        Libro lib = Libro.builder()
                .isbn(libro.isbn())
                .nombre(libro.nombre())
                .genero(libro.genero())
                .fechaPublicacion(libro.fechaPublicacion())
                .unidades(libro.unidades())
                .autor(obtenerAutores(libro.idAutores()))
                .build();
        return lib;
    }

    private LibroGet convertir(Libro libro) {
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

    private List<Autor> obtenerAutores(List<Long> idAutores) {

        if (idAutores.isEmpty()){
            return null;
        }
        List<Autor> autores = autorRepo.findAllById( idAutores );

        if(autores.size()!=idAutores.size()){
            String noEncontrados = idAutores
                    .stream()
                    .filter(id -> !autores.stream()
                            .map(Autor::getId).toList()
                            .contains(id))
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            throw new AutorNoEncontradoException("Los autores "+noEncontrados+" no existen");
        }
        return autores;
    }

}