package com.matheus.agendamento_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
    @NotNull(message = "O ID do médico é obrigatório")
    Long idMedico,

    @NotNull(message = "O ID do paciente é obrigatório")
    Long idPaciente,

    @NotNull(message = "A data da consulta é obrigatória")
    @Future(message = "A data da consulta deve ser no futuro")
    // Define a formatação da data/hora recebida no JSON (Ex: 2026-10-15T14:30:00)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime data
) {
}