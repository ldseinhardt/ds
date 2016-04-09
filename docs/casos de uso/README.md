# Casos de uso

**Desktop**

![Desktop](/docs/casos de uso/caso_de_uso_desktop.png)

1. **Gerenciar receitas**
  * Breve descritivo: Processo pelo qual o usuário gerencia suas receitas.
  * Pré-condições: Nenhuma.
  * Atores: Usuário.
  * Cenários principais: O usuário tem acesso a uma lista de receitas, onde esta pode ser vazia, com as opções de cadastrar, editar e remover. Para cadastrar e editar são disponibilizados ao usuário campos de informações como data, valor, categoria, conta bancária, descrição e informações adicionais. Já para exclusão é necessário exibir uma mensagem pedindo a confirmação do usuário para que a mesma ocorra.
  * Cenários alternativos: Informações não condizentes com o campo ou não preenchido; lista de receitas vazia. Informar isso ao usuário por meio de alguma mensagem.
  * Requisitos especiais: Listagem das categorias e contas para o usuário selecionar ao cadastrar ou editar alguma receita.
  * Observação: Devem ser previstos casos de usos cadastrar, editar e remover para gerenciar receitas.
2. **Gerenciar despesas**
  * Breve descritivo: Processo pelo qual o usuário gerencia suas despesas.
  * Pré-condições: Nenhuma.
  * Atores: Usuário.
  * Cenários principais: O usuário tem acesso a uma lista de despesas, onde esta pode ser vazia, com as opções de cadastrar, editar e remover. Para cadastrar e editar são disponibilizados ao usuário campos de informações como data, valor, categoria, conta bancária, descrição e informações adicionais. Já para exclusão é necessário exibir uma mensagem pedindo a confirmação do usuário para que a mesma ocorra.
  * Cenários alternativos: Informações não condizentes com o campo ou não preenchido; lista de despesas vazia. Informar isso ao usuário por meio de alguma mensagem.
  * Requisitos especiais: Listagem das categorias e contas para o usuário selecionar ao cadastrar ou editar alguma despesa.
  * Observação: Devem ser previstos casos de usos cadastrar, editar e remover para gerenciar despesas.
3. **Gerenciar contas bancárias**
  * Breve descritivo: Processo pelo qual o usuário gerencia suas contas bancárias para que cada conta possua suas receitas e despesas associadas.
  * Pré-condições: Nenhuma.
  * Atores: Usuário.
  * Cenários principais: O usuário tem acesso a uma lista de contas, onde esta não pode ser vazia, ou seja, deve haver pelo menos uma padrão, com as opções de cadastrar, editar e remover. Para cadastrar e editar são disponibilizados ao usuário campos de informações como nome da conta, saldo inicial, descrição e informações adicionais. Já para exclusão é necessário exibir uma mensagem pedindo a confirmação do usuário para que a mesma ocorra, sendo que deve haver pelo menos uma, ou seja, a última não pode ser excluída ou então ao excluir a última uma nova é criada automaticamente.
  * Cenários alternativos: Informações não condizentes com o campo ou não preenchido, portanto validar as mesmas.
  * Requisitos especiais: Nenhum.
  * Observação: Devem ser previstos casos de usos cadastrar, editar e remover para gerenciar contas bancárias.
4. **Importar dados**
  * Breve descritivo: Processo pelo qual o usuário importa seus dados, em formato de arquivo, para a versão desktop.
  * Pré-condições: Nenhuma.
  * Atores: Usuário.
  * Cenários principais: O usuário clica na opção de importar dados, escolhe o arquivo que contém todas as suas informações e envia para o sistema, que armazena e organiza todos os dados.
  * Cenários alternativos: Caso o usuário não tenha arquivo para importar, os dados serão inseridos manualmente. Caso ocorra erro durante a importação ou o arquivo a ser importado seja de formato fora do padrão, alguma mensagem de erro será exibida.
  * Requisitos especiais: Usuário possuir arquivo compatível contendo seus dados.
  * Observação: Nenhuma.
5. **Exportar dados**
  * Breve descritivo: Processo pelo qual o usuário exporta seus dados, em formato de arquivo, para alguma mídia física de armazenamento.
  * Pré-condições: Usuário possuir dados inseridos no sistema.
  * Atores: Usuário.
  * Cenários principais: O usuário escolhe a opção exportar dados, seleciona o local/pasta a ser salvo, confirma a operação e o arquivo final, contendo todos os dados, é gerado com sucesso. Formato de arquivo gerado será único, usuário não pode escolher algum outro formato para a exportação.
  * Cenários alternativos: Caso ocorra erro durante a exportação o arquivo não será gerado e alguma mensagem será exibida ao usuário.
  * Requisitos especiais: Usuário possuir dados no sistema.
  * Observação: Nenhuma
