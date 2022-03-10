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
	private UsuarioRepository repository;

	@BeforeAll
	void start() {
		repository.deleteAll();

		repository.save(new Usuario("Lucas Oliveira", "lucass", "123456"));
		repository.save(new Usuario("Savio de Oliveira", "savim25", "987654"));
		repository.save(new Usuario("Paula Oliveira", "paulinha23", "987654"));
	}

	@Test
	@DisplayName("Teste FindByUsuario")
	void searchUserReturnTrue() {

		Optional<Usuario> optional = repository.findByUsuario("lucass");

		assertTrue(optional.get().getUsuario().equals("lucass"));
	}

	@Test
	@DisplayName("Teste FindAll")
	void searchAllReturnTreeUsers() {

		List<Usuario> list = repository.findAllByNomeContainingIgnoreCase("Oliveira");

		assertEquals(3, list.size());
		assertTrue(list.get(0).getNome().equals("Lucas Oliveira"));
		assertTrue(list.get(1).getNome().equals("Savio de Oliveira"));
		assertTrue(list.get(2).getNome().equals("Paula Oliveira"));
	}
}