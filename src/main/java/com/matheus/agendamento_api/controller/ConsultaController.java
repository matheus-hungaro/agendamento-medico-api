package com.matheus.agendamento_api.controller;

import com.matheus.agendamento_api.dto.DadosAgendamentoConsulta;
import com.matheus.agendamento_api.dto.DadosCancelamentoConsulta;
import com.matheus.agendamento_api.dto.DadosDetalhamentoConsulta;
import com.matheus.agendamento_api.repository.ConsultaRepository;
import com.matheus.agendamento_api.service.AgendaDeConsultasService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final AgendaDeConsultasService agendaService;
    private final ConsultaRepository consultaRepository;

    public ConsultaController(AgendaDeConsultasService agendaService, ConsultaRepository consultaRepository) {
        this.agendaService = agendaService;
        this.consultaRepository = consultaRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoConsulta> agendar(@RequestBody @Valid DadosAgendamentoConsulta dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoConsulta detalhamento = agendaService.agendar(dados);
        var uri = uriBuilder.path("/consultas/{id}").buildAndExpand(detalhamento.id()).toUri();
        return ResponseEntity.created(uri).body(detalhamento);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoConsulta>> listar(
            @PageableDefault(size = 10, sort = {"data"}) Pageable paginacao) {

        Page<DadosDetalhamentoConsulta> pagina = consultaRepository.findAll(paginacao)
                .map(DadosDetalhamentoConsulta::new);

        return ResponseEntity.ok(pagina);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> cancelar(@RequestBody @Valid DadosCancelamentoConsulta dados) {
        agendaService.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}