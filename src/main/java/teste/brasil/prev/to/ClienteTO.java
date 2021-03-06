package teste.brasil.prev.to;

import teste.brasil.prev.entities.Cliente;

public class ClienteTO {

	private Integer idCliente;
	
	private String nome;
	
	private String email;
	
	private String senha;
	
	private String rua;
	
	private String cidade;
	
	private String bairro;
	
	private String cep;
	
	private String estado;

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Cliente toCliente() {
		Cliente cli = new Cliente();
		
		cli.setIdCliente(idCliente);
		cli.setNome(nome);
		cli.setEmail(email);
		cli.setSenha(senha);
		cli.setRua(rua);
		cli.setCidade(cidade);
		cli.setBairro(bairro);
		cli.setCep(cep);
		cli.setEstado(estado);
		
		return cli;
	}
}
