package com.univesp.pi.s3t20.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta para Venda com entidades relacionadas")
public class VendaResponseDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único da venda", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Código único da venda", example = "VEN001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idVenda;

    @Schema(description = "Data da venda", example = "2024-01-15")
    private LocalDate data;

    @Schema(description = "Produto vendido")
    private ProdutoDTO produto;

    @Schema(description = "Quantidade vendida", example = "2")
    private Integer quantidade;

    @Schema(description = "Preço unitário do produto", example = "25.90")
    private BigDecimal precoUnitario;

    @Schema(description = "Receita total da venda", example = "51.80")
    private BigDecimal receitaTotal;

    @Schema(description = "Cliente que fez a compra")
    private ClienteDTO cliente;

    @Schema(description = "Forma de pagamento utilizada")
    private FormaPagamentoDTO formaPagamento;

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
    public VendaResponseDTO() {}

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

    public ProdutoDTO getProduto() {
        return produto;
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = produto;
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

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public FormaPagamentoDTO getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamentoDTO formaPagamento) {
        this.formaPagamento = formaPagamento;
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
