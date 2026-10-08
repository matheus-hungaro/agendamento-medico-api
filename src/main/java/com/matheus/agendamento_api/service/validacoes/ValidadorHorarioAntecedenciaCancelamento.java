package com.matheus.agendamento_api.service.validacoes;

import com.matheus.agendamento_api.dto.DadosCancelamentoConsulta;
import com.matheus.agendamento_api.model.Consulta;
import com.matheus.agendamento_api.repository.ConsultaRepository;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedenciaCancelamento implements ValidadorCancelamentoDeConsulta {

    private final ConsultaRepository consultaRepository;

    public ValidadorHorarioAntecedenciaCancelamento(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    @Override
    public void validar(DadosCancelamentoConsulta dados) {
        Consulta consulta = consultaRepository.getReferenceById(dados.idConsulta());
        LocalDateTime agora = LocalDateTime.now();
        long diferencaEmHoras = Duration.between(agora, consulta.getData()).toHours();

        if (diferencaEmHoras < 24) {
            throw new IllegalArgumentException("A consulta só pode ser cancelada com antecedência mínima de 24 horas!");
        }
    }
}