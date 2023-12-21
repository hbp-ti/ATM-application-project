package atm.ptda_atm;

class TheState {
        private double value;

    TheState(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
        public String toString() {
            return "Estado{" +
                    ", valor=" + value +
                    '}';
        }
}


class Services {
    private String entity;
    private double value;

    Services(String entity, double value) {
        this.value = value;
        this.entity = entity;
    }

    public double getValue() {
        return value;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public String toString() {
        return "Servico{" +
                "entidade='" + entity + '\'' +
                ", valor=" + value +
                '}';
    }
}