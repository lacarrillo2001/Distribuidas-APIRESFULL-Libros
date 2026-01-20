// src/main/java/com/espe/test/test/models/entities/Libros.java
package com.espe.test.test.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "libro")
public class Libros extends RepresentationModel<Libros> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[\\p{L}\\p{N} :]+$", message = "No se permiten caracteres especiales")
    private String titulo;

    @Column(name = "autor_id")
    @NotNull
    private Long autor_id;




    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[\\p{L}\\p{N} ]+$", message = "No se permiten caracteres especiales")
    private String genero;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @JsonProperty("autor_id")
    public Long getAutorId() {
        return autor_id;
    }
    public void setAutorId(Long autor_id) {
        this.autor_id = autor_id;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
