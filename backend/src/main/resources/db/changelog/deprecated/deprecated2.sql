--liquibase formatted sql
--changeset lars:1

BEGIN;


CREATE TABLE IF NOT EXISTS public.notes
(
    pk_note_id uuid NOT NULL,
    fk_author_user_id uuid NOT NULL,
    fk_target uuid,
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

ALTER TABLE IF EXISTS public.notes
    ADD CONSTRAINT author FOREIGN KEY (fk_author_user_id)
    REFERENCES public.users (pk_user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;


ALTER TABLE IF EXISTS public.notes
    ADD CONSTRAINT target_user FOREIGN KEY (fk_target)
    REFERENCES public.users (pk_user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID;

END;

--changeset lars:2
INSERT INTO public.users (pk_user_id, username, email, password)
        VALUES
            ('1fd8e9c4-1b7f-4d59-b4a6-1b7f19401b9e', 'Leonard Nimoy', 'test@test.test', '$2a$10$n7fdqOtpaEAxnozM6tLlKuuN9NBIEdSjv00G7kIkm1qxX.4qjqQ9m'),
            ('66f1a556-d358-4599-8cd8-28d3812049a2', 'Jonathan Frakes', 'test2@test.test', '$2a$10$n7fdqOtpaEAxnozM6tLlKuuN9NBIEdSjv00G7kIkm1qxX.4qjqQ9m'),
            ('7f694f11-1a3a-4846-8b5c-1a3a1a8b5c8d', 'William Shatner', 'test3@test.test', '$2a$10$n7fdqOtpaEAxnozM6tLlKuuN9NBIEdSjv00G7kIkm1qxX.4qjqQ9m'),
            ('3e34b670-f832-4e5f-85a7-f832f8785a70', 'Patrick Stewart', 'test4@test.test', '$2a$10$n7fdqOtpaEAxnozM6tLlKuuN9NBIEdSjv00G7kIkm1qxX.4qjqQ9m');

--changeset lars:3
INSERT INTO public.notes (pk_note_id, fk_author_user_id, fk_target, created_at, titel, content)
        VALUES
            ('e0db994f-6cfb-4f76-9c1f-2d9261563bed', '1fd8e9c4-1b7f-4d59-b4a6-1b7f19401b9e', NULL, '2023-09-05 11:30:44.138208+00', 'test', '{"task": "test the self notes", "status": false}'),
            ('d4d1cfc0-f9e6-4088-b78b-26825c9fcdc4', '66f1a556-d358-4599-8cd8-28d3812049a2', '1fd8e9c4-1b7f-4d59-b4a6-1b7f19401b9e', '2023-09-05 11:51:18.880522+00', 'test from Jonathan Frakes to Leonard Nimoy', '{"task": "test the received note", "status": false}'),
            ('7e7446b8-6d3d-4da2-b7a0-4f676d3db7a0', '1fd8e9c4-1b7f-4d59-b4a6-1b7f19401b9e', '66f1a556-d358-4599-8cd8-28d3812049a2', '2023-09-05 11:51:18.880522+00', 'test from Leonard Nimoy to Jonathan Frakes', '{"task": "test the send notes", "status": true}'),
            ('b0a76d0f-b62f-41ff-b9e2-8b62f19fb9e6', '66f1a556-d358-4599-8cd8-28d3812049a2', NULL, '2023-09-05 13:02:36.228496+00', 'title', '{"task": "i did it", "status": false}'),
            ('31b4fb5a-4ee5-4632-950f-e54ee5a63249', '7f694f11-1a3a-4846-8b5c-1a3a1a8b5c8d', NULL, '2023-09-05 13:02:36.228496+00', 'get fit or die tryin', '[{"task": "gym", "status": true},{"task": "gym ", "status": false},{"task": "gym", "status": false}]'),
            ('b8aae362-0e8e-4d86-8406-e3620e8efbe7', '66f1a556-d358-4599-8cd8-28d3812049a2', '3e34b670-f832-4e5f-85a7-f832f8785a70', '2023-09-05 13:02:36.228496+00', 'lets do stuff', '[{"task": "do", "status": false},{"task": "stuff", "status": false},{"task": "now", "status": false}]'),
            ('2d5c9b36-d8c1-40f2-8f89-c1d8c140f2ed', '3e34b670-f832-4e5f-85a7-f832f8785a70', NULL, '2023-09-05 13:02:36.228496+00', 'i wanna be done with this...', '[{"task": "test this", "status": false},{"task": "test this", "status": true},{"task": "test that", "status": false}]');
