6. **Visualizar relatórios**
  * Breve descritivo: Processo pelo qual o usuário tem acesso a uma visualização compilada e interligada de seus dados, por meio de gráficos ou tabelas.
  * Pré-condições: Nenhuma.
  * Atores: Usuário.
  * Cenários principais: O usuário seleciona a opção de visualizar relatórios, dentro da qual já são pré-carregados relatórios padrão. O usuário pode selecionar um deles e escolher alguns filtros para a exibição dos dados, como período, categoria, movimentos consolidados ou futuros, etc. Caso o faça, o relatório é recarregado com as novas informações.
  * Cenários alternativos: Não há dados que correspondam à seleção feita pelo usuário, então é exibida alguma mensagem de aviso ao usuário.
  * Requisitos especiais: Nenhum.
  * Observação: Nenhuma.

**Web**

![Web](/docs/casos de uso/caso_de_uso_web.png)

1. **Download software desktop**
  * Breve descritivo: Processo pelo qual um usuário faz o download do software versão desktop.
  * Pré-condições: Não há pré-condições.
  * Atores: Usuário e Administrador.
  * Cenários principais: O Usuário/Administrador entra na pagina Web, e poderá realizar o download da versão desktop do software, sem a necessidade de realizar login.
  * Cenários alternativos: Nenhum.
  * Requisitos especiais: Nenhum.
  * Observação: Nenhuma.
2. **Login**
  * Breve descritivo: O usuário realiza a entrada no sistema web em seu espaço de usuário.
  * Pré-condições: Deve ser um usuário previamente cadastrado (ver caso de uso “Cadastro”).
  * Atores: Usuário e Administrador.
  * Cenários principais: Um usuário já cadastrado insere seu nome de usuário e sua senha. Este conjunto de entrada deve ser válido para permitir o ingresso no sistema.
  * Cenários alternativos: Um usuário tenta ingressar e não consegue, ou por ter errado, esquecido a senha ou o nome de usuário ou por ainda não ser cadastrado. O sistema informa que é possível recuperar a senha (ver caso de uso “Recuperar dados de login”) ou se cadastrar (ver caso de uso “Cadastro”).
  * Requisitos especiais: Se um administrador ingressa no sistema ele tem à disposição funcionalidades especiais (ver caso de uso “Gerenciar Usuários”).
  * Observação: Nenhuma.
3. **Logout**
  * Breve descritivo: O usuário realiza a saída do sistema web em seu espaço de usuário.
  * Pré-condições: Estar logado no sistema Web.
  * Atores: Usuário e Administrador.
  * Cenários principais: O Usuário/Administrador deseja o sair do sistema. Ao clicar em sair o sistema encerra as atividades, retornando para a tela de login.
  * Cenários alternativos: Usuário/Administrador fecha a janela do navegador, impossibilitando o sistema de realizar o Logout corretamente.
  * Requisitos especiais: Estar logado no sistema Web.
  * Observação:O Usuário/Administrador pode sair a qualquer momento do sistema web.
4. **Cadastro**
  * Breve descritivo: Processo que configura e atribui ao sistema um novo usuário.
  * Pré-condições: O nome de usuário do novo usuário não pode já existir no sistema.
  * Atores: Usuário.
  * Cenários principais: São solicitados nome de usuário e senha (deve conter no mínimo 4 dígitos e deve ser inserida em dois campos). Após a validação de nome de usuário e senha, o novo usuário é inserido no sistema e é feito automaticamente o seu login (ver caso de uso “Login”).
  * Cenários alternativos: O nome de usuário é igual ao de um usuário já existente; as senhas digitadas nos dois campos são diferentes. Em ambos os casos, é exibida uma mensagem solicitando alteração do dado digitado, informando o motivo correspondente.
  * Requisitos especiais: Nenhum.
  * Observação: Nenhuma.
5. **Recuperar dados de login**
  * Breve descritivo: o Usuário/Administrador perdem o login ou a senha do sistema.
  * Pré-condições: Tentar Entrar no Sistema.
  * Atores: Usuário e Administrador.
  * Cenários principais: Haverá um botão para recuperação dos mesmos, partindo do envio para um e-mail pré-cadastrado os dados necessários para a recuperação dos mesmos.
  * Cenários alternativos: O usuário não informou nenhum e-mail no momento do cadastro web.
  * Requisitos especiais: Ter previamente cadastrado no sistema o e-mail do usuário.
  * Observação: Nenhuma.
