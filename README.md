# POSITIONAL-READER
Um projeto simples para conversão de arquivo posicional, utilizando Java 8.

## GET STARTED

Primeiramente, a posição do atributo na classe Java indicará a posição do atributo no arquivo. Isso significa que, se no arquivo posicional, os primeiros 60 caracteres se referirem ao nome, o primeiro atributo com a anotação <b>@FieldReader</b> na classe Java deverá ser: nome.
<p><b>NÃO UTILIZE TIPOS PRIMITIVOS NA CLASSE QUE REPRESENTA O POSICIONAL</b></p>
<p>Estando os atributos organizados, devemos em seguida utilizar a anotação <b>@FieldReader</b> para marcar os atributos a serem extraídos e informar alguns parâmetros para que a extração aconteça de forma correta. 
Não serão considerados os atributos que não possuirem está marcação.</p>

### FORMATOS SUPORTADOS

String, Integer, Long, Double, Date, Boolean, List e Classes internas do projeto.

### CONFIGURANDO

Todos os atributos que representem um dado no posicional deverá receber a anotação <b>@FieldReader</b> e seus parâmetros, conforme o tipo de dado.
<p>String: parâmetro: length</p>
<p>Integer: parâmetro: length</p>
<p>Long: parâmetro: length</p>
<p>Double: parâmetros: length e precision</p>
<p>Date: parâmetros: length e datePattern</p>
<p>Boolean: parâmetro: length</p>
<p>List: Para o tipo lista temos dois cenários... a lista está contida no mesmo posicional ou em uma outra linha do posicional.</p>
<p><b> - Lista contida: a lista está contida no posicional.</b> Parâmetros: dataLocation = FieldReader.DataLocation.INNER, listType = SuaClasse.class, listSize = Tamanho da lista</p>
<p><b> - Lista: a lista não está contida no posicional.</b> Parâmetros: dataLocation = FieldReader.DataLocation.OUTER, listType = SuaClasse.class, dataKey = chave para obter a lista de posicional</p>

### EXEMPLO
O posicional: 
<p>JOÃO NASCIMENTO DIAS          14041956MARIA NASCIMENTO DIAS         12071955TESTE DO POSITIONAL </p>

A classe Java que representa o posicional acima:
```java
import br.com.wcf.annotation.FieldReader;

public class Pessoa {

	@FieldReader(length = 30)
	private String nome;

	@FieldReader(length = 8, datePattern = "ddMMyyyy")
	private Date nascimento;

	@FieldReader(dataLocation = FieldReader.DataLocation.OUTER, listType = Telefone.class, dataKey = "TELEFONES")
	private List<Telefone> telefones;

	@FieldReader(dataLocation = FieldReader.DataLocation.INNER, dataKey = "CONJUGE")
	private Conjuge conjuge;

	@FieldReader(length = 20)
	private String observacao;

        public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date teste2) {
		this.nascimento = teste2;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String teste) {
		this.nome = teste;
	}
	
	public static String getMockPessoa() {
		StringBuilder sb = new StringBuilder();
		sb.append("JOÃO NASCIMENTO DIAS          ");
		sb.append("14041956");
		sb.append("MARIA NASCIMENTO DIAS         ");
		sb.append("12071955");
		sb.append("TESTE DO POSITIONAL ");
		return sb.toString();
	}

	public static List<String> getMockTelefone(int qtd) {
		List<String> tels = new ArrayList<>();
		for (int i = 0; i < qtd; i++) {
			tels.add("01122228888");
		}
		return tels;
	}
```
A classe Java que utiliza o <b>posicional-reader</b>:

```java
import br.com.wcf.reader.PositionalReader;

public class Inicializar {

	public static void main(String[] args) {

		PositionalReader reader = new PositionalReader();
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("TELEFONES", Pessoa.getMockTelefone(3));

			System.out.println(Pessoa.getMockPessoa());
			Pessoa pessoa = reader.parse(Pessoa.class, Pessoa.getMockPessoa(), map);
			
			System.out.println(pessoa.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
```
