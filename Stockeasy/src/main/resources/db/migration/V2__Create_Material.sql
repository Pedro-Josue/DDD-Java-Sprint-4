CREATE TABLE material (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    categoria_id BIGINT,
    CONSTRAINT fk_material_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria (id)
        ON DELETE SET NULL
);