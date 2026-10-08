package com.matheus.agendamento_api.service.validacoes;

import com.matheus.agendamento_api.dto.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsulta {

    @Override
    public void validar(DadosAgendamentoConsulta dados) {
        LocalDateTime dataConsulta = dados.data();

        boolean domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        boolean antesDaAbertura = dataConsulta.getHour() < 7;
        boolean depoisDoEncerramento = dataConsulta.getHour() >= 19;

        if (domingo || antesDaAbertura || depoisDoEncerramento) {
            throw new IllegalArgumentException("Consulta fora do horário de funcionamento da clínica (Seg-Sáb, 07:00 às 19:00)");
        }
    }
}