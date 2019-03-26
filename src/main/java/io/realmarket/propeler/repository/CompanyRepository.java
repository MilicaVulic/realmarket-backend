package io.realmarket.propeler.repository;

import io.realmarket.propeler.model.Auth;
import io.realmarket.propeler.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

  Optional<Company> findByAuthId(final Long authId);

  boolean existsCompanyByAuth(Auth owner);

  Optional<Company> findByAuth(Auth owner);
}
