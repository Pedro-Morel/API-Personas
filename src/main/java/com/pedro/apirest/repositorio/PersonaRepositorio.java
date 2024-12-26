package com.pedro.apirest.repositorio;

import com.pedro.apirest.persona.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepositorio extends JpaRepository<Persona, Integer> {
}
