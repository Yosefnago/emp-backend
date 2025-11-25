package com.ms.sw.Dto.salaries;

/**
 * {@code SalaryCalculationProjection} is an immutable Data Transfer Object (DTO)
 * specifically designed to serve as a projection of the minimum necessary data
 * required from the persistence layer (e.g., JPA/Hibernate) to perform a salary calculation.
 * <p>This record ensures that the database query is optimized to fetch only the essential
 * fields needed by the {@code SalaryService} or calculation logic, significantly reducing
 * object materialization overhead and memory footprint compared to loading a full entity.
 * @see com.ms.sw.service.SalaryService
 */
public record SalaryCalculationProjection(
        Double getTotalHoursMonth,
        Double getSalaryPerHour,
        Double getBonus
) {}
