CREATE TABLE `booking` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT,
                           `booking_amount` decimal(19,2) DEFAULT NULL,
                           `booking_uid` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `checkin` datetime(6) NOT NULL,
                           `checkout` datetime(6) NOT NULL,
                           `created_date` datetime(6) DEFAULT NULL,
                           `customer_id` bigint(20) NOT NULL,
                           `has_addons` bit(1) DEFAULT NULL,
                           `is_paid` bit(1) DEFAULT NULL,
                           `last_modified_date` datetime(6) DEFAULT NULL,
                           `notes` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                           `version` bigint(20) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
