package com.espe.test.test.clientes;

import com.espe.test.test.models.dto.AutorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "Autor", url = "http://${API_AUTOR:localhost}:8002/api/autores")
//@FeignClient(name = "Autor", url = "http://autores:8002/api/autores")
public interface AutorClienteRest {
    @GetMapping("/{id}")
    Optional<AutorDTO> buscarPorId(@PathVariable("id")  Long id);
}
