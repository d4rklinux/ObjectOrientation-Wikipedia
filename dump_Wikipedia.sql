PGDMP       $                |         	   Wikipedia    16.1    16.0 S    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    17782 	   Wikipedia    DATABASE     �   CREATE DATABASE "Wikipedia" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = icu LOCALE = 'en_US.UTF-8' ICU_LOCALE = 'en-US';
    DROP DATABASE "Wikipedia";
                postgres    false            d           1247    17784    stato_proposta    TYPE     a   CREATE TYPE public.stato_proposta AS ENUM (
    'Accettata',
    'Rifiutata',
    'In attesa'
);
 !   DROP TYPE public.stato_proposta;
       public          postgres    false            �            1255    17924    after_insert_proposta_trigger()    FUNCTION     J  CREATE FUNCTION public.after_insert_proposta_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Chiamiamo la funzione di modifica direttamente dopo l'inserimento della proposta
    PERFORM public.modifica_diretta_frase(NEW.id_frase, NEW.nuovo_contenuto, NEW.username_utente_proposta);

    RETURN NEW;
END;
$$;
 6   DROP FUNCTION public.after_insert_proposta_trigger();
       public          postgres    false            �            1255    17917    after_update_proposta_trigger()    FUNCTION     �  CREATE FUNCTION public.after_update_proposta_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Se la proposta è accettata, aggiorna il contenuto della frase
    IF NEW.stato = 'Accettata' THEN
        UPDATE frase
        SET contenuto_frase = NEW.nuovo_contenuto
        WHERE id_frase = OLD.id_frase;  -- Utilizza l'ID della frase precedente (OLD)

       
    END IF;

    RETURN NEW;
END;
$$;
 6   DROP FUNCTION public.after_update_proposta_trigger();
       public          postgres    false            �            1255    17904    before_insert_utente()    FUNCTION     :  CREATE FUNCTION public.before_insert_utente() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM utente WHERE username_utente = NEW.username_utente) THEN
        RAISE EXCEPTION 'L''utente con username % è già registrato', NEW.username_utente;
    END IF;
    RETURN NEW;
END;
$$;
 -   DROP FUNCTION public.before_insert_utente();
       public          postgres    false            �            1255    17921 *   calcola_ratio_modifiche(character varying)    FUNCTION     =  CREATE FUNCTION public.calcola_ratio_modifiche(username_utente_input character varying) RETURNS numeric
    LANGUAGE plpgsql
    AS $$
DECLARE
    total_modifiche INT;
    modifiche_accettate INT;
    ratio DECIMAL(10,2);
BEGIN
    -- Calcola il totale delle modifiche fatte dall'utente
    SELECT COUNT(*) INTO total_modifiche
    FROM modifica
    WHERE username_utente = username_utente_input;

    -- Calcola il totale delle modifiche accettate dall'utente
    SELECT COUNT(*) INTO modifiche_accettate
    FROM modifica
    WHERE username_utente = username_utente_input AND stato = 'Accettata';

    -- Calcola il rapporto tra modifiche accettate e totale modifiche
    IF total_modifiche > 0 THEN
    ratio := modifiche_accettate::DECIMAL(10,2) / total_modifiche;
ELSE
    ratio := 0.0;
END IF;


    RETURN ratio;
