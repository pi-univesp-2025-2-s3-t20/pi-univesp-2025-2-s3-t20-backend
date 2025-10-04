package com.univesp.pi.s3t20.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO para operações com Produto")
public class ProdutoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "ID único do produto (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Código único do produto (gerado automaticamente)", example = "PROD001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idProduto;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(max = 255, message = "Nome do produto deve ter no máximo 255 caracteres")
    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String produto;

    @NotBlank(message = "Categoria é obrigatória")
    @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
    @Schema(description = "Categoria do produto", example = "Pizza", required = true)
    private String categoria;

    @Positive(message = "Pedido mínimo deve ser um número positivo")
    @Schema(description = "Quantidade mínima para pedido", example = "1")
    private Integer pedidoMinimo;

    @Positive(message = "Custo unitário deve ser um número positivo")
    @Schema(description = "Custo unitário do produto", example = "15.50")
    private BigDecimal custoUnitario;

    @Positive(message = "Preço sugerido deve ser um número positivo")
    @Schema(description = "Preço sugerido do produto", example = "25.90")
    private BigDecimal precoSugerido;

    @Positive(message = "Preço por cento deve ser um número positivo")
    @Schema(description = "Preço por cento do produto", example = "259.00")
    private BigDecimal centoPreco;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Indica se o produto está ativo", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data e hora de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(description = "Data e hora da última atualização do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    // Constructors
    public ProdutoDTO() {}

    public ProdutoDTO(String produto, String categoria) {
        this.produto = produto;
        this.categoria = categoria;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getPedidoMinimo() {
        return pedidoMinimo;
    }

    public void setPedidoMinimo(Integer pedidoMinimo) {
        this.pedidoMinimo = pedidoMinimo;
    }

    public BigDecimal getCustoUnitario() {
        return custoUnitario;
    }

    public void setCustoUnitario(BigDecimal custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    public BigDecimal getPrecoSugerido() {
        return precoSugerido;
    }

    public void setPrecoSugerido(BigDecimal precoSugerido) {
        this.precoSugerido = precoSugerido;
    }

    public BigDecimal getCentoPreco() {
        return centoPreco;
    }

    public void setCentoPreco(BigDecimal centoPreco) {
        this.centoPreco = centoPreco;
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
