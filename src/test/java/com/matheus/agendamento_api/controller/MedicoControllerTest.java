package com.matheus.agendamento_api.controller;

import com.matheus.agendamento_api.dto.DadosAutenticacao;
import com.matheus.agendamento_api.dto.DadosCadastroMedico;
import com.matheus.agendamento_api.dto.DadosEndereco;
import com.matheus.agendamento_api.model.Especialidade;
import com.matheus.agendamento_api.model.Usuario;
import com.matheus.agendamento_api.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class MedicoControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void configurarUsuarioPadrao() {
        usuarioRepository.deleteAll();
        usuarioRepository.save(new Usuario(null, "admin@agendamento.com", passwordEncoder.encode("123456")));
    }

    @Test
    @DisplayName("Deveria retornar codigo http 400 quando informacoes estao invalidas")
    @WithMockUser
    void cadastrar_cenario1() throws Exception {
        var response = mvc.perform(post("/medicos"))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Deveria retornar codigo http 201 Created quando informacoes estao validas")
    @WithMockUser
    void cadastrar_cenario2() throws Exception {
        var endereco = new DadosEndereco("Rua 1", "Bairro", "12345678", "Cidade", "SP", null, "100");
        var dadosCadastro = new DadosCadastroMedico(
                "Dr. Carlos Silva",
                "carlos.silva@clinic.com",
                "11999998888",
                "123456",
                Especialidade.CARDIOLOGIA,
                endereco
        );

        var response = mvc
                .perform(
                        post("/medicos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dadosCadastro))
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Deveria autenticar usuario admin e retornar token JWT")
    void login_cenario1() throws Exception {
        var dadosAutenticacao = new DadosAutenticacao("admin@agendamento.com", "123456");

        var response = mvc
                .perform(
                        post("/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dadosAutenticacao))
                )
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("token");
    }
}