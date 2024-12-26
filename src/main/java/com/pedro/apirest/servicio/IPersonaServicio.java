package com.pedro.apirest.servicio;

import com.pedro.apirest.persona.Persona;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IPersonaServicio {
    public List<Persona> listarPersonas();

    public Persona buscarPersonaPorId(Integer id);

    public void guardarPersona(Persona persona);

    public void eliminarPersona(Persona persona);
}
