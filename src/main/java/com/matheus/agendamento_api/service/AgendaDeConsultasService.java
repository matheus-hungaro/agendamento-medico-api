package com.matheus.agendamento_api.service;

import com.matheus.agendamento_api.dto.DadosAgendamentoConsulta;
import com.matheus.agendamento_api.dto.DadosCancelamentoConsulta;
import com.matheus.agendamento_api.dto.DadosDetalhamentoConsulta;
import com.matheus.agendamento_api.model.Consulta;
import com.matheus.agendamento_api.model.Medico;
import com.matheus.agendamento_api.model.Paciente;
import com.matheus.agendamento_api.repository.ConsultaRepository;
import com.matheus.agendamento_api.repository.MedicoRepository;
import com.matheus.agendamento_api.repository.PacienteRepository;
import com.matheus.agendamento_api.service.validacoes.ValidadorAgendamentoDeConsulta;
import com.matheus.agendamento_api.service.validacoes.ValidadorCancelamentoDeConsulta;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultasService {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final List<ValidadorAgendamentoDeConsulta> validadores;

    // Adicione essa nova lista no construtor da classe AgendaDeConsultasService:
private final List<ValidadorCancelamentoDeConsulta> validadoresCancelamento;

public AgendaDeConsultasService(
        ConsultaRepository consultaRepository,
        MedicoRepository medicoRepository,
        PacienteRepository pacienteRepository,
        List<ValidadorAgendamentoDeConsulta> validadores,
        List<ValidadorCancelamentoDeConsulta> validadoresCancelamento) {
    this.consultaRepository = consultaRepository;
    this.medicoRepository = medicoRepository;
    this.pacienteRepository = pacienteRepository;
    this.validadores = validadores;
    this.validadoresCancelamento = validadoresCancelamento;
}


public void cancelar(DadosCancelamentoConsulta dados) {
    if (!consultaRepository.existsById(dados.idConsulta())) {
        throw new IllegalArgumentException("ID da consulta informado não existe!");
    }

    validadoresCancelamento.forEach(v -> v.validar(dados));

    Consulta consulta = consultaRepository.getReferenceById(dados.idConsulta());
    consulta.cancelar(dados.motivo());
}

    public DadosDetalhamentoConsulta agendar(DadosAgendamentoConsulta dados) {
        if (!medicoRepository.existsById(dados.idMedico())) {
            throw new IllegalArgumentException("ID do médico informado não existe!");
        }

        if (!pacienteRepository.existsById(dados.idPaciente())) {
            throw new IllegalArgumentException("ID do paciente informado não existe!");
        }

        // Executa todas as validações ativas no sistema (princípio SOLID - Open/Closed)
        validadores.forEach(v -> v.validar(dados));

        Medico medico = medicoRepository.getReferenceById(dados.idMedico());
        Paciente paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setData(dados.data());

        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }
}