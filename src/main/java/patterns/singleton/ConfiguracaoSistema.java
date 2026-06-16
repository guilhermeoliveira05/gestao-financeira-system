package patterns.singleton;

/*
  PADRÃO SINGLETON — ConfiguracaoSistema

  - Garante uma única instância global de configuração do sistema.
  - Usado por ContaPagar (e demais componentes) via getInstancia().
*/

public class ConfiguracaoSistema {

    private static volatile ConfiguracaoSistema instance;

    private String  urlApi;
    private Integer porta;


    private ConfiguracaoSistema() {
        this.urlApi = "https://api.sistemafinanceiro.com";
        this.porta  = 8080;
    }

   
    public static ConfiguracaoSistema getInstancia() {
        if (instance == null) {
            synchronized (ConfiguracaoSistema.class) {
                if (instance == null) {
                    instance = new ConfiguracaoSistema();
                    System.out.println("[SINGLETON] ConfiguracaoSistema criada: "
                            + instance.urlApi + ":" + instance.porta);
                }
            }
        }
        return instance;
    }

    public String getUrlApi() {
        return urlApi;
    }

    public void setUrlApi(String url) {
        this.urlApi = url;
        System.out.println("[SINGLETON] URL da API atualizada para: " + url);
    }


    public Integer getPorta() {
        return porta;
    }

    public void setPorta(Integer porta) {
        this.porta = porta;
    }

    @Override
    public String toString() {
        return "ConfiguracaoSistema{urlApi='" + urlApi + "', porta=" + porta + "}";
    }
}
