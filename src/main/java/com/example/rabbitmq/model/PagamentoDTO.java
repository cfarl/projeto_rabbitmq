package com.example.rabbitmq.model;

import java.math.BigDecimal;

public class PagamentoDTO {
    private Integer id ;
    private String descricao ;
    private BigDecimal valor ;

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
