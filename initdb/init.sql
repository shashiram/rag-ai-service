CREATE EXTENSION IF NOT EXISTS vector;

-- Table: public.chat_sessions

-- DROP TABLE IF EXISTS public.chat_sessions;

CREATE TABLE IF NOT EXISTS public.chat_sessions
(
    is_favorite boolean NOT NULL,
    created_at timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp(6) without time zone,
    id uuid NOT NULL,
    title character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT chat_sessions_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.chat_sessions
    OWNER to admin;

-- Table: public.chat_messages

-- DROP TABLE IF EXISTS public.chat_messages;

CREATE TABLE IF NOT EXISTS public.chat_messages
(
    created_at timestamp(6) without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id uuid NOT NULL,
    session_id uuid NOT NULL,
    content text COLLATE pg_catalog."default" NOT NULL,
    context text COLLATE pg_catalog."default",
    sender character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT chat_messages_pkey PRIMARY KEY (id),
    CONSTRAINT fk3cpkdtwdxndrjhrx3gt9q5ux9 FOREIGN KEY (session_id)
        REFERENCES public.chat_sessions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.chat_messages
    OWNER to admin;