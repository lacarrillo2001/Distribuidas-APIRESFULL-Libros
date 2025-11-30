package com.espe.test.test.repositories;

import com.espe.test.test.models.entities.Libros;
import org.springframework.data.repository.CrudRepository;

public interface LibroRepository extends CrudRepository<Libros, Long> {

}
