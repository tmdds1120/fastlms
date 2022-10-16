package com.zerobase.fastlms.admin.mailtemplate.repository;

import com.zerobase.fastlms.admin.mailtemplate.entity.Mailtemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailtemplateRepository extends JpaRepository<Mailtemplate, Long> {

	Optional<Mailtemplate> findByMailtemplateKey(String mailtemplateKey);

}
