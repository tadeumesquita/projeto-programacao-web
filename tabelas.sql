-- Tabela: Curso
CREATE TABLE cursos (
  id   SERIAL,
  nome VARCHAR (50) NOT NULL,
  qtde INTEGER NOT NULL,
  CONSTRAINT pk_c_id PRIMARY KEY (id),
  CONSTRAINT uk_c_nome UNIQUE (nome)
);

-- Tabela: Disciplinas
CREATE TABLE disciplinas (
  id       SERIAL,
  fk_curso SERIAL,
  nome     VARCHAR (50) NOT NULL,
  semestre INTEGER NOT NULL,
  CONSTRAINT pk_d_id PRIMARY KEY (id),
  CONSTRAINT fk_d_fk_curso FOREIGN KEY (fk_curso) REFERENCES cursos (id)
);

-- Tabela: Planos de ensino
CREATE TABLE planos_ensino (
  id            SERIAL,
  fk_disciplina SERIAL,
  objetivo      VARCHAR (50),
  conteudo      TEXT,
  CONSTRAINT pk_pe_id PRIMARY KEY (id),
  CONSTRAINT fk_pe_fk_disciplina FOREIGN KEY (fk_disciplina) REFERENCES disciplinas (id)
);
