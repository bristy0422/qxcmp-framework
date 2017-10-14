package com.qxcmp.framework.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface SmsTemplateRepository extends JpaRepository<SmsTemplate, String>, JpaSpecificationExecutor<SmsTemplate> {
}
