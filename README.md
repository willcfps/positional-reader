# POSITIONAL-READER
Um projeto simples para conversão de arquivo posicional, utilizando Java 8.

## GET STARTED

Primeiramente, a posição do atributo na classe Java indicará a posição do atributo no arquivo. Isso significa que, se no arquivo posicional, os primeiros 60 caracteres se referirem ao nome, o primeiro atributo com a anotação @FieldReader na classe Java deverá ser: nome.
Estando os atributos organizados, devemos em seguida utilizar a anotação @FieldReader para marcar os atributos a serem extraídos e informar alguns parâmetros para que a extração aconteça de forma correta. 
Não serão considerados os atributos que não possuirem está marcação.



