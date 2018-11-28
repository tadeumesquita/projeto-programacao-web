-- Table: public.cursos

-- DROP TABLE public.cursos;

CREATE TABLE public.cursos
(
  id SERIAL,
  nome character varying(50) NOT NULL,
  qtde integer NOT NULL,
  CONSTRAINT pk_c_id PRIMARY KEY (id),
  CONSTRAINT uk_c_nome UNIQUE (nome)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.cursos
  OWNER TO postgres;


-- Table: public.disciplinas

-- DROP TABLE public.disciplinas;

CREATE TABLE public.disciplinas
(
  id SERIAL,
  fk_curso integer,
  nome character varying(50) NOT NULL,
  semestre integer NOT NULL,
  CONSTRAINT pk_d_id PRIMARY KEY (id),
  CONSTRAINT fk_d_fk_curso FOREIGN KEY (fk_curso)
      REFERENCES public.cursos (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.disciplinas
  OWNER TO postgres;


-- Table: public.planos_ensino

-- DROP TABLE public.planos_ensino;

CREATE TABLE public.planos_ensino
(
  id SERIAL,
  fk_disciplina integer,
  objetivo character varying(50),
  conteudo text,
  CONSTRAINT pk_pe_id PRIMARY KEY (id),
  CONSTRAINT fk_pe_fk_disciplina FOREIGN KEY (fk_disciplina)
      REFERENCES public.disciplinas (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.planos_ensino
  OWNER TO postgres;

/*

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
*/
