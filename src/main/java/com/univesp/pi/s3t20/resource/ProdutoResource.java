package com.univesp.pi.s3t20.resource;

import com.univesp.pi.s3t20.dto.ProdutoDTO;
import com.univesp.pi.s3t20.model.Produto;
import com.univesp.pi.s3t20.service.ProdutoService;
import com.univesp.pi.s3t20.service.MapperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private MapperService mapperService;

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoDTO.class)))
    public List<ProdutoDTO> listarProdutos() {
        return produtoService.listarTodos().stream()
                .map(mapperService::toProdutoDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoDTO> buscarProduto(
            @Parameter(description = "ID do produto", required = true, example = "1")
            @PathVariable Long id) {
        Optional<Produto> produto = produtoService.buscarPorId(id);
        if (produto.isPresent()) {
            return ResponseEntity.ok(mapperService.toProdutoDTO(produto.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar produto por código", description = "Retorna um produto específico pelo seu código único")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoDTO> buscarProdutoPorCodigo(
            @Parameter(description = "Código único do produto", required = true, example = "PROD001")
            @PathVariable String codigo) {
        Optional<Produto> produto = produtoService.buscarPorCodigo(codigo);
        if (produto.isPresent()) {
            return ResponseEntity.ok(mapperService.toProdutoDTO(produto.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/categoria/{categoria}")
    @Operation(summary = "Buscar produtos por categoria", description = "Retorna uma lista de produtos de uma categoria específica")
    @ApiResponse(responseCode = "200", description = "Lista de produtos da categoria retornada com sucesso",
            content = @Content(schema = @Schema(implementation = ProdutoDTO.class)))
    public List<ProdutoDTO> buscarPorCategoria(
            @Parameter(description = "Categoria dos produtos", required = true, example = "Pizza")
            @PathVariable String categoria) {
        return produtoService.buscarPorCategoria(categoria).stream()
                .map(mapperService::toProdutoDTO)
                .toList();
    }

    @PostMapping
    @Operation(summary = "Criar novo produto", description = "Cria um novo produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ProdutoDTO> criarProduto(
            @Parameter(description = "Dados do produto a ser criado", required = true)
            @RequestBody ProdutoDTO produtoDTO) {
        try {
            Produto produto = mapperService.toProduto(produtoDTO);
            Produto produtoCriado = produtoService.criar(produto);
            if (produtoCriado != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(mapperService.toProdutoDTO(produtoCriado));
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ProdutoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    public ResponseEntity<ProdutoDTO> atualizarProduto(
            @Parameter(description = "ID do produto a ser atualizado", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Novos dados do produto", required = true)
            @RequestBody ProdutoDTO produtoDTO) {
        try {
            Produto produto = mapperService.toProduto(produtoDTO);
            Optional<Produto> produtoAtualizado = produtoService.atualizar(id, produto);
            if (produtoAtualizado.isPresent()) {
                return ResponseEntity.ok(mapperService.toProdutoDTO(produtoAtualizado.get()));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto", description = "Remove um produto do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> deletarProduto(
            @Parameter(description = "ID do produto a ser deletado", required = true, example = "1")
            @PathVariable Long id) {
        boolean deletado = produtoService.deletar(id);
        if (deletado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/categorias")
    @Operation(summary = "Listar categorias", description = "Retorna uma lista com todas as categorias de produtos")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    public List<String> listarCategorias() {
        return produtoService.listarCategorias();
    }

    @GetMapping("/count")
    @Operation(summary = "Contar produtos", description = "Retorna o número total de produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Contagem de produtos retornada com sucesso")
    public Long contar() {
        return produtoService.contar();
    }
}