package com.anderson.sistema_cuentas.repositories;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anderson.sistema_cuentas.entities.Movimiento;
import com.anderson.sistema_cuentas.enums.TipoMovimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
	@Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.fecha = :fecha AND m.tipoMovimiento = 'RETIRO'")
    BigDecimal sumValoresByNumeroCuentaAndFecha(@Param("cuentaId") Long cuentaId, @Param("fecha") LocalDate fecha);
	
    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.id = :cuentaId ORDER BY m.fecha DESC, m.id DESC")
    List<Movimiento> findLatestMovimientoByCuentaId(@Param("cuentaId") Long cuentaId, Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(m.valor), 0) FROM Movimiento m WHERE m.cuenta.id = :cuentaId AND m.tipoMovimiento = :tipoMovimiento AND m.fecha BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumValoresByTipoMovimientoAndFecha(
            @Param("cuentaId") Long cuentaId,
            @Param("tipoMovimiento") TipoMovimiento tipoMovimiento,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
    
    List<Movimiento> findByCuentaIdAndFechaBetween(Long cuentaId, LocalDate fechaInicio, LocalDate fechaFin);

}
