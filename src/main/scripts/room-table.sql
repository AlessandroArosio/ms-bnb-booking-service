CREATE TABLE `room` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `capacity` smallint(6) NOT NULL,
                        `property` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                        `room_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `room_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `version` bigint(20) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
