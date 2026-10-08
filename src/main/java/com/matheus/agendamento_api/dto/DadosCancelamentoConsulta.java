package com.matheus.agendamento_api.dto;

import com.matheus.agendamento_api.model.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoConsulta(
    @NotNull(message = "O ID da consulta é obrigatório")
    Long idConsulta,

    @NotNull(message = "O motivo do cancelamento é obrigatório")
    MotivoCancelamento motivo
) {
}