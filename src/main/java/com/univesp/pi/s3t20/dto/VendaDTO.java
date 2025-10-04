package com.univesp.pi.s3t20.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO para operações com Venda")
public class VendaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único da venda (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Código único da venda (gerado automaticamente)", example = "VEN001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idVenda;

    @NotNull(message = "Data da venda é obrigatória")
    @Schema(description = "Data da venda", example = "2024-01-15", required = true)
    private LocalDate data;

    @NotNull(message = "ID do produto é obrigatório")
    @Schema(description = "ID do produto vendido", example = "1", required = true)
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser um número positivo")
    @Schema(description = "Quantidade vendida", example = "2", required = true)
    private Integer quantidade;

    @NotNull(message = "Preço unitário é obrigatório")
    @Positive(message = "Preço unitário deve ser um número positivo")
    @Schema(description = "Preço unitário do produto", example = "25.90", required = true)
    private BigDecimal precoUnitario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Receita total da venda (calculada automaticamente)", example = "51.80", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal receitaTotal;

    @NotNull(message = "ID do cliente é obrigatório")
    @Schema(description = "ID do cliente que fez a compra", example = "1", required = true)
    private Long clienteId;

    @NotNull(message = "ID da forma de pagamento é obrigatório")
    @Schema(description = "ID da forma de pagamento utilizada", example = "1", required = true)
    private Long formaPagamentoId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Indica se a venda está ativa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data e hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data e hora da última atualização do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    // Constructors
    public VendaDTO() {}

    public VendaDTO(LocalDate data, Long produtoId, Integer quantidade, BigDecimal precoUnitario, 
                   Long clienteId, Long formaPagamentoId) {
        this.data = data;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.clienteId = clienteId;
        this.formaPagamentoId = formaPagamentoId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdVenda() {
        return idVenda;
    }

    public void setIdVenda(String idVenda) {
        this.idVenda = idVenda;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(BigDecimal receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getFormaPagamentoId() {
        return formaPagamentoId;
    }

    public void setFormaPagamentoId(Long formaPagamentoId) {
        this.formaPagamentoId = formaPagamentoId;
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
