package com.matheus.agendamento_api.dto;

import com.matheus.agendamento_api.model.Especialidade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroMedico(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "O CRM é obrigatório")
        @Pattern(regexp = "\\d{4,6}", message = "O CRM deve ter entre 4 e 6 dígitos numéricos")
        String crm,

        @NotNull(message = "A especialidade é obrigatória")
        Especialidade especialidade,

        @NotNull(message = "Os dados do endereço são obrigatórios")
        @Valid
        DadosEndereco endereco
) {
}