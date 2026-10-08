package com.matheus.agendamento_api.dto;

import com.matheus.agendamento_api.model.Paciente;

public record DadosDetalhamentoPaciente(
    Long id,
    String nome,
    String email,
    String cpf,
    String telefone,
    Boolean ativo
) {
    public DadosDetalhamentoPaciente(Paciente paciente) {
        this(
            paciente.getId(),
            paciente.getNome(),
            paciente.getEmail(),
            paciente.getCpf(),
            paciente.getTelefone(),
            paciente.getAtivo()
        );
    }
}