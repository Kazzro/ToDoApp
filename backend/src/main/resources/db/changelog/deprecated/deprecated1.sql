BEGIN;

CREATE TABLE IF NOT EXISTS public.notes
(
    pk_note_id uuid NOT NULL,
    fk_author_user_id uuid NOT NULL,
    created_at timestamp without time zone NOT NULL DEFAULT now(),
    titel text NOT NULL,
    content json NOT NULL,
    PRIMARY KEY (pk_note_id)
);

CREATE TABLE IF NOT EXISTS public.users
(
    pk_user_id uuid NOT NULL,
    username text NOT NULL,
    email character varying(60) NOT NULL,
    password character varying(50) NOT NULL,
    PRIMARY KEY (pk_user_id)
);

CREATE TABLE IF NOT EXISTS public.groups
(
    pk_group_id uuid NOT NULL,
    fk_owner_user_id uuid NOT NULL,
    name text,
    description character varying,
    PRIMARY KEY (pk_group_id)
);

CREATE TABLE IF NOT EXISTS public.group_notes
(
    fk_note_id uuid NOT NULL,
    fk_group_id uuid NOT NULL
);

CREATE TABLE IF NOT EXISTS public.note_correspondents
(
    fk_note_id uuid,
    fk_target_user_id uuid
);

CREATE TABLE IF NOT EXISTS public.group_members
(
    fk_member_user_id uuid,
    fk_group_id uuid
);

ALTER TABLE IF EXISTS public.notes
    ADD CONSTRAINT author FOREIGN KEY (fk_author_user_id)
    REFERENCES public.users (pk_user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.groups
    ADD CONSTRAINT owner FOREIGN KEY (fk_owner_user_id)
    REFERENCES public.users (pk_user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.group_notes
    ADD CONSTRAINT note_id FOREIGN KEY (fk_note_id)
    REFERENCES public.notes (pk_note_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;


ALTER TABLE IF EXISTS public.group_notes
    ADD CONSTRAINT group_id FOREIGN KEY (fk_group_id)
    REFERENCES public.groups (pk_group_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE CASCADE
    NOT VALID;


ALTER TABLE IF EXISTS public.note_correspondents
    ADD CONSTRAINT note_id FOREIGN KEY (fk_note_id)
    REFERENCES public.notes (pk_note_id) MATCH SIMPLE
    ON UPDATE CASCADE
    ON DELETE CASCADE
    NOT VALID;


ALTER TABLE IF EXISTS public.note_correspondents
    ADD CONSTRAINT target_id FOREIGN KEY (fk_target_user_id)
    REFERENCES public.users (pk_user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.group_members
    ADD CONSTRAINT group_id FOREIGN KEY (fk_group_id)
    REFERENCES public.groups (pk_group_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.group_members
    ADD CONSTRAINT member_id FOREIGN KEY (fk_member_user_id)
    REFERENCES public.users (pk_user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

END;
