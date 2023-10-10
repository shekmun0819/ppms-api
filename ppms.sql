-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 09, 2023 at 09:57 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ppms`
--

-- --------------------------------------------------------

--
-- Table structure for table `constraints`
--

CREATE TABLE `constraints` (
  `constraint_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `num_of_consecutive_slots` int(11) NOT NULL,
  `prefer_venue_change` bit(1) NOT NULL,
  `unavailable_time` varbinary(255) DEFAULT NULL,
  `csmind_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `constraints`
--

INSERT INTO `constraints` (`constraint_id`, `created_at`, `updated_at`, `num_of_consecutive_slots`, `prefer_venue_change`, `unavailable_time`, `csmind_id`, `user_id`) VALUES
(1, '2023-02-09 16:35:23', '2023-02-09 16:35:23', 4, b'0', NULL, 1, 4),
(2, '2023-02-09 16:35:23', '2023-02-09 16:35:23', 3, b'1', NULL, 1, 5);

-- --------------------------------------------------------

--
-- Table structure for table `csminds`
--

CREATE TABLE `csminds` (
  `csmind_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `academic_session` varchar(255) DEFAULT NULL,
  `course_code` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `num_of_presentations` int(11) NOT NULL,
  `schedule_url` varchar(255) DEFAULT NULL,
  `time_range` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `csminds`
--

