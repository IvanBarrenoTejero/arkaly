package com.arkaly.arkalybackend.repositories;

import com.arkaly.arkalybackend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// <Usuario, Integer> Usuario para saber que devolver y luego Integer, para saber que tipo de pk
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);


    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}