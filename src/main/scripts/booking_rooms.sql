CREATE TABLE `booking_rooms` (
                                 `booking_id` bigint(20) NOT NULL,
                                 `room_id` bigint(20) NOT NULL,
                                 KEY `FKbxlt04wy4xvkhoiqx56qycf15` (`room_id`),
                                 KEY `FKe0g8vkaeh304hm4aoqg115bmv` (`booking_id`),
                                 CONSTRAINT `FKbxlt04wy4xvkhoiqx56qycf15` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`),
                                 CONSTRAINT `FKe0g8vkaeh304hm4aoqg115bmv` FOREIGN KEY (`booking_id`) REFERENCES `booking` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
