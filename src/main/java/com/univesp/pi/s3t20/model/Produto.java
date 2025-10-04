package com.univesp.pi.s3t20.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produtos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(description = "Entidade que representa um produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "id_produto", nullable = false, length = 10, unique = true)
    @Schema(description = "Código único do produto (gerado automaticamente)", example = "PROD001", accessMode = Schema.AccessMode.READ_ONLY)
    private String idProduto;

    @Column(name = "produto", nullable = false, length = 255)
    @Schema(description = "Nome do produto", example = "Pizza Margherita", required = true)
    private String produto;

    @Column(name = "categoria", nullable = false, length = 100)
    @Schema(description = "Categoria do produto", example = "Pizza", required = true)
    private String categoria;

    @Column(name = "pedido_minimo")
    @Schema(description = "Quantidade mínima para pedido", example = "1")
    private Integer pedidoMinimo;

    @Column(name = "custo_unitario", precision = 10, scale = 2)
    @Schema(description = "Custo unitário do produto", example = "15.50")
    private BigDecimal custoUnitario;

    @Column(name = "preco_sugerido", precision = 10, scale = 2)
    @Schema(description = "Preço sugerido do produto", example = "25.90")
    private BigDecimal precoSugerido;

    @Column(name = "cento_preco", precision = 10, scale = 2)
    @Schema(description = "Preço por cento do produto", example = "259.00")
    private BigDecimal centoPreco;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @Schema(description = "Lista de vendas do produto", hidden = true)
    private List<Venda> vendas;

    @Column(name = "is_active")
    @Schema(description = "Indica se o produto está ativo (gerado automaticamente)", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Schema(description = "Data e hora de criação do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "Data e hora da última atualização do registro (gerado automaticamente)", accessMode = Schema.AccessMode.READ_ONLY)
    private java.time.LocalDateTime updatedAt;

    // Constructors
    public Produto() {}

    public Produto(String idProduto, String produto, String categoria) {
        this.idProduto = idProduto;
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

    // idProduto é gerado automaticamente - setter removido para manter somente leitura
    // Método público para uso interno dos serviços (não deve ser usado externamente)
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

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // isActive, createdAt e updatedAt são gerenciados automaticamente - setters removidos para manter somente leitura
    // Métodos públicos para uso interno dos serviços (não devem ser usados externamente)
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(java.time.LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public java.time.LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
