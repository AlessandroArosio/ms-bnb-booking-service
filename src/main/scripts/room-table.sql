CREATE TABLE `room` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `capacity` smallint(6) DEFAULT NULL,
                        `property` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                        `room_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `room_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `version` bigint(20) DEFAULT NULL,
                        `room_id` bigint(20) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FKsb41j0o5yw4ckeua3y95u52jh` (`room_id`),
                        CONSTRAINT `FKsb41j0o5yw4ckeua3y95u52jh` FOREIGN KEY (`room_id`) REFERENCES `booking` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
