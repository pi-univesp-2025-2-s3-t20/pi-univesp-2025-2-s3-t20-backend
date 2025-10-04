package com.univesp.pi.s3t20.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(description = "DTO para operações com Cliente")
public class ClienteDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único do cliente (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Código único do cliente (gerado automaticamente)", example = "CLI001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idCliente;

    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(max = 255, message = "Nome do cliente deve ter no máximo 255 caracteres")
    @Schema(description = "Nome do cliente", example = "João Silva", required = true)
    private String nomeCliente;

    @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
    @Schema(description = "Bairro do cliente", example = "Centro")
    private String bairro;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    @Schema(description = "Cidade do cliente", example = "São Paulo")
    private String cidade;

    @NotBlank(message = "Tipo do cliente é obrigatório")
    @Size(max = 20, message = "Tipo do cliente deve ter no máximo 20 caracteres")
    @Schema(description = "Tipo do cliente", example = "Pessoa Física", required = true)
    private String tipoCliente;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Indica se o cliente está ativo", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data e hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data e hora da última atualização do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    // Constructors
    public ClienteDTO() {}

    public ClienteDTO(String nomeCliente, String bairro, String cidade, String tipoCliente) {
        this.nomeCliente = nomeCliente;
        this.bairro = bairro;
        this.cidade = cidade;
        this.tipoCliente = tipoCliente;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
