CREATE DATABASE course_management;
USE course_management;
 CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password BINARY(60) NOT NULL
);

CREATE TABLE student (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNIQUE,
  name VARCHAR(255),
  department VARCHAR(255),
  enrollment_year YEAR,
  current_grade TINYINT,
  degree_program VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE teacher (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT UNIQUE,
  name VARCHAR(255),
  department VARCHAR(255),
  FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE course (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255),
  introduction TEXT,
  classroom varchar(50),
  day VARCHAR(50),
  start_time TIME,
  end_time TIME,
  teacher_id BIGINT,
  FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE SET NULL
);

CREATE TABLE course_student (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_id BIGINT,
  student_id BIGINT,
  FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
  FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);
