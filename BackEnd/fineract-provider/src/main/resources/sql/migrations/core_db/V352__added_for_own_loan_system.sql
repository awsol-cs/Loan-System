--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

CREATE TABLE scoring_rule(id INT AUTO_INCREMENT,rule_name VARCHAR(255),tag VARCHAR(255),weighted_value INT,rule_type INT,status VARCHAR(255),created_by VARCHAR(255),created_date TIMESTAMP,updated_by VARCHAR(255),updated_date TIMESTAMP,PRIMARY KEY(id));
CREATE TABLE choice_rule_type(id INT AUTO_INCREMENT,scoring_rule_id INT NOT NULL,choice_name VARCHAR(255),relative_value DECIMAL(11,2),PRIMARY KEY(id),FOREIGN KEY(scoring_rule_id) REFERENCES scoring_rule(id));
CREATE TABLE range_rule_type(id INT AUTO_INCREMENT,scoring_rule_id INT NOT NULL,min_value INT,max_value INT,relative_value DECIMAL(11,2),PRIMARY KEY(id),FOREIGN KEY(scoring_rule_id) REFERENCES scoring_rule(id));
CREATE TABLE `formula` (
 `id` int(11) NOT NULL AUTO_INCREMENT,
 `formula_name` varchar(255) DEFAULT NULL,
 `formula` varchar(255) DEFAULT NULL,
 `status` varchar(255) DEFAULT NULL,
 `created_by` varchar(255) DEFAULT NULL,
 `created_date` timestamp NULL DEFAULT NULL,
 `updated_by` varchar(255) DEFAULT NULL,
 `updated_date` timestamp NULL DEFAULT NULL,
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE credit_score(id BIGINT(20) AUTO_INCREMENT, client_id BIGINT(20), creditscore INT(11), PRIMARY KEY(id), FOREIGN KEY(client_id) REFERENCES m_client(id));

CREATE TABLE m_kyc_info(id BIGINT AUTO_INCREMENT, client_id BIGINT(20), nationality VARCHAR(255), no_of_dependents INT(11), marital_status INT(11), educational_attainment INT(11), others_educational_attainment VARCHAR(255), school_last_attended VARCHAR(255), place_of_birth VARCHAR(255), mother_maiden_name VARCHAR(255), residence_ownership INT(11), rented_residence_ownership INT(11), employment INT(11), other_employment VARCHAR(255), self_employed INT(11), years_in_operation INT(11), no_of_employees INT(11), name_of_present_employer_business VARCHAR(255), nature_of_business VARCHAR(255), office_address VARCHAR(255), office_phone VARCHAR(20), office_mobile_phone VARCHAR(20), office_local_number VARCHAR(20), office_fax_number VARCHAR(20), office_email_address VARCHAR(255), rank INT(11), title_or_position VARCHAR(255), gross_annual_income INT(11), other_income INT(11), name_of_previous_employer VARCHAR(255), office_address_previous VARCHAR(255), years_with_present_employer INT(11), years_with_previous_employer INT(11), name_reference VARCHAR(255), relationship_reference VARCHAR(255), employer_reference VARCHAR(255), address_reference VARCHAR(255), contact_number_reference VARCHAR(20), mobile_reference VARCHAR(20), related_to_officer INT(11), name_of_officer VARCHAR(255), contact_number_officer VARCHAR(20), relationship_of_officer INT(11), PRIMARY KEY(id), FOREIGN KEY(client_id) REFERENCES m_client(id));

CREATE TABLE m_co_maker(id BIGINT AUTO_INCREMENT, loan_id BIGINT(20), kyc_id BIGINT(20), firstname VARCHAR(50), middlename VARCHAR(50), lastname VARCHAR(50), fullname VARCHAR(100), mobile_no VARCHAR(50), gender INT(11), date_of_birth DATE, email_address VARCHAR(150), PRIMARY KEY(id), FOREIGN KEY(loan_id) REFERENCES m_loan(id), FOREIGN KEY(kyc_id) REFERENCES m_kyc_info(id));

ALTER TABLE m_portfolio_command_source ADD COLUMN formula_id BIGINT(20);

INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'UPDATE_CREDIT_SCORE', 'CREDIT_SCORE', 'UPDATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'CREATE_CREDIT_SCORE', 'CREDIT_SCORE', 'CREATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'CREATE_CREDIT_SCORE_FORMULA', 'CREDIT_SCORE_FORMULA', 'CREATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'UPDATE_CREDIT_SCORE_FORMULA', 'CREDIT_SCORE_FORMULA', 'UPDATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'TRANSFERLOAN_LOAN', 'LOAN', 'TRANSFERLOAN', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'UPDATE_SCORE_CONFIG', 'SCORE_CONFIG', 'UPDATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'CREATE_SCORE_CONFIG', 'SCORE_CONFIG', 'CREATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'UPDATE_SCORE_CONFIG_STATUS', 'SCORE_CONFIG_STATUS', 'UPDATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'REJECT_LOAN_APP', 'LOAN_APP', 'REJECT', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('configuration', 'APPROVE_LOAN_APP', 'LOAN_APP', 'APPROVE', 0);


INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'UPDATE_CS_CLIENT', 'CS_CLIENT', 'UPDATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'CREATE_CS_CLIENT', 'CS_CLIENT', 'CREATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'UPDATE_CS_COMAKER', 'CS_COMAKER', 'UPDATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'CREATE_CS_LOAN', 'CS_LOAN', 'CREATE', 0);

INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('educationalAttainment', 1);
INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('residenceOwnership', 1);
INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('employment', 1);
INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('selfEmployed', 1);
INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('rank', 1);
INSERT INTO `m_code` (`code_name`, `is_system_defined`) VALUES ('relationshipOfOfficer', 1);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (34, 'Owned', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (34, 'Co.Quarters', '', 1, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (34, 'Mortgaged', '', 2, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (34, 'With Relatives', '', 3, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (34, 'Rented', '', 4, 1, 0);


INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (33, 'High School', '', 1, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (33, 'College', '', 2, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (33, 'Post Graduate', '', 3, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (33, 'Others', '', 4, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (35, 'Private Sector', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (35, 'Government', '', 1, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (35, 'Professional', '', 2, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (35, 'Self-Employed', '', 3, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (35, 'Others', '', 4, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (36, 'Sole Proprietor', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (36, 'Partnership', '', 1, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (37, 'Senior Officer', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (37, 'Junior Officer', '', 1, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (37, 'Non-Officer', '', 2, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (38, 'Spouse', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (38, 'Parent', '', 1, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (38, 'Sibling', '', 2, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (38, 'Children', '', 3, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (38, 'Others', '', 4, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (4, 'Male', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (4, 'Female', '', 1, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (30, 'Single', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (30, 'Married', '', 1, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (30, 'Widow/er', '', 2, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (30, 'Separated', '', 3, 1, 0);

INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (5, 'Yes', '', 0, 1, 0);
INSERT INTO `m_code_value` (`code_id`, `code_value`, `code_description`, `order_position`, `is_active`, `is_mandatory`) VALUES (5, 'No', '', 1, 1, 0);
