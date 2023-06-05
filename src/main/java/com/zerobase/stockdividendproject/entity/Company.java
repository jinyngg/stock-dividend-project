package com.zerobase.stockdividendproject.entity;

import com.zerobase.stockdividendproject.model.CompanyDto;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "COMPANY")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String name;

    public Company(CompanyDto companyDto) {
        this.ticker = companyDto.getTicker();
        this.name = companyDto.getName();
    }

}
