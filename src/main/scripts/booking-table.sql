CREATE TABLE `booking` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `booking_amount` decimal(19,2) NOT NULL,
                           `booking_uid` binary(255) NOT NULL,
                           `checkin` datetime(6) NOT NULL,
                           `checkout` datetime(6) NOT NULL,
                           `created_date` datetime(6) DEFAULT NULL,
                           `customer_id` bigint(20) NOT NULL,
                           `has_addons` tinyint(4) DEFAULT '0',
                           `is_paid` tinyint(4) DEFAULT '0',
                           `last_modified_date` datetime(6) DEFAULT NULL,
                           `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                           `version` bigint(20) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
