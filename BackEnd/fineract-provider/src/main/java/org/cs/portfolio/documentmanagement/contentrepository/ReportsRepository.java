package org.cs.portfolio.documentmanagement.contentrepository;

import org.apache.fineract.infrastructure.documentmanagement.domain.DocumentRepository;
import org.apache.fineract.infrastructure.documentmanagement.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportsRepository extends DocumentRepository {
	
	public Document findByFileName(String fileName);
	
}