CREATE TYPE tipo_movimentacao AS ENUM ('ENTRADA', 'SAIDA');

CREATE TABLE movimentacao (
    id BIGSERIAL PRIMARY KEY,
    tipo tipo_movimentacao NOT NULL,
    usuario_id BIGINT,
    material_id BIGINT,
    quantidade INT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    CONSTRAINT fk_movimentacao_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuario (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_movimentacao_material FOREIGN KEY (material_id)
        REFERENCES material (id)
        ON DELETE CASCADE
);