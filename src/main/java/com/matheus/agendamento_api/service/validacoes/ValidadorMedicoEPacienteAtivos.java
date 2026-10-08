package com.matheus.agendamento_api.service.validacoes;

import com.matheus.agendamento_api.dto.DadosAgendamentoConsulta;
import com.matheus.agendamento_api.repository.MedicoRepository;
import com.matheus.agendamento_api.repository.PacienteRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoEPacienteAtivos implements ValidadorAgendamentoDeConsulta {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public ValidadorMedicoEPacienteAtivos(MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public void validar(DadosAgendamentoConsulta dados) {
        Boolean medicoEstaAtivo = medicoRepository.findAtivoById(dados.idMedico());
        if (medicoEstaAtivo != null && !medicoEstaAtivo) {
            throw new IllegalArgumentException("Consulta não pode ser agendada com médico inativo");
        }

        Boolean pacienteEstaAtivo = pacienteRepository.findAtivoById(dados.idPaciente());
        if (pacienteEstaAtivo != null && !pacienteEstaAtivo) {
            throw new IllegalArgumentException("Consulta não pode ser agendada com paciente inativo");
        }
    }
}