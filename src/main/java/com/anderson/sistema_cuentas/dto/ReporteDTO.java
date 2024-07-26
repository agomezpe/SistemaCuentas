package com.anderson.sistema_cuentas.dto;

import java.math.BigDecimal;

public class ReporteDTO {
    private BigDecimal totalDebitos;
    private BigDecimal totalCreditos;

    // Constructor, getters y setters
    public ReporteDTO(BigDecimal totalDebitos, BigDecimal totalCreditos) {
        this.totalDebitos = totalDebitos;
        this.totalCreditos = totalCreditos;
    }

    public BigDecimal getTotalDebitos() {
        return totalDebitos;
    }

    public void setTotalDebitos(BigDecimal totalDebitos) {
        this.totalDebitos = totalDebitos;
    }

    public BigDecimal getTotalCreditos() {
        return totalCreditos;
    }

    public void setTotalCreditos(BigDecimal totalCreditos) {
        this.totalCreditos = totalCreditos;
    }
}
