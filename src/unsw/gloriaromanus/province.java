package unsw.gloriaromanus;

public class province {
    private static final int MAX_FAC = 2;

    private UnitFactory[] factories;

    public province(String unitConfig) {
        generateFactories(unitConfig);
        
    }

    private void generateFactories(String unitConfig) {
        factories = new UnitFactory[MAX_FAC];
        for (int i = 0; i < MAX_FAC; i++) {
            factories[i] = new UnitFactory(unitConfig);
        }
    }
}
