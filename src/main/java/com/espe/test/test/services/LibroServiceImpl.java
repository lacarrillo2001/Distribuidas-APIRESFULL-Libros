package com.espe.test.test.services;

import com.espe.test.test.models.entities.Libros;
import com.espe.test.test.repositories.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LibroServiceImpl implements LibroService {
    @Autowired
    private LibroRepository repository;

    @Transactional(readOnly = true)
    @Override
    public List<Libros> buscarTodos() {
        return (List<Libros>) repository.findAll();
    }

    @Override
    public List<Libros> buscarTodos(String autor) {
        return List.of();
    }


    @Transactional
    @Override
    public Libros guardar(Libros libro) {
        return repository.save(libro);
    }

    @Transactional
    @Override
    public void eliminarPorId(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Libros> buscarPorId(Long id) {
        return repository.findById(id);
    }
}