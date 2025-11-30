package com.espe.test.test.services;

import com.espe.test.test.models.entities.Libros;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LibroService {

    Optional<Libros> buscarPorId(Long id);

    @Transactional(readOnly = true)
    List<Libros> buscarTodos();

    List<Libros> buscarTodos(String autor);

    Libros guardar(Libros libro);
    void eliminarPorId(Long id);
}
