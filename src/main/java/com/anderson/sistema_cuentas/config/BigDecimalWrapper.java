package com.anderson.sistema_cuentas.config;

import java.math.BigDecimal;

public class BigDecimalWrapper {
    private BigDecimal value;

    public BigDecimalWrapper(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
