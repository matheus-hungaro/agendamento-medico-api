package com.matheus.agendamento_api.dto;

import com.matheus.agendamento_api.model.Especialidade;
import com.matheus.agendamento_api.model.Medico;

public record DadosListagemMedico(
    Long id,
    String nome,
    String email,
    String crm,
    Especialidade especialidade
) 
{
    public DadosListagemMedico(Medico medico) {
        this(
            medico.getId(),
            medico.getNome(),
            medico.getEmail(), // <--- Certifique-se de que este método está aqui
            medico.getCrm(),
            medico.getEspecialidade()
        );
    }
}