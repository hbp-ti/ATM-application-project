package PTDA_ATM;

/**
 * Classe que representa um estado com um valor associado.
 */
class TheState {
        private double value;

    /**
     * Construtor que inicializa o estado com o valor fornecido.
     *
     * @param value O valor associado ao estado.
     */
    TheState(double value) {
        this.value = value;
    }

    /**
     * Obtém o valor associado ao estado.
     *
     * @return O valor do estado.
     */
    public double getValue() {
        return value;
    }

    /**
     * Converte o objeto para uma representação de string.
     *
     * @return Uma string representando o estado.
     */
    @Override
        public String toString() {
            return "Estado{" +
                    ", valor=" + value +
                    '}';
        }
}

/**
 * Classe que representa um serviço com uma entidade e um valor associado.
 */
class Services {
    private String entity;
    private double value;

    /**
     * Construtor que inicializa o serviço com a entidade e valor fornecidos.
     *
     * @param entity A entidade associada ao serviço.
     * @param value  O valor associado ao serviço.
     */
    Services(String entity, double value) {
        this.value = value;
        this.entity = entity;
    }

    /**
     * Obtém o valor associado ao serviço.
     *
     * @return O valor do serviço.
     */
    public double getValue() {
        return value;
    }

    /**
     * Obtém a entidade associada ao serviço.
     *
     * @return A entidade do serviço.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Converte o objeto para uma representação de string.
     *
     * @return Uma string representando o serviço.
     */
    @Override
    public String toString() {
        return "Servico{" +
                "entidade='" + entity + '\'' +
                ", valor=" + value +
                '}';
    }
}