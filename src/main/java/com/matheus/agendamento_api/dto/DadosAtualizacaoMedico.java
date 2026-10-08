package com.matheus.agendamento_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoMedico(
        @NotNull(message = "O ID é obrigatório para atualização")
        Long id,
        String nome,
        String email,
        @Valid
        DadosEndereco endereco
) {
}