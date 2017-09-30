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


