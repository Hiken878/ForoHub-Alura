package com.alura.foro.controller;

import com.alura.foro.domain.topico.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping(path = "/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    // Metodo para registrar nuevo topico
    @PostMapping
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(@RequestBody DatosRegistroTopico datosRegistroTopico, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = topicoRepository.save( new Topico(datosRegistroTopico));
        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(topico.getTitulo(), topico.getMensaje(), topico.getAutor(), topico.getCurso());
        //Url para registrar servidor en donde se encuentra la base de datos con parametro dinamico id expresado entre llaves {]
        URI url = uriComponentsBuilder.path("/topico/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }
// Volviendo responsivo los datos del listado del topico
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopicos>> listadoTopicos(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAll(paginacion).map(DatosListadoTopicos::new));
    }
// Metodo para devolver los datos completos del topico
    @GetMapping(path = "/{id}")
    public ResponseEntity<DatosListadoTopicos> topicoPorId(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        var datosTopico = new DatosListadoTopicos(topico.getTitulo(),
                topico.getMensaje(), topico.getFecha_creacion().toLocalDate(),
                topico.getStatus().toString(), topico.getAutor(), topico.getCurso());
        return ResponseEntity.ok(datosTopico);
    }
// Metodo para actualizar topico
    @PutMapping(path = "/{id}")
    @Transactional
    public ResponseEntity actualizarTopico (@PathVariable Long id, @RequestBody DatosActualizarTopico datosActualizarTopico){
        Topico topico = topicoRepository.getReferenceById(id);
        topico.actualizarDatos(datosActualizarTopico);
        return ResponseEntity.ok(new DatosRespuestaTopico(topico.getTitulo(),
                topico.getMensaje(),
                topico.getAutor(),
                topico.getCurso()));
    }
    //metodo para borrar topicos
    @DeleteMapping(path = "/{id}")
    @Transactional
    public ResponseEntity topicoCerrado(@PathVariable Long id){
        Topico topico = topicoRepository.getReferenceById(id);
        topico.cerrarTopico();
        return ResponseEntity.noContent().build();

    }


}
