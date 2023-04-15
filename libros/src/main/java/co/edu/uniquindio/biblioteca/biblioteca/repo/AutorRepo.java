package co.edu.uniquindio.biblioteca.biblioteca.repo;

import co.edu.uniquindio.biblioteca.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepo extends JpaRepository<Autor, Long> {
}