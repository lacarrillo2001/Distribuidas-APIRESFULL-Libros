package com.espe.test.test.controllers;

import com.espe.test.test.clientes.AutorClienteRest;
import com.espe.test.test.models.dto.AutorDTO;
import com.espe.test.test.models.entities.Libros;
import com.espe.test.test.services.LibroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    @Autowired
    private LibroService service;

    @Autowired
    private AutorClienteRest autorClienteRest;

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
    public ResponseEntity<?> crear(@Valid @RequestBody Libros libro) {
        try {
            // Buscar el autor por ID usando el cliente REST
            Optional<AutorDTO> autorDTO = autorClienteRest.buscarPorId(libro.getAutorId());

            if (autorDTO.isPresent()) {
                // Poblar el campo autor con el nombre del AutorDTO
                libro.setAutorId(autorDTO.get().getId());

                // Guardar el libro con todos los campos completos
                Libros libroDB = service.guardar(libro);
                return ResponseEntity.status(HttpStatus.CREATED).body(libroDB);
            } else {
                return ResponseEntity.badRequest()
                        .body("Autor con ID " + libro.getAutorId() + " no encontrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear el libro: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Libros libro, @PathVariable Long id){
        Optional<Libros> libroOptional = service.buscarPorId(id);
        if (libroOptional.isPresent()){
            Libros libroDB = libroOptional.get();
            libroDB.setTitulo(libro.getTitulo());
            libroDB.setAutorId(libro.getAutorId());
            libroDB.setGenero(libro.getGenero());
            service.guardar(libroDB);
            EntityModel<Libros> resource = EntityModel.of(libroDB,
                    linkTo(methodOn(LibroController.class).obtenerPorId(libroDB.getId())).withSelfRel(),
                    linkTo(methodOn(LibroController.class).listar()).withRel("libros"));

            Map<String, Object> body = new HashMap<>();
            body.put("message", "Libro actualizado exitosamente");
            body.put("data", resource);

            return ResponseEntity.ok(body);
        }

        Map<String, Object> notFound = new HashMap<>();
        notFound.put("message", "Libro no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Libros> libroOptional = service.buscarPorId(id);
        if (libroOptional.isPresent()) {
            service.eliminarPorId(id);
            Map<String, Object> body = new HashMap<>();
            body.put("message", "Libro eliminado exitosamente");
            body.put("id", id);
            return ResponseEntity.ok(body);
        }
        Map<String, Object> notFound = new HashMap<>();
        notFound.put("message", "Libro no encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
    }
}
