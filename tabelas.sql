-- Tabela: Curso
CREATE TABLE `cursos` (
  `id`   SERIAL,
  `nome` VARCHAR (50) NOT NULL,
  `qtde` INTEGER NOT NULL,
  CONSTRAINT `pk_id_c` PRIMARY KEY (`id`),
  CONSTRAINT `uk_nome_c` UNIQUE (`nome`)
);

-- Tabela: Disciplinas
CREATE TABLE `disciplinas` (
  `id`       SERIAL,
  `fk_curso` SERIAL,
  `nome`     VARCHAR (50) NOT NULL,
  `semestre` INTEGER NOT NULL,
  CONSTRAINT `pk_id_d` PRIMARY KEY (`id`),
  CONSTRAINT `fk_id_d` FOREIGN KEY (`fk_curso`) REFERENCES `cursos` (`id`)
);

-- Tabela: Planos de ensino
CREATE TABLE `planos_ensino` (
  `id`            SERIAL,
  `fk_disciplina` SERIAL,
  `objetivo`      VARCHAR (50),
  `conteudo`      TEXT,
  CONSTRAINT `pk_id_p` PRIMARY KEY (`id`),
  CONSTRAINT `fk_id_p` FOREIGN KEY (`fk_disciplina`) REFERENCES `disciplinas` (`id`)
);
