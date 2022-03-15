package org.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.repository.UsuarioRepository;
import org.generation.blogPessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	private @Autowired TestRestTemplate testRestTemplate;
	
	private @Autowired UsuarioService usuarioService;
	
	private @Autowired UsuarioRepository repository;
	
	@BeforeAll
	void start() {
		repository.deleteAll();
	}
	
	@Test
    @Order(1)
    @DisplayName("Cadastrar um usuário")
    public void deveCriarUmUsuario() {
        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
                new Usuario(0L, "Joao Rodrigres", "FOTO","rodrigues@email.com", "542342"));
        ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
                Usuario.class);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
        assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());

    }
@Test
    @Order(2)
    @DisplayName("Não deve permitir duplicação de usuário")
    public void naoDeveDuplicarUsuario() {
        usuarioService.cadastrarUsuario(new Usuario(0L, "Paulo Oliveira", "FOTO", "paulo@email.com", "82888821"));

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
                new Usuario(0L, "Paulo Oliveira", "FOTO", "paulo@email.com", "82888821"));

        ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
                Usuario.class);
        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }
@Test
    @Order(3)
    @DisplayName("Alterar um usuário")
    public void deveAtualizarUmUsuario() {
        Optional<Usuario> atualizarUsuario = usuarioService
                .cadastrarUsuario(new Usuario(0L, "Paula Rodrigues", "FOTO", "paula@email.com", "1323443"));

        Usuario usuarioUpdate = new Usuario(atualizarUsuario.get().getId(), "Paula Rodrigues", "FOTO", "paula@email.com", "1323443");

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> resposta = testRestTemplate
                .withBasicAuth("root", "root")
                .exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
        assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
    }
@Test
    @Order(4)
    @DisplayName("Listar todos os usuários")
    public void deveMostrarTodasPostagens() {
        usuarioService.cadastrarUsuario(new Usuario(0L, "Lucas Oliveira", "FOTO", "lucas@email.com", "5432213"));

        usuarioService.cadastrarUsuario(new Usuario(0L, "Savio Oliveira", "FOTO", "savio@email.com", "1323443"));

        ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("lucas@email.com", "5432213").exchange("/usuarios/all",
                HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}