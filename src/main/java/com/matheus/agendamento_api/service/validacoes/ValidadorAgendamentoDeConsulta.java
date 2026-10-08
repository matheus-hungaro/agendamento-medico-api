package com.matheus.agendamento_api.service.validacoes;

import com.matheus.agendamento_api.dto.DadosAgendamentoConsulta;

public interface ValidadorAgendamentoDeConsulta {
    void validar(DadosAgendamentoConsulta dados);
}