END;
$$;
 W   DROP FUNCTION public.calcola_ratio_modifiche(username_utente_input character varying);
       public          postgres    false            �            1255    17922 /   calcola_totale_pagine_autore(character varying)    FUNCTION     y  CREATE FUNCTION public.calcola_totale_pagine_autore(username_autore_input character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    total_pagine INT;
BEGIN
    -- Calcola il totale delle pagine realizzate dall'autore
    SELECT COUNT(*) INTO total_pagine
    FROM pagina
    WHERE username_autore = username_autore_input;

    RETURN total_pagine;
END;
$$;
 \   DROP FUNCTION public.calcola_totale_pagine_autore(username_autore_input character varying);
       public          postgres    false            �            1255    17902    check_autore_per_pagina()    FUNCTION     ,  CREATE FUNCTION public.check_autore_per_pagina() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF (SELECT ruolo FROM utente WHERE username_utente = NEW.username_autore) != 'Autore' THEN
        RAISE EXCEPTION 'Solo gli autori possono creare pagine.';
    END IF;
    RETURN NEW;
END;
$$;
 0   DROP FUNCTION public.check_autore_per_pagina();
       public          postgres    false            �            1255    17909 :   check_vecchio_contenuto_exists(integer, character varying)    FUNCTION     �  CREATE FUNCTION public.check_vecchio_contenuto_exists(p_id_frase integer, p_vecchio_contenuto character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
    contenuto_attuale VARCHAR;
BEGIN
    -- Ottieni il contenuto attuale della frase
    SELECT contenuto_frase INTO contenuto_attuale
    FROM frase
    WHERE id_frase = p_id_frase;

    -- Restituisci true se il vecchio_contenuto corrisponde al contenuto attuale
    RETURN contenuto_attuale = p_vecchio_contenuto;
END;
$$;
 p   DROP FUNCTION public.check_vecchio_contenuto_exists(p_id_frase integer, p_vecchio_contenuto character varying);
       public          postgres    false            �            1255    17916 3   get_proposte_ordered_by_data_ora(character varying)    FUNCTION     �  CREATE FUNCTION public.get_proposte_ordered_by_data_ora(autore_destinatario character varying) RETURNS TABLE(id_proposta integer, vecchio_contenuto character varying, nuovo_contenuto character varying, stato public.stato_proposta, id_frase integer, ora_proposta time without time zone, data_proposta date, username_utente_proposta character varying, titolo_pagina character varying, autore_pagina character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT
        p.id_proposta,
        p.vecchio_contenuto,
        p.nuovo_contenuto,
        p.stato,
        p.id_frase,
        p.ora_proposta,
        p.data_proposta,
        p.username_utente_proposta,
        pa.titolo AS titolo_pagina,
        ua.username_utente AS autore_pagina
    FROM
        proposta AS p
    INNER JOIN frase AS f ON p.id_frase = f.id_frase
    INNER JOIN pagina AS pa ON f.id_pagina = pa.id_pagina
    INNER JOIN utente AS ua ON pa.username_autore = ua.username_utente
    -- Rimuovi la condizione di filtro sull'autore
    -- WHERE
    --     p.username_utente_proposta = autore_destinatario
    ORDER BY
        p.data_proposta ASC,
        p.ora_proposta ASC;
END;
$$;
 ^   DROP FUNCTION public.get_proposte_ordered_by_data_ora(autore_destinatario character varying);
       public          postgres    false    868            �            1255    17901 D   inserisci_titolo_e_frasi(character varying, text, character varying)    FUNCTION       CREATE FUNCTION public.inserisci_titolo_e_frasi(titolo_input character varying, testo_input text, username_autore_input character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
DECLARE
    id_pagina_new INT;
    id_frase_new INT;
    id_testo_new INT;
    frase_parti TEXT[];
    frase_parte TEXT;
BEGIN
    -- Inserisci il titolo nella tabella pagina
    INSERT INTO pagina (titolo, data_creazione, ora_creazione, username_autore)
    VALUES (titolo_input, CURRENT_DATE, CURRENT_TIME, username_autore_input)
    RETURNING id_pagina INTO id_pagina_new;

    -- Inserisci il testo associato alla pagina nella tabella testo
    INSERT INTO testo (id_pagina)
    VALUES (id_pagina_new)
    RETURNING id_testo INTO id_testo_new;

    -- Divide il testo in frasi usando sia il punto che il carattere di a capo come delimitatori
    frase_parti := regexp_split_to_array(testo_input, '[.\n]+');

    -- Rimuovi eventuali stringhe vuote dall'array
    frase_parti := array_remove(frase_parti, '');

    -- Inserisci ogni frase nella tabella frase
    FOREACH frase_parte IN ARRAY frase_parti
    LOOP
        -- Rimuovi eventuali spazi iniziali e finali
        frase_parte := trim(frase_parte);

        -- Inserisci la frase nella tabella frase
        INSERT INTO frase (contenuto_frase, versione, id_pagina, id_testo)
        VALUES (frase_parte, 1, id_pagina_new, id_testo_new)
        RETURNING id_frase INTO id_frase_new;

        -- Fai qualcos'altro con id_frase_new se necessario
    END LOOP;

    -- Esempio: Aggiungi un log delle frasi inserite
    RAISE NOTICE 'Inserite % frasi per la pagina %', array_length(frase_parti, 1), titolo_input;

    -- Esempio: Restituisci un messaggio di successo
    RAISE NOTICE 'Pagina "%", titolo "%", inserita con successo.', id_pagina_new, titolo_input;

END;
$$;
 �   DROP FUNCTION public.inserisci_titolo_e_frasi(titolo_input character varying, testo_input text, username_autore_input character varying);
       public          postgres    false            �            1255    17923 E   modifica_diretta_frase(integer, character varying, character varying)    FUNCTION     �  CREATE FUNCTION public.modifica_diretta_frase(p_id_frase integer, p_nuovo_contenuto character varying, p_username_autore character varying) RETURNS void
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Stampa di debug per controllare i valori dei parametri
    RAISE NOTICE 'modifica_diretta_frase - p_id_frase: %, p_nuovo_contenuto: %, p_username_autore: %', p_id_frase, p_nuovo_contenuto, p_username_autore;

    -- Verifica se l'utente è l'autore della pagina con quella frase
    IF EXISTS (
        SELECT 1
        FROM pagina AS p
        JOIN frase AS f ON p.id_pagina = f.id_pagina
        WHERE f.id_frase = p_id_frase AND p.username_autore = p_username_autore
    ) THEN
        -- Effettua la modifica dello stato della proposta
        UPDATE proposta
        SET stato = 'Accettata'
        WHERE id_frase = p_id_frase;
        
        -- Stampa di debug per confermare l'aggiornamento
        RAISE NOTICE 'Stato della proposta associata alla frase con id_frase % aggiornato a ''accettata''.', p_id_frase;
    END IF;
    
    -- Stampa di debug per confermare il completamento della funzione
    RAISE NOTICE 'modifica_diretta_frase completata';
    
END;
$$;
 �   DROP FUNCTION public.modifica_diretta_frase(p_id_frase integer, p_nuovo_contenuto character varying, p_username_autore character varying);
       public          postgres    false            �            1255    17919    post_update_proposta_trigger()    FUNCTION     B  CREATE FUNCTION public.post_update_proposta_trigger() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Inserisci una nuova riga nella tabella modifica
    INSERT INTO modifica (id_proposta, username_utente, stato)
    VALUES (NEW.id_proposta, NEW.username_utente_proposta, NEW.stato);

    RETURN NEW;
END;
$$;
 5   DROP FUNCTION public.post_update_proposta_trigger();
       public          postgres    false            �            1255    17910 $   trigger_elimina_proposte_in_attesa()    FUNCTION     
  CREATE FUNCTION public.trigger_elimina_proposte_in_attesa() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Se la proposta è stata accettata e lo stato precedente era "In attesa"
    IF NEW.stato = 'Accettata' AND COALESCE(OLD.stato, 'In attesa') = 'In attesa' THEN
        -- Qui eliminiamo tutte le proposte in attesa tranne quella accettata
        DELETE FROM proposta
        WHERE id_frase = NEW.id_frase AND stato = 'In attesa' AND id_proposta <> NEW.id_proposta;
    END IF;

    RETURN NEW;
END;
$$;
 ;   DROP FUNCTION public.trigger_elimina_proposte_in_attesa();
       public          postgres    false            �            1255    17914    trigger_incrementa_versione()    FUNCTION     �   CREATE FUNCTION public.trigger_incrementa_versione() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    NEW.versione := COALESCE(OLD.versione, 0) + 1;
    RETURN NEW;
END;
$$;
 4   DROP FUNCTION public.trigger_incrementa_versione();
       public          postgres    false            �            1255    17906 !   trigger_prevent_data_ora_futura()    FUNCTION     �  CREATE FUNCTION public.trigger_prevent_data_ora_futura() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF NEW.data_creazione > CURRENT_DATE THEN
        RAISE EXCEPTION 'Impossibile creare una pagina con data di creazione futura.';
    ELSIF NEW.data_creazione = CURRENT_DATE AND NEW.ora_creazione > CURRENT_TIME THEN
        RAISE EXCEPTION 'Impossibile creare una pagina con data e ora di creazione future.';
    END IF;
    RETURN NEW;
END;
$$;
 8   DROP FUNCTION public.trigger_prevent_data_ora_futura();
       public          postgres    false            �            1255    17913 ?   trova_id_frase_con_titolo(character varying, character varying)    FUNCTION     �  CREATE FUNCTION public.trova_id_frase_con_titolo(titolo_pagina character varying, vecchio_contenuto_frase character varying) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
    frase_id INT;
BEGIN
    SELECT f.id_frase INTO frase_id
    FROM frase f
    JOIN pagina p ON f.id_pagina = p.id_pagina
    WHERE p.titolo = titolo_pagina
        AND f.contenuto_frase = vecchio_contenuto_frase;

    RETURN frase_id;
END;
$$;
 |   DROP FUNCTION public.trova_id_frase_con_titolo(titolo_pagina character varying, vecchio_contenuto_frase character varying);
       public          postgres    false            �            1255    17932    verifica_formato_email()    FUNCTION     �  CREATE FUNCTION public.verifica_formato_email() RETURNS trigger
    LANGUAGE plpgsql
    AS $_$
BEGIN
    -- Verifica il formato dell'email
    IF NEW.email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$' THEN
        -- Se l'email è valida, restituisci la nuova riga per l'inserimento
        RETURN NEW;
    ELSE
        -- Se l'email non è valida, lancia un'eccezione
        RAISE EXCEPTION 'Il formato dell''email non è valido';
    END IF;
END;
$_$;
 /   DROP FUNCTION public.verifica_formato_email();
       public          postgres    false            �            1255    17930    verifica_formato_password()    FUNCTION     �  CREATE FUNCTION public.verifica_formato_password() RETURNS trigger
    LANGUAGE plpgsql
    AS $_$
BEGIN
    -- Verifica se la password ha almeno 6 caratteri, contiene almeno una lettera maiuscola, un numero o un simbolo
    IF LENGTH(NEW.password) >= 6 AND 
       (NEW.password ~ '[0-9]' OR NEW.password ~ '[!@#$%^&*()]') AND 
       (NEW.password ~ '[A-Z]')
    THEN
        -- Se la password è valida, restituisci la nuova riga per l'inserimento
        RETURN NEW;
    ELSE
        -- Se la password non è valida, lancia un'eccezione
        RAISE EXCEPTION 'La password deve contenere almeno 6 caratteri, almeno una lettera maiuscola, un numero o un simbolo speciale';
    END IF;
END;
$_$;
 2   DROP FUNCTION public.verifica_formato_password();
       public          postgres    false            �            1255    17908 .   visualizza_frasi_per_pagina(character varying)    FUNCTION     �  CREATE FUNCTION public.visualizza_frasi_per_pagina(titolo_input character varying) RETURNS TABLE(contenuto_frase character varying, versione integer, data_creazione date, ora_creazione time without time zone, autore_frase character varying)
    LANGUAGE plpgsql
    AS $$
BEGIN
    RETURN QUERY
    SELECT
        f.contenuto_frase,
        f.versione,
        p.data_creazione,
        p.ora_creazione,
        p.username_autore AS autore_frase
    FROM
        pagina p
    JOIN
        frase f ON p.id_pagina = f.id_pagina
    WHERE
        p.titolo = titolo_input
    ORDER BY
        f.id_frase; -- Ordina per id_frase invece che per versione
END;
$$;
 R   DROP FUNCTION public.visualizza_frasi_per_pagina(titolo_input character varying);
       public          postgres    false            �            1259    17873    collegamento    TABLE     �   CREATE TABLE public.collegamento (
    id_pagina integer NOT NULL,
    id_frase integer NOT NULL,
    url character varying(100) NOT NULL
);
     DROP TABLE public.collegamento;
       public         heap    postgres    false            �            1259    17821    frase    TABLE     �   CREATE TABLE public.frase (
    id_frase integer NOT NULL,
    contenuto_frase character varying,
    versione integer,
    id_pagina integer,
    id_testo integer
);
    DROP TABLE public.frase;
       public         heap    postgres    false            �            1259    17820    frase_id_frase_seq    SEQUENCE     �   CREATE SEQUENCE public.frase_id_frase_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.frase_id_frase_seq;
       public          postgres    false    221            �           0    0    frase_id_frase_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.frase_id_frase_seq OWNED BY public.frase.id_frase;
          public          postgres    false    220            �            1259    17858    modifica    TABLE     �   CREATE TABLE public.modifica (
    id_proposta integer NOT NULL,
    username_utente character varying(15) NOT NULL,
    stato public.stato_proposta NOT NULL
);
    DROP TABLE public.modifica;
       public         heap    postgres    false    868            �            1259    17797    pagina    TABLE     %  CREATE TABLE public.pagina (
    id_pagina integer NOT NULL,
    titolo character varying(100) NOT NULL,
    data_creazione date,
    ora_creazione time without time zone,
    username_autore character varying(15),
    CONSTRAINT chk_data_creazione CHECK ((data_creazione <= CURRENT_DATE))
);
    DROP TABLE public.pagina;
       public         heap    postgres    false            �            1259    17796    pagina_id_pagina_seq    SEQUENCE     �   CREATE SEQUENCE public.pagina_id_pagina_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 +   DROP SEQUENCE public.pagina_id_pagina_seq;
       public          postgres    false    217            �           0    0    pagina_id_pagina_seq    SEQUENCE OWNED BY     M   ALTER SEQUENCE public.pagina_id_pagina_seq OWNED BY public.pagina.id_pagina;
          public          postgres    false    216            �            1259    17840    proposta    TABLE     �  CREATE TABLE public.proposta (
    id_proposta integer NOT NULL,
    vecchio_contenuto character varying(255),
    nuovo_contenuto character varying(255),
    stato public.stato_proposta DEFAULT 'In attesa'::public.stato_proposta,
    id_frase integer,
    ora_proposta time without time zone,
    data_proposta date,
    username_utente_proposta character varying(15),
    CONSTRAINT chk_vecchio_nuovo_contenuto_different CHECK (((vecchio_contenuto)::text <> (nuovo_contenuto)::text))
);
    DROP TABLE public.proposta;
       public         heap    postgres    false    868    868            �            1259    17839    proposta_id_proposta_seq    SEQUENCE     �   CREATE SEQUENCE public.proposta_id_proposta_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.proposta_id_proposta_seq;
       public          postgres    false    223            �           0    0    proposta_id_proposta_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.proposta_id_proposta_seq OWNED BY public.proposta.id_proposta;
          public          postgres    false    222            �            1259    17809    testo    TABLE     T   CREATE TABLE public.testo (
    id_testo integer NOT NULL,
    id_pagina integer
);
    DROP TABLE public.testo;
       public         heap    postgres    false            �            1259    17808    testo_id_testo_seq    SEQUENCE     �   CREATE SEQUENCE public.testo_id_testo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.testo_id_testo_seq;
       public          postgres    false    219            �           0    0    testo_id_testo_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.testo_id_testo_seq OWNED BY public.testo.id_testo;
          public          postgres    false    218            �            1259    17791    utente    TABLE     �  CREATE TABLE public.utente (
    username_utente character varying(15) NOT NULL,
    password character varying(15) NOT NULL,
    ruolo character varying(6) NOT NULL,
    nome character varying(20) NOT NULL,
    cognome character varying(20) NOT NULL,
    email character varying(255),
    CONSTRAINT chk_ruolo CHECK (((ruolo)::text = ANY ((ARRAY['Autore'::character varying, 'Utente'::character varying])::text[])))
);
    DROP TABLE public.utente;
       public         heap    postgres    false            �           2604    17824    frase id_frase    DEFAULT     p   ALTER TABLE ONLY public.frase ALTER COLUMN id_frase SET DEFAULT nextval('public.frase_id_frase_seq'::regclass);
 =   ALTER TABLE public.frase ALTER COLUMN id_frase DROP DEFAULT;
       public          postgres    false    220    221    221            �           2604    17800    pagina id_pagina    DEFAULT     t   ALTER TABLE ONLY public.pagina ALTER COLUMN id_pagina SET DEFAULT nextval('public.pagina_id_pagina_seq'::regclass);
 ?   ALTER TABLE public.pagina ALTER COLUMN id_pagina DROP DEFAULT;
       public          postgres    false    217    216    217            �           2604    17843    proposta id_proposta    DEFAULT     |   ALTER TABLE ONLY public.proposta ALTER COLUMN id_proposta SET DEFAULT nextval('public.proposta_id_proposta_seq'::regclass);
 C   ALTER TABLE public.proposta ALTER COLUMN id_proposta DROP DEFAULT;
       public          postgres    false    223    222    223            �           2604    17812    testo id_testo    DEFAULT     p   ALTER TABLE ONLY public.testo ALTER COLUMN id_testo SET DEFAULT nextval('public.testo_id_testo_seq'::regclass);
 =   ALTER TABLE public.testo ALTER COLUMN id_testo DROP DEFAULT;
       public          postgres    false    219    218    219            �          0    17873    collegamento 
   TABLE DATA           @   COPY public.collegamento (id_pagina, id_frase, url) FROM stdin;
    public          postgres    false    225   F�       }          0    17821    frase 
   TABLE DATA           Y   COPY public.frase (id_frase, contenuto_frase, versione, id_pagina, id_testo) FROM stdin;
    public          postgres    false    221   Ǐ       �          0    17858    modifica 
   TABLE DATA           G   COPY public.modifica (id_proposta, username_utente, stato) FROM stdin;
    public          postgres    false    224   ��       y          0    17797    pagina 
   TABLE DATA           c   COPY public.pagina (id_pagina, titolo, data_creazione, ora_creazione, username_autore) FROM stdin;
    public          postgres    false    217   Ý                 0    17840    proposta 
   TABLE DATA           �   COPY public.proposta (id_proposta, vecchio_contenuto, nuovo_contenuto, stato, id_frase, ora_proposta, data_proposta, username_utente_proposta) FROM stdin;
    public          postgres    false    223   Y�       {          0    17809    testo 
   TABLE DATA           4   COPY public.testo (id_testo, id_pagina) FROM stdin;
    public          postgres    false    219   �       w          0    17791    utente 
   TABLE DATA           X   COPY public.utente (username_utente, password, ruolo, nome, cognome, email) FROM stdin;
    public          postgres    false    215   @�       �           0    0    frase_id_frase_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.frase_id_frase_seq', 84, true);
          public          postgres    false    220            �           0    0    pagina_id_pagina_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.pagina_id_pagina_seq', 3, true);
          public          postgres    false    216            �           0    0    proposta_id_proposta_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.proposta_id_proposta_seq', 2, true);
          public          postgres    false    222            �           0    0    testo_id_testo_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.testo_id_testo_seq', 3, true);
          public          postgres    false    218            �           2606    17877    collegamento collegamento_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_pkey PRIMARY KEY (url);
 H   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_pkey;
       public            postgres    false    225            �           2606    17828    frase frase_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.frase
    ADD CONSTRAINT frase_pkey PRIMARY KEY (id_frase);
 :   ALTER TABLE ONLY public.frase DROP CONSTRAINT frase_pkey;
       public            postgres    false    221            �           2606    17862    modifica modifica_pkey 
   CONSTRAINT     u   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_pkey PRIMARY KEY (id_proposta, username_utente, stato);
 @   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_pkey;
       public            postgres    false    224    224    224            �           2606    17802    pagina pagina_pkey 
   CONSTRAINT     W   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pagina_pkey PRIMARY KEY (id_pagina);
 <   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_pkey;
       public            postgres    false    217            �           2606    17847    proposta proposta_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.proposta
    ADD CONSTRAINT proposta_pkey PRIMARY KEY (id_proposta);
 @   ALTER TABLE ONLY public.proposta DROP CONSTRAINT proposta_pkey;
       public            postgres    false    223            �           2606    17814    testo testo_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.testo
    ADD CONSTRAINT testo_pkey PRIMARY KEY (id_testo);
 :   ALTER TABLE ONLY public.testo DROP CONSTRAINT testo_pkey;
       public            postgres    false    219            �           2606    17889    utente uk_email 
   CONSTRAINT     K   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT uk_email UNIQUE (email);
 9   ALTER TABLE ONLY public.utente DROP CONSTRAINT uk_email;
       public            postgres    false    215            �           2606    17935    pagina uk_titolo 
   CONSTRAINT     M   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT uk_titolo UNIQUE (titolo);
 :   ALTER TABLE ONLY public.pagina DROP CONSTRAINT uk_titolo;
       public            postgres    false    217            �           2606    17795    utente utente_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (username_utente);
 <   ALTER TABLE ONLY public.utente DROP CONSTRAINT utente_pkey;
       public            postgres    false    215            �           2620    17918    proposta after_update_proposta    TRIGGER     �   CREATE TRIGGER after_update_proposta AFTER UPDATE ON public.proposta FOR EACH ROW WHEN ((old.stato IS DISTINCT FROM new.stato)) EXECUTE FUNCTION public.after_update_proposta_trigger();
 7   DROP TRIGGER after_update_proposta ON public.proposta;
       public          postgres    false    223    249    223            �           2620    17912    pagina before_insert_pagina    TRIGGER     �   CREATE TRIGGER before_insert_pagina BEFORE INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.check_autore_per_pagina();
 4   DROP TRIGGER before_insert_pagina ON public.pagina;
       public          postgres    false    217    228            �           2620    17907 ,   pagina before_insert_prevent_data_ora_futura    TRIGGER     �   CREATE TRIGGER before_insert_prevent_data_ora_futura BEFORE INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.trigger_prevent_data_ora_futura();
 E   DROP TRIGGER before_insert_prevent_data_ora_futura ON public.pagina;
       public          postgres    false    245    217            �           2620    17931    utente before_insert_utente    TRIGGER     �   CREATE TRIGGER before_insert_utente BEFORE INSERT ON public.utente FOR EACH ROW EXECUTE FUNCTION public.verifica_formato_password();
 4   DROP TRIGGER before_insert_utente ON public.utente;
       public          postgres    false    215    231            �           2620    17933 !   utente before_insert_utente_email    TRIGGER     �   CREATE TRIGGER before_insert_utente_email BEFORE INSERT ON public.utente FOR EACH ROW EXECUTE FUNCTION public.verifica_formato_email();
 :   DROP TRIGGER before_insert_utente_email ON public.utente;
       public          postgres    false    232    215            �           2620    17905 #   utente before_insert_utente_trigger    TRIGGER     �   CREATE TRIGGER before_insert_utente_trigger BEFORE INSERT ON public.utente FOR EACH ROW EXECUTE FUNCTION public.before_insert_utente();
 <   DROP TRIGGER before_insert_utente_trigger ON public.utente;
       public          postgres    false    215    244            �           2620    17915    frase before_update_frase    TRIGGER     �   CREATE TRIGGER before_update_frase BEFORE UPDATE ON public.frase FOR EACH ROW EXECUTE FUNCTION public.trigger_incrementa_versione();
 2   DROP TRIGGER before_update_frase ON public.frase;
       public          postgres    false    230    221            �           2620    17920    proposta post_update_proposta    TRIGGER     �   CREATE TRIGGER post_update_proposta AFTER UPDATE ON public.proposta FOR EACH ROW WHEN ((old.stato IS DISTINCT FROM new.stato)) EXECUTE FUNCTION public.post_update_proposta_trigger();
 6   DROP TRIGGER post_update_proposta ON public.proposta;
       public          postgres    false    223    250    223            �           2620    17925 &   proposta trigger_after_insert_proposta    TRIGGER     �   CREATE TRIGGER trigger_after_insert_proposta AFTER INSERT ON public.proposta FOR EACH ROW EXECUTE FUNCTION public.after_insert_proposta_trigger();
 ?   DROP TRIGGER trigger_after_insert_proposta ON public.proposta;
       public          postgres    false    223    253            �           2620    17903 &   pagina trigger_check_autore_per_pagina    TRIGGER     �   CREATE TRIGGER trigger_check_autore_per_pagina BEFORE INSERT ON public.pagina FOR EACH ROW EXECUTE FUNCTION public.check_autore_per_pagina();
 ?   DROP TRIGGER trigger_check_autore_per_pagina ON public.pagina;
       public          postgres    false    228    217            �           2620    17911 +   proposta trigger_elimina_proposte_in_attesa    TRIGGER     �   CREATE TRIGGER trigger_elimina_proposte_in_attesa AFTER UPDATE ON public.proposta FOR EACH ROW EXECUTE FUNCTION public.trigger_elimina_proposte_in_attesa();
 D   DROP TRIGGER trigger_elimina_proposte_in_attesa ON public.proposta;
       public          postgres    false    227    223            �           2606    17883 '   collegamento collegamento_id_frase_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_id_frase_fkey FOREIGN KEY (id_frase) REFERENCES public.frase(id_frase) ON DELETE CASCADE;
 Q   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_id_frase_fkey;
       public          postgres    false    221    225    3532            �           2606    17878 (   collegamento collegamento_id_pagina_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.collegamento
    ADD CONSTRAINT collegamento_id_pagina_fkey FOREIGN KEY (id_pagina) REFERENCES public.pagina(id_pagina) ON DELETE CASCADE;
 R   ALTER TABLE ONLY public.collegamento DROP CONSTRAINT collegamento_id_pagina_fkey;
       public          postgres    false    225    3526    217            �           2606    17829    frase frase_id_pagina_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.frase
    ADD CONSTRAINT frase_id_pagina_fkey FOREIGN KEY (id_pagina) REFERENCES public.pagina(id_pagina) ON DELETE CASCADE;
 D   ALTER TABLE ONLY public.frase DROP CONSTRAINT frase_id_pagina_fkey;
       public          postgres    false    3526    217    221            �           2606    17834    frase frase_id_testo_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.frase
    ADD CONSTRAINT frase_id_testo_fkey FOREIGN KEY (id_testo) REFERENCES public.testo(id_testo) ON DELETE CASCADE;
 C   ALTER TABLE ONLY public.frase DROP CONSTRAINT frase_id_testo_fkey;
       public          postgres    false    221    219    3530            �           2606    17863 "   modifica modifica_id_proposta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_id_proposta_fkey FOREIGN KEY (id_proposta) REFERENCES public.proposta(id_proposta) ON DELETE CASCADE;
 L   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_id_proposta_fkey;
       public          postgres    false    224    223    3534            �           2606    17868 &   modifica modifica_username_utente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.modifica
    ADD CONSTRAINT modifica_username_utente_fkey FOREIGN KEY (username_utente) REFERENCES public.utente(username_utente) ON DELETE CASCADE;
 P   ALTER TABLE ONLY public.modifica DROP CONSTRAINT modifica_username_utente_fkey;
       public          postgres    false    215    3524    224            �           2606    17803 "   pagina pagina_username_autore_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.pagina
    ADD CONSTRAINT pagina_username_autore_fkey FOREIGN KEY (username_autore) REFERENCES public.utente(username_utente) ON DELETE CASCADE;
 L   ALTER TABLE ONLY public.pagina DROP CONSTRAINT pagina_username_autore_fkey;
       public          postgres    false    217    215    3524            �           2606    17848    proposta proposta_id_frase_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.proposta
    ADD CONSTRAINT proposta_id_frase_fkey FOREIGN KEY (id_frase) REFERENCES public.frase(id_frase) ON DELETE CASCADE;
 I   ALTER TABLE ONLY public.proposta DROP CONSTRAINT proposta_id_frase_fkey;
       public          postgres    false    3532    223    221            �           2606    17853 /   proposta proposta_username_utente_proposta_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.proposta
    ADD CONSTRAINT proposta_username_utente_proposta_fkey FOREIGN KEY (username_utente_proposta) REFERENCES public.utente(username_utente) ON DELETE CASCADE;
 Y   ALTER TABLE ONLY public.proposta DROP CONSTRAINT proposta_username_utente_proposta_fkey;
       public          postgres    false    215    3524    223            �           2606    17815    testo testo_id_pagina_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.testo
    ADD CONSTRAINT testo_id_pagina_fkey FOREIGN KEY (id_pagina) REFERENCES public.pagina(id_pagina) ON DELETE CASCADE;
 D   ALTER TABLE ONLY public.testo DROP CONSTRAINT testo_id_pagina_fkey;
       public          postgres    false    217    219    3526            �   q   x��ͱ
�0��9�$��hGG�A�	��^H�
>}����[~���3��⃒"�[Uދ�J8M�uUX���V�Y~�9�it�3��%��e���-����F頧'���3;      }   �  x��Y˒�]����TfR3�<��#�\r�d��v���I8�F�֬�)���mvY�O�%9�h�3Ne��U�4�s�}�y�vPv����h[���{�>�]�A9;l��n�W�Uc�۠�^?X?�;�]�X�7������!j<�[g��nM�Va��v�e�墅jw��*=�Xk���s��~��V��1jKM~��P
3�6�A5������b#�JJ0������p����O�][�'CgG3t���Ν޶ӡ{z�z��Ŏ���uE;���hZ��~
�gф1�X֌��5���4��߿{�^Lf�6X�s2��ju�&�I<��|��<�a�bEo�GqI��,�`q���6;�'�h���@�7�|��~oܝ�@3n[tG#a�5����=[6E���Fp!6�4�V��&f�;��<�>j±S��V�l������w=� ��k �\�j���oo�j�S+�?B�W�/��g�vV^=����������!�=��ζ�O�h�	���� ���6$�6#F�M��ux�%��G\3�V#�7���O��{�|C�{� �o�߶@��W��~my!Dc� l�A?�&�כ,4��TүEe����wK # �Ã�'���60]�_�h�v�/�?���W����hQ\#A����[�����ntk�PBPOS>�0�>:Nl�]����v<�[(g�4�̴��$}g�-�l�,���-��ZO��)�d�Pn���L��m��W�ˊW�z��,	�ɷ���P����,ѥE�����F���O"�^�b ��Ȁ3�1�hVn&�M�qGh��<h0D
�@�ឍ�C����^X�c�d 9�i��T�&;u�"��){#�v~JB�4~�vtk�a��=��j=PLz-8pOu��f
���f�ev §ц�;�Q�~䉉�P_�2m��՛r�����U�T^B�CΘ�a �@X�\b1��?p���k6%R��U2W��C�F	�	��B��A����}-1��!�Ԋ��9��a}�@R�`��U�DH��?��� R�)�;+F	de�ٯ�����8�����E��{'�L����4����3�Z����q=%�֗��I(�6AL���a�G	��m �;�N�;���dXD;?��8�������{W�L��N�mQ��N)p��`A�n�[���Yc�����C��u�f@l?�6�o��6Y�̣l�҅��R�
A\"x���D��L!��>������@r����)�A�#x�VR̏ �uO9Qܨ4�MH���NG|(e$��
Q�7 [��D�6(��v�x~����@󥒼�g\W�Y5o�����׸~yvvE=I�;�
`�kX������u5���Z��P��`ͦ�gp�#�T������*�E�N y�\ǔ#����g�\6w��Xt�E�'A�Z��mk�oM�Eί���_��PtL�5QE~������s`m��T�����G� ��̾��J. (V���U�d�d���"�XMfz	�³z�����XЙh��:�n�o+��i�h�}-�I6R�#�*��9���/���7͝�%vg�8\4���ƺ��U=��LW�:�ovD��J��[�z�;J7ǉ��`��%��9E�t�2H","�C]W��<k>U���l���ȕF�}J@v�����@:�0���PJ}�<�}����a�+;N��Y�#�\���2ѠBu�~lj�;R�eVX6�g�eS��@C�bLl[���@<#�� ?%R4챷p7�`:�b����I�;����|�jgN�'�Y��M��u�e��a�����>�j�M�'8��m�1p(� �#�ɋ��b+�Z�ڋ����S4]��wB[Ò
k�ɔ�U�b���P�����<����$4� �d�ϫ����K�"�L���T
�C��|%��T�Z�7�ib[jYR�ccT��N1��-ҳ�2���{>����4U3���I<�4Ȱ�F$6&��Z� ?b��O�IG�W(�Zf�����lK7� �	sY���j��t��E̯�jl�|`V�5E����W�f�.�Q��"�WT셝Z�	-���Fz���p�ޅ2���8,��#.�NC�i���ސ^DY��.U|^�7,H�H��bt���]�,O��[Db<R�S��b��QhvՑ�;zp;�qq�b�J�$�k��"0���N&�AT�E�s��V�櫪]}p����c�ԭ�@�U��f6)�Ip��8$� �Kd����X~�<+8��X��~�����J���9"A�%F�!���,6�Ta'��������{�~>�-]Q$��\6@���$Il6�UF]E�#1���̤��@�cX/A�~�ϻ��uv��l�@	(J_���)fDkni�
�����H�����Yf	9L�I���2Y�E�P����{)J����s�(�X=p[!��Q�:o2Q�*k5󤮶�Z�<ߞS"�L��:�U��]��I���$�����ٳ�\'��=dȫ+x�
�
u'(Z�n>Ks��/����٘8������_H��s�|�]|�d=�� f���w	�c`��M���(��e��y]/�m��Ú���IN@pyP�_���Y7�8�'�|}�x0�VP�:l@�h��FԹ�^� �p���,��U6��+5��R&dhޝ9�L8n�a���@	�!ZdסsU'�d��?�v�j��ճk)��[�N�7|��\Y������:p�U���9o/��F鑤���C�i�`�2��
)�AY�l-7�(��%��hd졞C�/c�T���U����_��c������@s8�� �����xiu�>�b�Z�S��kJB7�Y��8�̣/�H�P>ֹ�ύ�1B����$���TnAO����|g�5IF���r��q~��ܳ��s����sJ�Y~�"=����`�����둣���M;Hw�1$S���OH�v��.��3فGv��W �~"��Gy��5�qST<Ո�p6�>r���:�%y�lYfs�'Bޥ�ʸ1@��p�m炵�V('�0��hE�
8���gv� #�p�/:�;Z�ҴѨU��l�9�Y��'�?�N#�x����2��o���R}��w���[�.��*'�M�<��$�����<#���2m-�ͽ_y�`s��-S��2�^x�?��Җ��V2';Tz����񛴑�/
K0��nG��9k�����GH�S���J�Xyǖ�d�D�e���������u
���v���!:Ђo�i����=��h"����?���[NY���}m�q�d�S��xa#ɂ�Ȳ����Rr��D��G��~S�Äo4��Cyc�����P��P�27>(���^&?��]�@9k�������L�Ç��[JA	�L"�^��J@d��c����殼8�$	�9X�d�/���������$g��\�����I�5���U�=�XH�6���f������e��UHnT�����={��漨      �      x������ � �      y   �   x�e�1�0��+\B�e_���D<��X�I�8r
^HT m93��J҇��S)\��zi�4�'�9-�����ń!��X�����p�-��e���f@��V��.����H�.��s����(�^�U,         �   x�]�A
�@�uz�@ef� ݺ�'p� �NRfR��
.T���?���(s]2�6Y�*���78�V�})H��R�;�$��v2u��-�Y�ܹ�1u�Ѕ )�v���r��g�Qא��)�4���S15���EIד�}�IU����eĶk?��c_wMӼ xsL�      {      x�3�4�2�4�2�4����� A      w   �   x�m��
AD�����D��	�)�	$9�}�,N��&��<����6��iIs�������Q��#�ʚ���%5ƾ���xjAZ��F��­|�?��]��'�g���%r�g�Hf_A��a������L�     