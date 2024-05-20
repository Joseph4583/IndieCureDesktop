-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 20-05-2024 a las 21:58:10
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `indiecuredb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `diagnostic`
--

CREATE TABLE `diagnostic` (
  `id` int(11) NOT NULL,
  `id_pacient` int(11) NOT NULL,
  `id_illness` int(11) DEFAULT 0,
  `is_confirmed` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `diagnostic`
--

INSERT INTO `diagnostic` (`id`, `id_pacient`, `id_illness`, `is_confirmed`) VALUES
(1, 2, 0, 0),
(2, 1, 1, 1),
(3, 5, 1, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dia_sin`
--

CREATE TABLE `dia_sin` (
  `id` int(11) NOT NULL,
  `id_diagnostic` int(11) NOT NULL,
  `id_symptom` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `dia_sin`
--

INSERT INTO `dia_sin` (`id`, `id_diagnostic`, `id_symptom`) VALUES
(50, 2, 1),
(51, 2, 9),
(52, 2, 5),
(53, 2, 4),
(54, 1, 1),
(59, 3, 9),
(60, 3, 5);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `doctor`
--

CREATE TABLE `doctor` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `specialization` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `doctor`
--

INSERT INTO `doctor` (`id`, `name`, `specialization`, `password`) VALUES
(1, 'Sergio', 'Admin', '123'),
(2, 'Irina', 'Cabezera', '1234'),
(3, 'Ricardo', 'Especial', '12345');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `illness`
--

CREATE TABLE `illness` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `severity` varchar(32) NOT NULL,
  `is_official` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `illness`
--

INSERT INTO `illness` (`id`, `name`, `severity`, `is_official`) VALUES
(0, 'NO DATA', 'NO DATA', 1),
(1, 'Cancer', 'Letal', 1),
(2, 'Gripe', 'Grave', 1),
(3, 'Laringitis', 'Leve', 1),
(4, 'Resfriado comun', 'Muy_leve', 1),
(5, 'Hipoglucemia', 'Muy_Grave', 1),
(6, 'Fatiga cronica', 'Muy_Grave', 1),
(7, 'Miositis', 'Muy_leve', 1),
(8, 'Neumonia', 'Moderado', 0),
(9, 'Verial', 'Letal', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ill_test`
--

CREATE TABLE `ill_test` (
  `id` int(11) NOT NULL,
  `id_illness` int(11) NOT NULL,
  `id_medical_test` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `ill_test`
--

INSERT INTO `ill_test` (`id`, `id_illness`, `id_medical_test`) VALUES
(39, 1, 1),
(40, 1, 2),
(41, 2, 3),
(42, 2, 2),
(43, 3, 2),
(44, 3, 7),
(45, 4, 1),
(46, 4, 4),
(47, 5, 8),
(48, 6, 5),
(49, 7, 6),
(50, 7, 1),
(51, 8, 3),
(52, 8, 4),
(53, 9, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `medical_test`
--

CREATE TABLE `medical_test` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `medical_test`
--

INSERT INTO `medical_test` (`id`, `name`) VALUES
(1, 'Biopsia'),
(2, 'Laringoscopia'),
(3, 'Frotis nasofaríngeo'),
(4, 'Radiografía de pecho'),
(5, 'Criterios de Fukuda'),
(6, 'Examen físico'),
(7, 'Diagnóstico clínico'),
(8, 'Pruebas de glucosa en sangre');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pacient`
--

CREATE TABLE `pacient` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `age` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pacient`
--

INSERT INTO `pacient` (`id`, `name`, `age`) VALUES
(1, 'Mario', 53),
(2, 'Luis', 23),
(3, 'Lewis', 71),
(4, 'Mariano', 31),
(5, 'Robert Garcia', 12),
(6, 'Luis Hernandez', 34);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `symptom`
--

CREATE TABLE `symptom` (
  `id` int(11) NOT NULL,
  `name` varchar(32) NOT NULL,
  `description` varchar(255) NOT NULL,
  `is_official` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `symptom`
--

INSERT INTO `symptom` (`id`, `name`, `description`, `is_official`) VALUES
(1, 'tos', 'toses mucho', 1),
(2, 'dolor de estomago', 'lelepancha', 1),
(3, 'migrañas', 'dolor de cabeza persistente', 1),
(4, 'mareos', 'estas borracho', 1),
(5, 'vomitos', 'no mas', 1),
(6, 'dolor de muscular', 'estas paralitico', 1),
(7, 'dolor de garganta', 'no podes hablar', 1),
(8, 'inflamaciones', 'estas infectado', 1),
(9, 'desmayos', 'te vas pal suelo', 1),
(10, 'dolor de cabeza', 'no puedes pensar', 1),
(11, 'Tarta', 'estas gordo', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sym_ill`
--

CREATE TABLE `sym_ill` (
  `id` int(11) NOT NULL,
  `id_illness` int(11) NOT NULL,
  `id_symptom` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `sym_ill`
--

INSERT INTO `sym_ill` (`id`, `id_illness`, `id_symptom`) VALUES
(37, 1, 1),
(38, 1, 3),
(39, 1, 9),
(40, 1, 5),
(41, 1, 2),
(42, 1, 7),
(43, 1, 8),
(44, 1, 4),
(45, 1, 6),
(46, 2, 1),
(47, 2, 7),
(48, 3, 7),
(49, 3, 8),
(50, 3, 1),
(51, 4, 1),
(52, 5, 4),
(53, 5, 5),
(54, 5, 9),
(55, 6, 10),
(56, 6, 6),
(57, 6, 2),
(58, 7, 6),
(59, 7, 8),
(60, 8, 1),
(61, 8, 7),
(62, 9, 6),
(63, 9, 3),
(64, 9, 7),
(65, 9, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `diagnostic`
--
ALTER TABLE `diagnostic`
  ADD PRIMARY KEY (`id`),
  ADD KEY `diagnostic_ibfk_1` (`id_illness`),
  ADD KEY `diagnostic_ibfk_2` (`id_pacient`);

--
-- Indices de la tabla `dia_sin`
--
ALTER TABLE `dia_sin`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dia_sin_ibfk_1` (`id_diagnostic`),
  ADD KEY `dia_sin_ibfk_2` (`id_symptom`);

--
-- Indices de la tabla `doctor`
--
ALTER TABLE `doctor`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `illness`
--
ALTER TABLE `illness`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `ill_test`
--
ALTER TABLE `ill_test`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ill_test_ibfk_1` (`id_illness`),
  ADD KEY `ill_test_ibfk_2` (`id_medical_test`);

--
-- Indices de la tabla `medical_test`
--
ALTER TABLE `medical_test`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `pacient`
--
ALTER TABLE `pacient`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `symptom`
--
ALTER TABLE `symptom`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `sym_ill`
--
ALTER TABLE `sym_ill`
  ADD PRIMARY KEY (`id`),
  ADD KEY `sym_ill_ibfk_1` (`id_illness`),
  ADD KEY `sym_ill_ibfk_2` (`id_symptom`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `dia_sin`
--
ALTER TABLE `dia_sin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT de la tabla `doctor`
--
ALTER TABLE `doctor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `ill_test`
--
ALTER TABLE `ill_test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT de la tabla `medical_test`
--
ALTER TABLE `medical_test`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `sym_ill`
--
ALTER TABLE `sym_ill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `diagnostic`
--
ALTER TABLE `diagnostic`
  ADD CONSTRAINT `diagnostic_ibfk_1` FOREIGN KEY (`id_illness`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `diagnostic_ibfk_2` FOREIGN KEY (`id_pacient`) REFERENCES `pacient` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `dia_sin`
--
ALTER TABLE `dia_sin`
  ADD CONSTRAINT `dia_sin_ibfk_1` FOREIGN KEY (`id_diagnostic`) REFERENCES `diagnostic` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `dia_sin_ibfk_2` FOREIGN KEY (`id_symptom`) REFERENCES `symptom` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `ill_test`
--
ALTER TABLE `ill_test`
  ADD CONSTRAINT `ill_test_ibfk_1` FOREIGN KEY (`id_illness`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `ill_test_ibfk_2` FOREIGN KEY (`id_medical_test`) REFERENCES `medical_test` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `sym_ill`
--
ALTER TABLE `sym_ill`
  ADD CONSTRAINT `sym_ill_ibfk_1` FOREIGN KEY (`id_illness`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `sym_ill_ibfk_2` FOREIGN KEY (`id_symptom`) REFERENCES `symptom` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
