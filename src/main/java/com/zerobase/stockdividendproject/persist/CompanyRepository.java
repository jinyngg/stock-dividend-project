package com.zerobase.stockdividendproject.persist;

import com.zerobase.stockdividendproject.persist.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    boolean existsByTicker(String ticker);

    // Optional 감싸는 이유 => NPE 방지, 값이 없는 경우도 코드적으로 깔끔히 정리 가능한 장점이 있음.
    Optional<CompanyEntity> findByName(String name);

    // IgnoreCase => 알파벳 소문자, 대문자 구분하지 않음
    Page<CompanyEntity> findByNameStartingWithIgnoreCase(String s, Pageable limit);

}
