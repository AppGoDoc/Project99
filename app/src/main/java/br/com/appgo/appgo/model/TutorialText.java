package br.com.appgo.appgo.model;

import java.util.ArrayList;
import java.util.List;

public class TutorialText {
    private String titleTutorial;
    private String textTutorial;

    public TutorialText(String title, String text) {
        this.titleTutorial = title;
        this.textTutorial = text;
    }

    public TutorialText() {
    }

    public String getTitleTutorial() {
        return titleTutorial;
    }

    public void setTitleTutorial(String titleTutorial) {
        this.titleTutorial = titleTutorial;
    }

    public String getTextTutorial() {
        return textTutorial;
    }

    public void setTextTutorial(String textTutorial) {
        this.textTutorial = textTutorial;
    }

    public List<TutorialText> CreateTutorialText(){
        List<TutorialText> listTutorial = new ArrayList<>();
        listTutorial.add(new TutorialText(
                "Bem vindo ao AppGo!",
                "O App Go! é um app de Mídia Social que promove o Marketing Digital de empresas," +
                        " lojas, estabelecimentos comerciais e prestadores de serviços utilizando o " +
                        "mapa da região com a integração do uso de um Navegador e GPS onde permite " +
                        "as funcionalidades do Aplicativo em tempo real, oferecendo ao usuário, " +
                        "cliente ou consumidor que estiver parado ou em movimento ou em deslocamento " +
                        "ou enquanto dirige, viaja ou navega, a visualização, localização e geolocalização " +
                        "de pessoas, empresas, lojas, estabelecimentos comerciais e prestadores de " +
                        "serviços e todos estes que estiverem cadastrados no App Go! se apresentam " +
                        "através de ícones representados por seus logotipos ou a aparência desejada " +
                        "de se apresentarem aos usuários, clientes e consumidores do App Go! durante " +
                        "a navegação e através de um simples click no ícone ou logotipo desses " +
                        "cadastrados é possível conferir os produtos, bens ou serviços que eles oferecem" +
                        ", além de promoções, descontos, vantagens, benefícios, viagens ou transportes de " +
                        "uma maneira cujo foco principal é facilitar que o usuário ou cliente ou consumidor " +
                        "do App Go! encontre através destas pessoas, empresas, lojas, estabelecimentos comerciais" +
                        " e prestadores de serviços tudo aquilo que ele precisa, necessita ou deseja ou que " +
                        "estejam ao redor do usuário ou através da busca ou em uma distância relativamente " +
                        "próxima ou definida pelo usuário ou até mesmo fazendo a busca em outra cidade... " +
                        "O App Go! deseja que você cresça, apareça e prospere, o App Go! existe para mudar e" +
                        " inovar o modo de como você é visto, encontrado, localizado e lembrado pelos outros" +
                        " usuários do App Go! e de como você anuncia, divulga ou oferece algum produto, bem " +
                        "ou serviço, além do modo inovador, facilitador, curioso e divertido de como um " +
                        "usuário do App Go! se locomove ou quando vai as compras. "
        ));

//        listTutorial.add(new TutorialText(
//                "O que é o App Go!",
//                "O App Go! é um aplicativo de interesses pessoais, comerciais e financeiros onde é " +
//                        "permitido que qualquer pessoa, ramo de atividade ou estabelecimento comercial " +
//                        "ofereça seus produtos, bens ou serviços, além de promoções, descontos, vantagens, benefícios, viajens, " +
//                        "ou transportes sendo esse particular, de aplicativos ou de táxi e de uma maneira cujo o foco principal" +
//                        " é facilitar que um usuário do app encontre através destas pessoas, ramos de atividades ou estabelecimentos" +
//                        " comerciais tudo aquilo que ele precisa, necessita ou deseja, não importando tipo de ramo de atividade comercial e que esteja ao redor" +
//                        " do usuário através da busca ou em uma distância relativamente próxima ou definida pelo usuário e estas mesmas pessoas, ramos " +
//                        "de atividades e estabelecimentos comerciais se apresentam através de ícones representados por seus logotipos ou a aparência desejada"
//        ));
        listTutorial.add(new TutorialText(
                "Passo a Passo!",
                "Somente para você que quer anunciar no App Go!:\n" +
                        "\n" +
                        "Menu (alto da tela à esquerda) serve para:\n" +
                        "\n" +
                        "* Filtrar busca: escolha o ramo de atividade e faça busca de lojas, " +
                        "estabelecimentos comerciais e prestadores de serviços que estão ao seu redor, por KM ou por Cidade;\n" +
                        "\n" +
                        "* Compartilhe: compartilhe o App Go! com seus contatos de e-mail, WatsApp" +
                        " e redes sociais;\n" +
                        "\n" +
                        "* Login: faça login ou crie uma conta usando e-mail, Gmail ou Facebook;\n" +
                        "\n" +
                        "* Configuração: defina o tamanho do ícone que aparece no mapa, selecione o" +
                        " idioma e altere sua foto e seu nome de Perfil;\n" +
                        "\n" +
                        "* Perfil da loja: insira as informações de criação de seu cadastro: logotipo" +
                        ", endereço, fotos do seu estabelecimento, etc;\n" +
                        "\n" +
                        "* Criar anúncio: click no ícone de FOTO e insira fotos ou imagens e a " +
                        "descrição embaixo e crie até 10 colunas de 7 fotos cada.\n\n" +
                        "Dicas:\n" +
                        "*Se preferir, use as fotos ou imagens do seu Facebook e se precisar editar, use o editor " +
                        "de imagens da própria galeria do seu smartfone.\n" +
                        "*Você pode usar as fotos ou imagens de suas redes sociais.\n" +
                        "*Seja prático anunciando aquele produto, bem ou serviço que irá ficar " +
                        "em destaque ou evidencia ou que é mais procurado, mais pedido ou mais vendido."
        ));
        listTutorial.add(new TutorialText(
                "Porque usar o App Go!",
                "O App Go! é um aplicativo que oferece ao usuário ou consumidor o poder de compra! " +
                        " Através das comparações de preços, descontos e ofertas de produtos, bens e serviços" +
                        " anunciados pelos usuários cadastrados no App Go! você tem o poder de decisão da melhor " +
                        "compra, agregando valor, tempo e economia e você faz tudo isso antes mesmo de ir até " +
                        "aquela loja ou até aquele estabelecimento comercial. O App Go! proporciona uma" +
                        " inovação no modo em que o anunciante se apresenta aos usuários e consumidores" +
                        " através dos logotipos apresentados no mapa e em tempo real usando o navegador " +
                        "e GPS no seu smartfone.\n" +
                        "\n" +
                        "O App Go! irá proporcionar a você uma experiência real de tudo aquilo que você " +
                        "quer ver, saber e conhecer o que existe ao seu redor, é muito legal, é prático," +
                        " é curioso, é inovador.\n" +
                        "\n" +
                        "Tudo que você quer, precisa, ou deseja, você encontra no App Go!"
        ));
        listTutorial.add(new TutorialText(
                "O que te faz feliz? Não sabe? Olhe ao seu redor!\n" +
                        "\n" +
                        "Vamos começar?",
                ""
        ));
        return listTutorial;
    }
}