INSERT INTO `csminds` (`csmind_id`, `created_at`, `updated_at`, `academic_session`, `course_code`, `date`, `num_of_presentations`, `schedule_url`, `time_range`) VALUES
(1, '2023-02-09 16:32:35', '2023-02-09 16:32:35', 'Academic Session 2022/2023 Semester 1', 'CDS590', '2023-04-30', 43, NULL, NULL),
(2, '2023-02-09 16:32:35', '2023-02-09 16:32:35', 'Academic Session 2022/2023 Semester 1', 'CDT594', '2023-04-28', 33, NULL, NULL),
(3, '2023-02-09 16:33:38', '2023-02-09 16:33:38', 'Academic Session 2022/2023 Semester 2', 'CDS590', '2023-06-30', 23, NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `hosts`
--

CREATE TABLE `hosts` (
  `id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hosts`
--

INSERT INTO `hosts` (`id`, `created_at`, `updated_at`, `company_name`, `contact`, `user_id`) VALUES
(1, '2023-02-09 16:30:10', '2023-02-09 16:30:10', 'Company A', '011-2918276', 6),
(2, '2023-02-09 16:30:10', '2023-02-09 16:30:10', 'Company B', '012-1918172', 7);

-- --------------------------------------------------------

--
-- Table structure for table `lecturers`
--

CREATE TABLE `lecturers` (
  `id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_programme_manager` bit(1) NOT NULL,
  `staff_id` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lecturers`
--

INSERT INTO `lecturers` (`id`, `created_at`, `updated_at`, `is_programme_manager`, `staff_id`, `user_id`) VALUES
(1, '2023-02-09 16:29:26', '2023-02-09 16:29:26', b'1', 'STAFF1', 4),
(2, '2023-02-09 16:29:26', '2023-02-09 16:29:26', b'0', 'STAFF2', 5),
(3, '2023-02-09 16:49:33', '2023-02-09 16:49:33', b'0', 'STAFF3', 8);

-- --------------------------------------------------------

--
-- Table structure for table `lecturer_roles`
--

CREATE TABLE `lecturer_roles` (
  `role_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  `lecturer_id` int(11) NOT NULL,
  `pres_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lecturer_roles`
--

INSERT INTO `lecturer_roles` (`role_id`, `created_at`, `updated_at`, `role_name`, `lecturer_id`, `pres_id`) VALUES
(1, '2023-02-09 16:37:28', '2023-02-09 16:37:28', 'Supervisor', 4, 1),
(2, '2023-02-09 16:38:29', '2023-02-09 16:38:29', 'Examiner', 5, 1),
(3, '2023-02-09 16:38:29', '2023-02-09 16:38:29', 'Chair', 8, 1),
(4, '2023-02-09 16:39:01', '2023-02-09 16:39:01', 'Supervisor', 5, 2),
(5, '2023-02-09 16:39:01', '2023-02-09 16:39:01', 'Chair', 8, 2),
(6, '2023-02-09 16:40:22', '2023-02-09 16:40:22', 'Examiner', 4, 2);

-- --------------------------------------------------------

--
-- Table structure for table `practicum_projects`
--

CREATE TABLE `practicum_projects` (
  `project_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `domain` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `host_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `practicum_projects`
--

INSERT INTO `practicum_projects` (`project_id`, `created_at`, `updated_at`, `description`, `domain`, `name`, `status`, `host_id`, `student_id`) VALUES
(1, '2023-02-09 16:40:40', '2023-02-09 16:40:40', 'This is the description.', 'This is the domain.', 'Project A', 'Open', 6, 1),
(2, '2023-02-09 16:40:40', '2023-02-09 16:40:40', 'This is the description.', 'This is the domain.', 'Project B', 'Open', 6, 2),
(4, '2023-02-09 16:42:33', '2023-02-09 16:42:33', 'This is the description.', 'This is the domain.', 'Project C', 'Closed', 7, 3);

-- --------------------------------------------------------

--
-- Table structure for table `presentations`
--

CREATE TABLE `presentations` (
  `pres_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `pres_identity` varchar(255) DEFAULT NULL,
  `time_slot` varchar(255) DEFAULT NULL,
  `venue` varchar(255) DEFAULT NULL,
  `csmind_id` int(11) NOT NULL,
  `host_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `presentations`
--

INSERT INTO `presentations` (`pres_id`, `created_at`, `updated_at`, `pres_identity`, `time_slot`, `venue`, `csmind_id`, `host_id`, `student_id`) VALUES
(1, '2023-02-09 16:36:21', '2023-02-09 16:36:21', 'P1', NULL, NULL, 1, 6, 1),
(2, '2023-02-09 16:36:21', '2023-02-09 16:36:21', 'P2', NULL, NULL, 1, 7, 2);

-- --------------------------------------------------------

--
-- Table structure for table `reports`
--

CREATE TABLE `reports` (
  `report_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `academic_session` varchar(255) DEFAULT NULL,
  `file_url` varchar(255) DEFAULT NULL,
  `is_final` bit(1) NOT NULL,
  `similarity_score` int(11) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `submission_date` date DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` int(11) NOT NULL,
  `student_id` int(11) NOT NULL,
  `type_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `reports`
--

INSERT INTO `reports` (`report_id`, `created_at`, `updated_at`, `academic_session`, `file_url`, `is_final`, `similarity_score`, `status`, `submission_date`, `title`, `category_id`, `student_id`, `type_id`) VALUES
(1, '2023-02-09 16:43:34', '2023-02-09 16:43:34', 'Academic Session 2022/2023 Semester 1', NULL, b'0', 12, NULL, '2023-02-27', 'Title A', 1, 1, 1),
(2, '2023-02-09 16:44:15', '2023-02-09 16:44:15', 'Academic Session 2022/2023 Semester 1', NULL, b'1', 1, NULL, '2023-02-27', 'Title A', 1, 1, 1),
(3, '2023-02-09 16:44:47', '2023-02-09 16:44:47', 'Academic Session 2022/2023 Semester 1', NULL, b'0', 1, NULL, '2023-02-28', 'Title B', 2, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `report_access`
--

CREATE TABLE `report_access` (
  `access_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `allow_access` bit(1) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `report_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `report_access`
--

INSERT INTO `report_access` (`access_id`, `created_at`, `updated_at`, `allow_access`, `role`, `report_id`, `user_id`) VALUES
(1, '2023-02-09 16:54:09', '2023-02-09 16:54:09', b'0', 'Student', 3, 1),
(2, '2023-02-09 16:54:09', '2023-02-09 16:54:09', b'1', 'Supervisor', 2, 4);

-- --------------------------------------------------------

--
-- Table structure for table `report_categories`
--

CREATE TABLE `report_categories` (
  `category_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `category_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `report_categories`
--

INSERT INTO `report_categories` (`category_id`, `created_at`, `updated_at`, `category_name`) VALUES
(1, '2023-02-09 16:32:01', '2023-02-09 16:32:01', 'Healthcare'),
(2, '2023-02-09 16:32:01', '2023-02-09 16:32:01', 'Financial');

-- --------------------------------------------------------

--
-- Table structure for table `report_types`
--

CREATE TABLE `report_types` (
  `type_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL
  `due_date` datetime DEFAULT NULL,
  `active` boolean DEFAULT NULL,

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `report_types`
--

INSERT INTO `report_types` (`type_id`, `created_at`, `updated_at`, `type`, `due_date`, `active`) VALUES
(1, '2023-02-09 16:31:10', '2023-02-09 16:31:10', 'Activity Plan', '2023-03-31 23:59:00', 1),
(2, '2023-02-09 16:31:10', '2023-02-09 16:31:10', 'Mid-Term Report', '2023-05-30 23:59:00', 0);

-- --------------------------------------------------------

--
-- Table structure for table `resumes`
--

CREATE TABLE `resumes` (
  `resume_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `about_me` varchar(255) DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `education` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `experience` varchar(255) DEFAULT NULL,
  `filename` varchar(255) DEFAULT NULL,
  `linkedin_link` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `skill` varchar(255) DEFAULT NULL,
  `student_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resumes`
--

INSERT INTO `resumes` (`resume_id`, `created_at`, `updated_at`, `about_me`, `contact`, `education`, `email`, `experience`, `filename`, `linkedin_link`, `name`, `reference`, `skill`, `student_id`) VALUES
(1, '2023-02-09 16:45:33', '2023-02-09 16:45:33', 'About Me', '011-2912938', 'Education', 'hkx@test.com', 'Experience', NULL, NULL, 'Heng Kai Xuan', 'Reference', 'Skills', 1),
(2, '2023-02-09 16:45:33', '2023-02-09 16:45:33', 'About Me', '012-1919281', 'Education', 'tyf@test.com', 'Experience', NULL, NULL, 'Teoh Yu Fei', 'Reference', 'Skills', 2);

-- --------------------------------------------------------

--
-- Table structure for table `students`
--

CREATE TABLE `students` (
  `id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `course_code` varchar(255) DEFAULT NULL,
  `matric_num` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `students`
--

INSERT INTO `students` (`id`, `created_at`, `updated_at`, `course_code`, `matric_num`, `user_id`) VALUES
(1, '2023-02-09 16:28:21', '2023-02-09 16:28:21', 'CDS590', '146499', 2),
(2, '2023-02-09 16:28:47', '2023-02-09 16:28:47', 'CDS590', '145813', 1),
(3, '2023-02-09 16:28:47', '2023-02-09 16:28:47', 'CDT594', '145827', 3);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varbinary(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `created_at`, `updated_at`, `email`, `name`, `password`, `role`) VALUES
(1, '2023-02-09 12:01:11', '2023-02-09 12:01:11', 'hkx@test.com', 'Heng Kai Xuan', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b4702000078700000000174000773747564656e74),
(2, '2023-02-09 12:01:25', '2023-02-09 12:01:25', 'tyf@test.com', 'Teoh Yu Fei', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b4702000078700000000174000773747564656e74),
(3, '2023-02-09 16:27:21', '2023-02-09 16:27:21', 'lsm@test.com', 'Liew Shek Mun', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b4702000078700000000174000773747564656e74),
(4, '2023-02-09 16:25:07', '2023-02-09 16:25:07', 'wlp@test.com', 'Dr Wong Li Pei', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b4702000078700000000274000561646d696e7400086c65637475726572),
(5, '2023-02-09 16:26:32', '2023-02-09 16:26:32', 'nasuha@test.com', 'Dr Nasuha Lee', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400086c65637475726572),
(6, '2023-02-09 16:26:46', '2023-02-09 16:26:46', 'amanda@test.com', 'Amanda', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b47020000787000000001740004686f7374),
(7, '2023-02-09 16:26:56', '2023-02-09 16:26:56', 'alinda@test.com', 'Alinda', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b47020000787000000001740004686f7374),
(8, '2023-02-09 16:38:25', '2023-02-09 16:38:25', 'fadratul@test.com', 'Dr Fadratul', '12345', 0xaced0005757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400086c65637475726572);

-- --------------------------------------------------------

--
-- Table structure for table `venues`
--

CREATE TABLE `venues` (
  `venue_id` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `unavailable_time` varbinary(255) DEFAULT NULL,
  `csmind_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `venues`
--

INSERT INTO `venues` (`venue_id`, `created_at`, `updated_at`, `name`, `unavailable_time`, `csmind_id`) VALUES
(1, '2023-02-09 16:34:08', '2023-02-09 16:34:08', 'Room A', NULL, 1),
(2, '2023-02-09 16:34:08', '2023-02-09 16:34:08', 'Room B', NULL, 1),
(3, '2023-02-09 16:34:42', '2023-02-09 16:34:42', 'Room C', NULL, 2),
(4, '2023-02-09 16:34:42', '2023-02-09 16:34:42', 'Room D', NULL, 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `constraints`
--
ALTER TABLE `constraints`
  ADD PRIMARY KEY (`constraint_id`),
  ADD KEY `FKllexlxkf8ill0m9cvi3jna97` (`csmind_id`),
  ADD KEY `FKf7r56kas3yx1hx2y713ud0gyq` (`user_id`);

--
-- Indexes for table `csminds`
--
ALTER TABLE `csminds`
  ADD PRIMARY KEY (`csmind_id`);

--
-- Indexes for table `hosts`
--
ALTER TABLE `hosts`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKim6h7w6syp1n0vvcrycxggs6x` (`user_id`);

--
-- Indexes for table `lecturers`
--
ALTER TABLE `lecturers`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKp8mocspquan2i6fjx36qghvct` (`user_id`);

--
-- Indexes for table `lecturer_roles`
--
ALTER TABLE `lecturer_roles`
  ADD PRIMARY KEY (`role_id`),
  ADD KEY `FK83b9cm9lmxwrxk67vcvhobik0` (`lecturer_id`),
  ADD KEY `FKjacup13yoxqci7aq7ldh7k3u8` (`pres_id`);

--
-- Indexes for table `practicum_projects`
--
ALTER TABLE `practicum_projects`
  ADD PRIMARY KEY (`project_id`),
  ADD KEY `FKrtjj21ny280pulythl37po0a5` (`host_id`),
  ADD KEY `FKalvyf4kjcwddf7w8ahnl3wb6b` (`student_id`);

--
-- Indexes for table `presentations`
--
ALTER TABLE `presentations`
  ADD PRIMARY KEY (`pres_id`),
  ADD KEY `FK7blh3o19fpo4wdjgp82vmvyy4` (`csmind_id`),
  ADD KEY `FKk81urtgsqvpdsg3kc3qk1by6k` (`host_id`),
  ADD KEY `FKo7xo3nq92lcv5d7978gpv6k7f` (`student_id`);

--
-- Indexes for table `reports`
--
ALTER TABLE `reports`
  ADD PRIMARY KEY (`report_id`),
  ADD KEY `FKfbhu3raban69qm5v3hogustwd` (`category_id`),
  ADD KEY `FKougny0aymdw7080j2h5bagnrj` (`student_id`),
  ADD KEY `FKsaqauy0j3pjgu6iv84c05v7we` (`type_id`);

--
-- Indexes for table `report_access`
--
ALTER TABLE `report_access`
  ADD PRIMARY KEY (`access_id`),
  ADD KEY `FK64d2ggcspdthndwnm0otwv8ta` (`report_id`),
  ADD KEY `FKrvybmga1d58wjrdnt6js2fbl7` (`user_id`);

--
-- Indexes for table `report_categories`
--
ALTER TABLE `report_categories`
  ADD PRIMARY KEY (`category_id`);

--
-- Indexes for table `report_types`
--
ALTER TABLE `report_types`
  ADD PRIMARY KEY (`type_id`);

--
-- Indexes for table `resumes`
--
ALTER TABLE `resumes`
  ADD PRIMARY KEY (`resume_id`),
  ADD KEY `FKf61wbcy88impqvsea6umid70j` (`student_id`);

--
-- Indexes for table `students`
--
ALTER TABLE `students`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdt1cjx5ve5bdabmuuf3ibrwaq` (`user_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `venues`
--
ALTER TABLE `venues`
  ADD PRIMARY KEY (`venue_id`),
  ADD KEY `FK4viw1akc69027b40m1mpvkbxn` (`csmind_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `constraints`
--
ALTER TABLE `constraints`
  MODIFY `constraint_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `csminds`
--
ALTER TABLE `csminds`
  MODIFY `csmind_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `hosts`
--
ALTER TABLE `hosts`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `lecturers`
--
ALTER TABLE `lecturers`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `lecturer_roles`
--
ALTER TABLE `lecturer_roles`
  MODIFY `role_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `practicum_projects`
--
ALTER TABLE `practicum_projects`
  MODIFY `project_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `presentations`
--
ALTER TABLE `presentations`
  MODIFY `pres_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `reports`
--
ALTER TABLE `reports`
  MODIFY `report_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `report_access`
--
ALTER TABLE `report_access`
  MODIFY `access_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `report_categories`
--
ALTER TABLE `report_categories`
  MODIFY `category_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `report_types`
--
ALTER TABLE `report_types`
  MODIFY `type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `resumes`
--
ALTER TABLE `resumes`
  MODIFY `resume_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `students`
--
ALTER TABLE `students`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `venues`
--
ALTER TABLE `venues`
  MODIFY `venue_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `constraints`
--
ALTER TABLE `constraints`
  ADD CONSTRAINT `FKf7r56kas3yx1hx2y713ud0gyq` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKllexlxkf8ill0m9cvi3jna97` FOREIGN KEY (`csmind_id`) REFERENCES `csminds` (`csmind_id`);

--
-- Constraints for table `hosts`
--
ALTER TABLE `hosts`
  ADD CONSTRAINT `FKim6h7w6syp1n0vvcrycxggs6x` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `lecturers`
--
ALTER TABLE `lecturers`
  ADD CONSTRAINT `FKp8mocspquan2i6fjx36qghvct` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `lecturer_roles`
--
ALTER TABLE `lecturer_roles`
  ADD CONSTRAINT `FK83b9cm9lmxwrxk67vcvhobik0` FOREIGN KEY (`lecturer_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKjacup13yoxqci7aq7ldh7k3u8` FOREIGN KEY (`pres_id`) REFERENCES `presentations` (`pres_id`);

--
-- Constraints for table `practicum_projects`
--
ALTER TABLE `practicum_projects`
  ADD CONSTRAINT `FKalvyf4kjcwddf7w8ahnl3wb6b` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKrtjj21ny280pulythl37po0a5` FOREIGN KEY (`host_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `presentations`
--
ALTER TABLE `presentations`
  ADD CONSTRAINT `FK7blh3o19fpo4wdjgp82vmvyy4` FOREIGN KEY (`csmind_id`) REFERENCES `csminds` (`csmind_id`),
  ADD CONSTRAINT `FKk81urtgsqvpdsg3kc3qk1by6k` FOREIGN KEY (`host_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKo7xo3nq92lcv5d7978gpv6k7f` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `reports`
--
ALTER TABLE `reports`
  ADD CONSTRAINT `FKfbhu3raban69qm5v3hogustwd` FOREIGN KEY (`category_id`) REFERENCES `report_categories` (`category_id`),
  ADD CONSTRAINT `FKougny0aymdw7080j2h5bagnrj` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `FKsaqauy0j3pjgu6iv84c05v7we` FOREIGN KEY (`type_id`) REFERENCES `report_types` (`type_id`);

--
-- Constraints for table `report_access`
--
ALTER TABLE `report_access`
  ADD CONSTRAINT `FK64d2ggcspdthndwnm0otwv8ta` FOREIGN KEY (`report_id`) REFERENCES `reports` (`report_id`),
  ADD CONSTRAINT `FKrvybmga1d58wjrdnt6js2fbl7` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `resumes`
--
ALTER TABLE `resumes`
  ADD CONSTRAINT `FKf61wbcy88impqvsea6umid70j` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `students`
--
ALTER TABLE `students`
  ADD CONSTRAINT `FKdt1cjx5ve5bdabmuuf3ibrwaq` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `venues`
--
ALTER TABLE `venues`
  ADD CONSTRAINT `FK4viw1akc69027b40m1mpvkbxn` FOREIGN KEY (`csmind_id`) REFERENCES `csminds` (`csmind_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
