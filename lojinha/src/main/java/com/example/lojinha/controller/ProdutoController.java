package com.example.lojinha.controller;

import com.example.lojinha.model.Produto;
import com.example.lojinha.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    public ProdutoController(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    // GET - Listar todos os produtos
    @GetMapping
    public List<Produto> listarTodosOsProdutos() {
        return produtoRepository.findAll();
    }

    // GET - Obter produto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Produto> obterProdutoPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(produto -> ResponseEntity.ok(produto))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST - Criar novo produto
    @PostMapping
    public Produto criarProduto(@RequestBody Produto produto) {
        return produtoRepository.save(produto);
    }

    // PUT - Atualizar produto existente
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto detalhesProduto) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setNome(detalhesProduto.getNome());
                    produto.setDescricao(detalhesProduto.getDescricao());
                    produto.setPreco(detalhesProduto.getPreco());
                    produto.setEstoque(detalhesProduto.getEstoque());
                    Produto produtoAtualizado = produtoRepository.save(produto);
                    return ResponseEntity.ok(produtoAtualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE - Remover produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removerProduto(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produtoRepository.delete(produto);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
