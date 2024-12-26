package com.pedro.apirest.servicio;

import com.pedro.apirest.persona.Persona;
import com.pedro.apirest.repositorio.PersonaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaServicio implements IPersonaServicio{

    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Override
    public List<Persona> listarPersonas() {
        return personaRepositorio.findAll();
    }

    @Override
    public Persona buscarPersonaPorId(Integer id) {
        Persona persona = personaRepositorio.findById(id).orElse(null);
        return persona;
    }

    @Override
    public void guardarPersona(Persona persona) {
        personaRepositorio.save(persona);

    }

    @Override
    public void eliminarPersona(Persona persona) {
        personaRepositorio.delete(persona);
    }
}
