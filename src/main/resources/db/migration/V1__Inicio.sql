CREATE TABLE clientes (
	idCliente SERIAL PRIMARY KEY, 
	nome VARCHAR(200) NOT NULL,
	email VARCHAR(200) UNIQUE NOT NULL,
	senha VARCHAR(200) NOT NULL,
	rua VARCHAR(200),
	cidade VARCHAR(200),
	bairro VARCHAR(200),
	cep VARCHAR(8),
	estado VARCHAR(2)
);

CREATE TABLE categorias (
	idCategoria SERIAL PRIMARY KEY, 
	categoria VARCHAR(200) NOT NULL
);

CREATE TABLE produtos (
	idProduto SERIAL PRIMARY KEY,
	idCategoria INT,
	produto VARCHAR(200) NOT NULL,
	preco NUMERIC(15,2) NOT NULL,
	quantidade INT,
	descricao VARCHAR(1000),
	foto VARCHAR(200),
	CONSTRAINT fkProdutosCategorias FOREIGN KEY (idCategoria)
    REFERENCES categorias (idCategoria) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE pedidos (
	idPedido SERIAL PRIMARY KEY,
	data TIMESTAMP NOT NULL,
	idCliente INT NOT NULL,
	status VARCHAR(200),
	sessao VARCHAR(200),
	CONSTRAINT fkPedidosClientes FOREIGN KEY (idCliente)
    REFERENCES clientes (idCliente) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE pedidoItens (
	idItem SERIAL PRIMARY KEY,
	idPedido INT NOT NULL,
	idProduto INT NOT NULL,
	produto VARCHAR(200) NOT NULL,
	quantidade INT NOT NULL,
	valor NUMERIC(15,2) NOT NULL,
	subtotal NUMERIC(15,2) NOT NULL,
	CONSTRAINT fkPedidosItensPedido FOREIGN KEY (idPedido)
    REFERENCES pedidos (idPedido) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT fkPedidosItensProduto FOREIGN KEY (idProduto)
    REFERENCES produtos (idProduto) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);


INSERT INTO clientes (nome, email, senha) VALUES ('Administrador', 'admin@teste.com', '$2a$11$BGIoqkaa3kPnFnxzO7p0E./QGbZ1BUfO4ccGPkk4O3EgtBK203U3G');

INSERT INTO categorias (categoria) VALUES ('Geral');

INSERT INTO produtos (idCategoria, produto, preco, quantidade, descricao, foto) VALUES (1, 'Produto A', 10.50, 1, '', '');
INSERT INTO produtos (idCategoria, produto, preco, quantidade, descricao, foto) VALUES (1, 'Produto B', 5.40, 1, '', '');
INSERT INTO produtos (idCategoria, produto, preco, quantidade, descricao, foto) VALUES (1, 'Produto C', 9.99, 1, '', '');







