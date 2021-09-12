# tec502-pbl1-server

<p align="center">
  <img src="https://i.imgur.com/grOHu0a.gif" alt="Sample Text" width="600px" height="400px">
</p>

## 📖 Descrição do Projeto ##
> **Resolução do problema 1 do MI - Concorrência e Conectividade (TEC 502).**<br/><br/>
O projeto trata-se de um servidor (utilizando [ServerSocket](https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html)) que recebe requisições [HTTP](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Methods) no formato [JSON](https://www.json.org/json-en.html), e devolve respostas nesse mesmo formato. <br/>
Este servidor permite múltiplas conexões com os *clients*, onde cada conexão é processada por uma *thread* diferente. Existem dois tipos de **client**, um que emula um dispositivo que possui diversos sensores acoplados, e outro que seria utilizado pelos médicos para o monitoramento dos pacientes que estão com suspeita de COVID-19.

### ⛵ Navegação pelos projetos: ###
- \> Servidor
- [Emulador de Sensores](https://github.com/AllanCapistrano/tec502-pbl1-sensors)
- [Monitoramento de Pacientes](https://github.com/AllanCapistrano/tec502-pbl1-monitoring)

### 📂 Tecnologias utilizadas: ###
- [Java JDK 8](https://www.oracle.com/br/java/technologies/javase/javase-jdk8-downloads.html)

### 📦 Dependências: ###
- [JSON](https://www.json.org/json-en.html)

------------

## 💻 Como utilizar ##

### Através de uma IDE ###
Caso esteja utilizando alguma IDE, basta **executar o projeto**, por exemplo, utilizando o *NetBeans IDE 8.2* aperte o botão `F6`; <br/>

### Através do terminal ###
1. Se desejar executar o projeto utilizando o terminal, será necessário fazer o *build* do projeto, para isso, recomenda-se a utilização do [Apache Ant](https://ant.apache.org/);
2. Por exemplo, em uma distribuição **Linux** baseada em **Ubuntu**, com o terminal aberto digite:
```powershell
$ sudo apt install ant
```
3. Após instalar o [Apache Ant](https://ant.apache.org/), clone este projeto:
```powershell
$ git clone git@github.com:AllanCapistrano/tec502-pbl1-server.git
```
4. Com o terminal aberto no diretório do projeto, entre na *branch* que possui o script para realizar a *build*:
```powershell
$ git checkout linux-compile
```
5. Nessa *branch*, basta executar o [Apache Ant](https://ant.apache.org/) com o seguinte comando:
```powershell
$ ant
```

------------

## 👨‍💻 Autor ##

| [![Allan Capistrano](https://github.com/AllanCapistrano.png?size=100)](https://github.com/AllanCapistrano) |
| -----------------------------------------------------------------------------------------------------------|
| [Allan Capistrano](https://github.com/AllanCapistrano)                                                     |

<p>
    <h3>Onde me encontrar:</h3>
    <a href="https://github.com/AllanCapistrano">
        <img src="https://github.com/AllanCapistrano/AllanCapistrano/blob/master/assets/github-square-brands.png" alt="Github icon" width="5%">
    </a>
    &nbsp
    <a href="https://www.linkedin.com/in/allancapistrano/">
        <img src="https://github.com/AllanCapistrano/AllanCapistrano/blob/master/assets/linkedin-brands.png" alt="Linkedin icon" width="5%">
    </a> 
    &nbsp
    <a href="https://mail.google.com/mail/u/0/?view=cm&fs=1&tf=1&source=mailto&to=asantos@ecomp.uefs.br">
        <img src="https://github.com/AllanCapistrano/AllanCapistrano/blob/master/assets/envelope-square-solid.png" alt="Email icon" width="5%">
    </a>
</p>

------------

## ⚖️ Licença ##
[GPL-3.0 License](https://github.com/AllanCapistrano/tec502-pbl1-server/blob/main/LICENSE)
