CREATE TABLE ferias (
    id SERIAL PRIMARY KEY,
    servidor_id INTEGER NOT NULL,
    status_id INTEGER NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE NOT NULL,
    dias INTEGER NOT NULL,
    observacao TEXT NOT NULL DEFAULT 'Pagamento feito 48h antes das férias',
    pag_ferias NUMERIC(10,2),

    CONSTRAINT fk_ferias_servidor FOREIGN KEY (servidor_id)
        REFERENCES servidores (id),
    CONSTRAINT fk_ferias_status FOREIGN KEY (status_id)
        REFERENCES status (id)
);
