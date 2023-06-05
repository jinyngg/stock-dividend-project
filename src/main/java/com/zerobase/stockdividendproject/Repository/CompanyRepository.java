package com.zerobase.stockdividendproject.Repository;

import com.zerobase.stockdividendproject.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByTicker(String ticker);

    // Optional 감싸는 이유 => NPE 방지, 값이 없는 경우도 코드적으로 깔끔히 정리 가능한 장점이 있음.
    Optional<Company> findByName(String name);

    Optional<Company> findByTicker(String ticker);

    // IgnoreCase => 알파벳 소문자, 대문자 구분하지 않음
    Page<Company> findByNameStartingWithIgnoreCase(String s, Pageable limit);

}
