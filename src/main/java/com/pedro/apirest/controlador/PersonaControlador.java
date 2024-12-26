package com.pedro.apirest.controlador;


import com.pedro.apirest.persona.Persona;
import com.pedro.apirest.servicio.PersonaServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/personas")
@Tag(name = "Persona", description = "Operaciones relacionadas con personas")
public class PersonaControlador {

    @Autowired
    PersonaServicio personaServicio;

    @Operation(summary = "Obtener todas las personas")
    @GetMapping()
    public ResponseEntity<List<Persona>> getPersona(){
            List<Persona> personas = personaServicio.listarPersonas();
            return ResponseEntity.ok(personas);
    }

    @Operation(summary = "Obtener una persona por ID",
            responses = {
                    @ApiResponse(description = "Persona encontrada", responseCode = "200"),
                    @ApiResponse(description = "Persona no encontrada", responseCode = "404")
            })
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPersonaPorId(@PathVariable Integer id){
        Persona persona = personaServicio.buscarPersonaPorId(id);
        if (persona != null){
            return ResponseEntity.ok(persona);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada con el ID: " + id);
        }
    }

        @Operation(summary = "Crear una nueva persona")
    @PostMapping()
    public ResponseEntity<?> crearPersona(@RequestBody Persona persona){
        personaServicio.guardarPersona(persona);
        URI location = ServletUriComponentsBuilder.
                fromCurrentRequest().
                path("/{id}").
                buildAndExpand(persona.getId()).
                toUri();
        return ResponseEntity.created(location).body(persona);
    }

    @Operation(summary = "Crear varias personas",
            responses = {
                    @ApiResponse(description = "Personas creadas correctamente", responseCode = "201"),
                    @ApiResponse(description = "Errores al guardar algunas personas", responseCode = "207")
            })
    @PostMapping("/batch")
    public ResponseEntity<?> crearPersonas(@RequestBody List<Persona> personas) {
        List<Persona> personasGuardadas = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        for (Persona persona : personas) {
            try {
                personaServicio.guardarPersona(persona);
                personasGuardadas.add(persona);
            } catch (Exception e) {
                errores.add("Error al guardar persona con datos: " + persona.toString() + ". Causa: " + e.getMessage());
            }
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("personasGuardadas", personasGuardadas);
        respuesta.put("errores", errores);

        if (!errores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(respuesta);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @Operation(summary = "Actualizar una persona")
    @PutMapping()
    public ResponseEntity<?> actualizarPersona(@RequestBody Persona personaActualizada){
        Persona personaExistente = personaServicio.buscarPersonaPorId(personaActualizada.getId());
        if(personaExistente == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada con ID: " + personaActualizada.getId());
        }
        personaExistente.setFirstName(personaActualizada.getFirstName());
        personaExistente.setLastName(personaActualizada.getLastName());
        personaExistente.setEmail(personaActualizada.getEmail());

        personaServicio.guardarPersona(personaExistente);

        return ResponseEntity.ok(personaExistente);
    }

    @Operation(summary = "Actualizar varias personas")
    @PutMapping("/batch")
    public ResponseEntity<?> actualizarPersonas(@RequestBody List<Persona> personasActualizadas) {
        List<Persona> personasActualizadasExitosamente = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        for (Persona personaActualizada : personasActualizadas) {
            try {
                Persona personaExistente = personaServicio.buscarPersonaPorId(personaActualizada.getId());
                if (personaExistente == null) {
                    errores.add("Persona no encontrada con ID: " + personaActualizada.getId());
                    continue;
                }

                personaExistente.setFirstName(personaActualizada.getFirstName());
                personaExistente.setLastName(personaActualizada.getLastName());
                personaExistente.setEmail(personaActualizada.getEmail());

                personaServicio.guardarPersona(personaExistente);
                personasActualizadasExitosamente.add(personaExistente);

            } catch (Exception e) {
                errores.add("Error al actualizar persona con ID: " + personaActualizada.getId() + ". Causa: " + e.getMessage());
            }
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("personasActualizadas", personasActualizadasExitosamente);
        respuesta.put("errores", errores);

        if (!errores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(respuesta);
        }

        return ResponseEntity.ok(respuesta);
    }

    @Operation(summary = "Eliminar una persona")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPersona(@PathVariable Integer id){
        Persona persona = personaServicio.buscarPersonaPorId(id);
        if (persona == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada con ID: " + id);
        }
        personaServicio.eliminarPersona(persona);

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar varias personas")
    @DeleteMapping("/batch")
    public ResponseEntity<?> eliminarPersonas(@RequestBody List<Integer> ids) {
        List<Persona> personasEliminadas = new ArrayList<>();
        List<String> errores = new ArrayList<>();

        for (Integer id : ids) {
            Persona persona = personaServicio.buscarPersonaPorId(id);
            if (persona == null) {
                errores.add("Persona no encontrada con ID: " + id);
            } else {
                personaServicio.eliminarPersona(persona);
                personasEliminadas.add(persona);
            }
        }

        if (!errores.isEmpty()) {
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("errores", errores);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }

        return ResponseEntity.noContent().build();
    }

}
