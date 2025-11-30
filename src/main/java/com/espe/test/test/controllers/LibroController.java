package com.espe.test.test.controllers;

import com.espe.test.test.models.entities.Libros;
import com.espe.test.test.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService service;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Libros>>> listar() {
        List<EntityModel<Libros>> libros = service.buscarTodos().stream()
                .map(libro -> EntityModel.of(libro,
                        linkTo(methodOn(LibroController.class).obtenerPorId(libro.getId())).withSelfRel(),
                        linkTo(methodOn(LibroController.class).listar()).withRel("libros")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(libros, linkTo(methodOn(LibroController.class).listar()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Libros>> obtenerPorId(@PathVariable Long id) {
        Optional<Libros> libroOptional = service.buscarPorId(id);

        return libroOptional.map(libro -> EntityModel.of(libro,
                        linkTo(methodOn(LibroController.class).obtenerPorId(id)).withSelfRel(),
                        linkTo(methodOn(LibroController.class).listar()).withRel("libros")))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Libros libro) {
        Libros libroDB = service.guardar(libro);
        System.out.println("Acción de guardar/actualizar completada para el libro con ID: " + libro.getId());
        EntityModel<Libros> resource = EntityModel.of(libroDB,
                linkTo(methodOn(LibroController.class).obtenerPorId(libroDB.getId())).withSelfRel(),
                linkTo(methodOn(LibroController.class).listar()).withRel("libros"));
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?>editar(@RequestBody Libros libro,@PathVariable Long id){
        Optional<Libros>LibroOptional = service.buscarPorId(id);
        if (LibroOptional.isPresent()){
            Libros LibroDB = LibroOptional.get();
            LibroDB.setTitulo(libro.getTitulo());
            LibroDB.setAutor(libro.getAutor());
            LibroDB.setGenero(libro.getGenero());
            service.guardar(LibroDB);
            EntityModel<Libros> resource = EntityModel.of(LibroDB,
                    linkTo(methodOn(LibroController.class).obtenerPorId(LibroDB.getId())).withSelfRel(),
                    linkTo(methodOn(LibroController.class).listar()).withRel("libros"));
            return ResponseEntity.status(HttpStatus.CREATED).body(resource);
        }

        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Libros> libroOptional = service.buscarPorId(id);
        if (libroOptional.isPresent()) {
            service.eliminarPorId(id);
            System.out.println("Acción de eliminar completada para el libro con ID: " + id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