6. **Gerenciar perfil de usuário**
  * Breve descritivo: O Usuário/Administrador serão capazes de gerenciar seus próprios perfis através desta funcionalidade.
  * Pré-condições: Estar logado no Sistema.
  * Atores: Usuário e Administrador.
  * Cenários principais: O Usuário/Administrador deseja realizar alterações em seus dados cadastrais com os botões salvar, editar e remover usuário. Haverá um tela que listará todas suas informações já cadastradas e com o botão de editar tornará os campos editáveis para poder alterar a informação do campo desejado, posteriormente poderá salvar as alterações no botão salvar e caso deseje haverá a possibilidade de remover usuário, surgindo uma mensagem de confirmação para este último botão.
  * Cenários alternativos: Usuário/Administrador não deseja editar nenhum dado, logo poderá encerrar a gerência de perfil de usuário sem alterar nenhum dado.
  * Requisitos especiais: Estar logado no sistema.
  * Observação: Nenhuma.
7. **Enviar dados (upload)**
  * Breve descritivo: Ao clicar nessa opção os dados contidos no json local do usuário do sistema Desktop são enviados para o sistema Web.
  * Pré-condições: Usuário estar logado no sistema Web; automaticamente, o aplicativo valida o último upload (o que foi enviado pelo enviar dados).
  * Atores: Usuário e Administrador.
  * Cenários principais:  Usuário efetua o login no sistema Web; automaticamente o aplicativo valida o último upload e caso seja necessário novo upload gera um aviso  e abre uma tela para enviar os dados para o servidor Web.
  * Cenários alternativos: Usuário cadastrado não ter nenhum dado existente para enviar no servidor Web, uma mensagem deve ser enviada.
  * Requisitos especiais: Estar logado.
  * Observação: Nenhuma.
8. **Visualizar relatórios**
  * Breve descritivo: Processo de exibir ao usuário por meio de gráficos, relatórios a respeito dos dados disponíveis no sistema.
  * Pré-condições: Estar logado no sistema Web e ter validado o último upload (ver caso de uso “Validar último upload”).
  * Atores: Usuário e Administrador.
  * Cenários principais: O usuário visualiza por meio de gráficos informações a respeito das despesas de todos os usuários disponíveis no sistema. São exibidos ao usuário relatórios padrões, ao mesmo tempo que o usuário pode realizar filtros de período, região, média salarial ou categoria de despesas.
  * Cenários alternativos: Último upload não validado; não há nenhum dado cadastrado no sistema, ou por meio de um filtro não há dados. Em todos os casos deve ser exibida alguma mensagem ao usuário, sendo que no caso de upload não validado o usuário deve ser direcionado ao local onde pode realizar o upload.
  * Requisitos especiais: Nenhum.
  * Observação: Nenhuma.
9. **Validar último upload**
  * Breve descritivo: Processo em que o sistema verifica se a última data de upload está dentro do prazo determinado pelo sistema, para dispensar uma nova requisição de upload dos dados.
  * Pré-condições: Usuário ter selecionado um perfil de usuário no sistema Web, estar logado e ter clicado em visualizar relatórios (ver Caso de Uso “Visualizar Relatórios”).
  * Atores: Usuário e Administrador.
  * Cenários principais: Usuário logado no sistema Web clica em visualizar relatórios e então é verificada a data do último upload do seu perfil no sistema Desktop para o Web. Se a data do último upload for posterior ao prazo determinado pelo sistema a visualização dos relatórios é liberada (retorna ‘true’), caso contrário será requisitado um novo upload dos dados para a liberação da ação.
  * Cenários alternativos: Nenhum.
  * Requisitos especiais: Nenhum.
  * Observação: Nenhuma.
10. **Gerenciar usuários**
  * Breve descritivo: Processo pelo qual o Administrador do Sistema gerencia os usuários cadastrados na web.
  * Pré-condições: Usuário ter o privilégio de Administrador e ter a permissão.
  * Atores: Administrador.
  * Cenários principais: O administrador tem acesso a uma lista de usuários, onde terá acesso às opções de selecionar, modificar nível de acesso e remover. Para selecionar será apenas um clique em cima dos dados do usuário, habilitando assim as opções de modificar nível de acesso e remover. Para modificar nível de acesso, será disponibilizada uma lista de tipos de usuários para ser escolhido. Já para exclusão é necessário exibir uma mensagem pedindo a confirmação do administrador para que a mesma ocorra.
  * Cenários alternativos: O administrador tem acesso a uma lista de usuários, porém esta pode estar vazia.
  * Requisitos especiais: Possuir o privilégio de administrador.
  * Observação: Devem ser previstos casos de uso listar usuários, selecionar, modificar nível de acesso e remover, para gerenciar usuários.

Para edição dos casos de uso abra o arquivo `casos_de_uso.asta` na ferramenta astah (http://astah.net)
