

| Tarefas | Descrição | Peso |
|-|-|-:|
| AWS | Configurar AWS | 5% |
| EKS | Disponibilizar a aplicação | 15% |
| Testes | Testes de carga | 20% |
| CI/CD | Jenkins | 10% |
| Custos | Análise de custos | 15% |
| PaaS | Plano de uso da plataforma | 15% |
| Apresentação | Storytelling | 20% |


## Configuração do AWS

A AWS é uma plataforma de computação em nuvem que oferece uma ampla gama de serviços, incluindo computação, armazenamento, banco de dados, análise, rede, mobilidade, ferramentas de desenvolvedor, gerenciamento e segurança. Para configurar a AWS, você precisará criar uma conta e configurar os serviços necessários para o seu projeto.

- [AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html){target="_blank"}

## Configuração do EKS

O Amazon Elastic Kubernetes Service (EKS) é um serviço gerenciado que facilita a execução do Kubernetes na AWS sem a necessidade de instalar e operar seu próprio plano de controle ou nós de trabalho do Kubernetes. O EKS cuida da alta disponibilidade e escalabilidade do plano de controle do Kubernetes, permitindo que você se concentre em implantar e gerenciar seus aplicativos.

- [EKS](https://docs.aws.amazon.com/eks/latest/userguide/getting-started.html){target="_blank"}

!!! danger "Custo de Uso"

    O custo de uso do EKS pode variar dependendo da região e dos serviços utilizados. É importante monitorar os custos e otimizar o uso dos recursos para evitar surpresas na fatura. Você pode usar a calculadora de preços da AWS para estimar os custos do seu projeto.

    **CUIDADO**: o tipo de instância EC2 é um dos principais fatores que afetam o custo do EKS. Instâncias maiores e mais poderosas custam mais, enquanto instâncias menores e menos poderosas custam menos. Além disso, o uso de recursos adicionais, como armazenamento em bloco e balanceadores de carga, também pode aumentar os custos[^1].


!!! info "TO DO"

    Faça um cluster EKS e faça o deploy da aplicação Spring Boot no cluster. Você pode usar o AWS CLI ou o console da AWS para criar e gerenciar seu cluster EKS.

    Para implementar a base de dados, você pode usar o Amazon RDS (Relational Database Service) ou o Amazon DynamoDB, dependendo das necessidades do seu projeto. 


## Testes de Carga

Os testes de carga são uma parte importante do desenvolvimento de software, pois ajudam a garantir que sua aplicação possa lidar com o tráfego esperado. Existem várias ferramentas disponíveis para realizar testes de carga, incluindo Apache JMeter, Gatling e Locust.

[Kubernetes - HPA - Increase the load](https://kubernetes.io/docs/tasks/run-application/horizontal-pod-autoscale-walkthrough/#increase-load){target="_blank"}

!!! info "TO DO"

    Faça um teste de carga na sua aplicação Spring Boot. Grave um video do teste de carga, mostrando:
    - O teste de carga em execução;
    - HPA (Horizontal Pod Autoscaler) em execução;
    

## CI/CD

A integração contínua (CI) e a entrega contínua (CD) são práticas de desenvolvimento de software que ajudam a garantir que seu código esteja sempre em um estado implantável. O Jenkins é uma ferramenta popular para implementar CI/CD em seus projetos.

!!! info "TO DO"

    Complemente seu pipeline de CI/CD de forma que após o push da imagem no Docker Hub, o Jenkins faça o deploy da imagem no EKS.

## Custos

A análise de custos é uma parte importante do desenvolvimento de software, pois ajuda a garantir que seu projeto esteja dentro do orçamento. Existem várias ferramentas disponíveis para ajudar na análise de custos, incluindo o AWS Cost Explorer[^2] e o AWS Budgets[^3].

!!! info "TO DO"

    Monte um plano de custos para o seu projeto, incluindo os custos de uso do EKS, RDS e outros serviços da AWS que você está utilizando. Use a calculadora de preços da AWS para estimar os custos do seu projeto.


## PaaS

A plataforma como serviço (PaaS) é um modelo de computação em nuvem que fornece uma plataforma para desenvolver, executar e gerenciar aplicativos sem a complexidade de construir e manter a infraestrutura normalmente associada ao desenvolvimento e lançamento de aplicativos.

!!! info "TO DO"

    Faça um plano para transformar sua aplicação em uma PaaS.


## Apresentação

A apresentação do seu projeto é uma parte importante do processo de desenvolvimento de software. É a oportunidade de mostrar seu trabalho e explicar como sua aplicação funciona. Use ferramentas como o PowerPoint ou o Google Slides para criar uma apresentação visualmente atraente. Se possível, faça uma demonstração ao vivo da sua aplicação para mostrar como ela funciona na prática, assim bem como um vídeo de apresentação do projeto.

!!! info "TO DO"

    Crie uma apresentação do seu projeto, incluindo os seguintes tópicos:
    - Introdução ao projeto;
    - Arquitetura do projeto;
    - Demonstração da aplicação;
    - Desafios enfrentados - bottlenecks;
    - Conclusão e próximos passos.

    O vídeo de apresentação deve ter entre 2 e 3 minutos e deve ser enviado junto com a documentação do projeto.



!!! danger "Entrega"

    Individualmente, cada aluno deve criar um repositório no GitHub, com a documentação em MkDocs dos exercícios realizados e também com o projeto e entrega o link via BlabkBoard. Na documentação publicada deve constar:

    - Nome do aluno e grupo;
    - Documentação das atividades realizadas;
    - Código fonte das atividades realizadas;
    - Documentação do projeto;
    - Código fonte do projeto;
    - Link para todos os repositórios utilizados;
    - Destaques para os bottlenecks implementados (ao menos 2 por indivíduo);
    - Apresentação do projeto;
    - Vídeo de apresentação do projeto (2-3 minutos);
    
    Um template de documentação pode ser encontrado em [Template de Documentação](https://hsandmann.github.io/documentation.template/){target="_blank"}.

[^1]: [AWS Pricing Calculator](https://calculator.aws/#/){target="_blank"}
[^2]: [AWS Cost Explorer](https://aws.amazon.com/aws-cost-management/aws-cost-explorer/){target="_blank"}
[^3]: [AWS Budgets](https://aws.amazon.com/aws-cost-management/aws-budgets/){target="_blank"}
