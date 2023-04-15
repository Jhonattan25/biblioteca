package co.edu.uniquindio.biblioteca.service;

import co.edu.uniquindio.biblioteca.dto.*;
import co.edu.uniquindio.biblioteca.model.Prestamo;
import co.edu.uniquindio.biblioteca.repo.PrestamoRepo;
import co.edu.uniquindio.biblioteca.service.excepciones.PrestamoNoEncontradoException;
import co.edu.uniquindio.biblioteca.service.interfaces.ClienteFeingClient;
import co.edu.uniquindio.biblioteca.service.interfaces.LibroFeingClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PrestamoServicio {
    private final LibroFeingClient libroFeingClient;

    private final ClienteFeingClient clienteFeingClient;

    private final PrestamoRepo prestamoRepo;
    public Prestamo save(PrestamoPostDTO prestamoDTO){

        ClienteGetDTO cliente = findClienteByCodigo(prestamoDTO.clienteID());
        log.info("cliente {}", cliente);

        isbnsExist(prestamoDTO.isbnLibros());

        Prestamo prestamo = new Prestamo();
        prestamo.setCodigoCliente(cliente.codigo());
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setIsbnLibros(prestamoDTO.isbnLibros());
        prestamo.setFechaDevolucion(prestamoDTO.fechaDevolucion());

        return prestamoRepo.save(prestamo);
    }

    public List<Prestamo> findByCodigoCliente(String codigoCliente){

        ClienteGetDTO cliente = findClienteByCodigo(codigoCliente);
        log.info("cliente {}", cliente);

        List<PrestamoQueryDTO> lista = prestamoRepo.findByCodigoCliente(codigoCliente);
        List<Prestamo> respuesta = new ArrayList<>();

        for(PrestamoQueryDTO q : lista){
            if(respuesta.stream().noneMatch(r -> r.getCodigo() == q.getPrestamoID())){
                ArrayList<String> libros = new ArrayList<>();
                libros.add(q.getIsbnLibro());
                respuesta.add( new Prestamo(q.getPrestamoID(), q.getClienteID(), q.getFechaCreacion(), q.getFechaDevolucion(), libros ) );
            }else{
                respuesta.stream().findAny().get().getIsbnLibros().add( q.getIsbnLibro() );
            }
        }

        return new ArrayList<>();

    }

    public List<Prestamo> findAll(){
        return prestamoRepo.findAll();
    }

    public Prestamo findById(long codigoPrestamo){
        return prestamoRepo.findById(codigoPrestamo).orElseThrow(()-> new PrestamoNoEncontradoException("No existe un préstamo con el código: "+codigoPrestamo));
    }

    public List<Prestamo> findByDate(LocalDate codigoPrestamo){
        return prestamoRepo.findByDate(codigoPrestamo);
    }

    public long lendingCount(String isbn){
        return prestamoRepo.lendingCount(isbn);
    }

    public Prestamo update(long codigoPrestamo, PrestamoPostDTO prestamoPostDTO){
        Prestamo prestamo = prestamoRepo.findById(codigoPrestamo).orElseThrow(()-> new PrestamoNoEncontradoException("No existe un préstamo con el código: "+codigoPrestamo));

        isbnsExist(prestamoPostDTO.isbnLibros());

        prestamo.setIsbnLibros(prestamoPostDTO.isbnLibros());
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setFechaDevolucion(prestamoPostDTO.fechaDevolucion());

        return prestamoRepo.save(prestamo);
    }

    private void isbnsExist(List<String> isbns){
        LibroISBNDTO libroISBNDTO = new LibroISBNDTO(isbns);
        HttpEntity<LibroISBNDTO> request = new HttpEntity<>(libroISBNDTO);

        try {
            Respuesta<Boolean>respuesta = libroFeingClient.isbnsExist(libroISBNDTO);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Hubo un error recuperando la información de los libros 8888");
        }
    }

    private ClienteGetDTO findClienteByCodigo(String codigoCliente){
        try {

            Respuesta<ClienteGetDTO> respuesta= clienteFeingClient.findById(codigoCliente);

            return respuesta.getDato();

        }catch (Exception e){
            System.out.println(e.getMessage());

            throw new RuntimeException("Hubo un error recuperando la información del cliente");
        }
    }

}
