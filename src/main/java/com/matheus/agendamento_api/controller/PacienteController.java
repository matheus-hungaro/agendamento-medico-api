package com.matheus.agendamento_api.controller;

import com.matheus.agendamento_api.dto.DadosCadastroPaciente;
import com.matheus.agendamento_api.dto.DadosDetalhamentoPaciente;
import com.matheus.agendamento_api.dto.DadosListagemPaciente;
import com.matheus.agendamento_api.model.Paciente;
import com.matheus.agendamento_api.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteRepository repository;

    public PacienteController(PacienteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoPaciente> cadastrar(@RequestBody @Valid DadosCadastroPaciente dados) {
        Paciente paciente = new Paciente();
        paciente.setNome(dados.nome());
        paciente.setEmail(dados.email());
        paciente.setCpf(dados.cpf());
        paciente.setTelefone(dados.telefone());
        paciente.setAtivo(true);

        repository.save(paciente);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> listar(
            @PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {

        Page<DadosListagemPaciente> pagina = repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemPaciente::new);

        return ResponseEntity.ok(pagina);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        Paciente paciente = repository.getReferenceById(id);
        paciente.desativar();

        return ResponseEntity.noContent().build();
    }
}