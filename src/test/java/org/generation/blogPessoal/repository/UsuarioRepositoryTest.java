package org.generation.blogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
    void start() {

        usuarioRepository.deleteAll();

        usuarioRepository.save(new Usuario(0L,"Lucas Oliveira", "FOTO", "lucass","5432213"));

        usuarioRepository.save(new Usuario(0L,"Savio Oliveira", "FOTO", "savio","1323443"));

        usuarioRepository.save(new Usuario(0L,"Paula Oliveira", "FOTO", "paula","12331234"));

        usuarioRepository.save(new Usuario(0L,"Bruna Oliveira", "FOTO", "brunex","1323443"));

    }
    @Test
    @DisplayName("Retorna 1 usuario")
    public void deveRetornarUmUsuario() {

        Optional<Usuario> usuario = usuarioRepository.findByUsuario("Oliveira");
        assertTrue(usuario.get().getUsuario().equals("Oliveira"));

    }
    @Test
    @DisplayName("Retorna 3 usuarios")
    public void deveRetornarTresUsuarios() {
        List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Oliveira");

        assertEquals(3, listaDeUsuarios.size());
        assertTrue(listaDeUsuarios.get(0).getNome().equals("Lucas Oliveira"));
        assertTrue(listaDeUsuarios.get(1).getNome().equals("Savio Oliveira"));
        assertTrue(listaDeUsuarios.get(2).getNome().equals("Paula Oliveira"));
    }
}