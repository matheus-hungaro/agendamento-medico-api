package com.matheus.agendamento_api.service;

import com.matheus.agendamento_api.dto.DadosAgendamentoConsulta;
import com.matheus.agendamento_api.model.Medico;
import com.matheus.agendamento_api.model.Paciente;
import com.matheus.agendamento_api.repository.ConsultaRepository;
import com.matheus.agendamento_api.repository.MedicoRepository;
import com.matheus.agendamento_api.repository.PacienteRepository;
import com.matheus.agendamento_api.service.validacoes.ValidadorAgendamentoDeConsulta;
import com.matheus.agendamento_api.service.validacoes.ValidadorCancelamentoDeConsulta;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AgendaDeConsultasServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ValidadorAgendamentoDeConsulta validadores;

    @Mock
    private ValidadorCancelamentoDeConsulta validadoresCancelamento;

    @Test
    @DisplayName("Deveria agendar consulta com sucesso quando dados forem válidos")
    void agendarCenarioSucesso() {
        var idMedico = 1L;
        var idPaciente = 2L;
        var data = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);

        var medico = new Medico();
        var paciente = new Paciente();

        given(medicoRepository.existsById(idMedico)).willReturn(true);
        given(pacienteRepository.existsById(idPaciente)).willReturn(true);
        given(medicoRepository.getReferenceById(idMedico)).willReturn(medico);
        given(pacienteRepository.getReferenceById(idPaciente)).willReturn(paciente);

        var agendaService = new AgendaDeConsultasService(
                consultaRepository,
                medicoRepository,
                pacienteRepository,
                List.of(validadores),
                List.of(validadoresCancelamento)
        );

        var dadosAgendamento = new DadosAgendamentoConsulta(idMedico, idPaciente, data);
        var result = agendaService.agendar(dadosAgendamento);

        assertNotNull(result);
    }
}
