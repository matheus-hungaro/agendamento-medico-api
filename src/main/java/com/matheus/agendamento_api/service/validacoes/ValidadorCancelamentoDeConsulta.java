package com.matheus.agendamento_api.service.validacoes;

import com.matheus.agendamento_api.dto.DadosCancelamentoConsulta;

public interface ValidadorCancelamentoDeConsulta {
    void validar(DadosCancelamentoConsulta dados);
}