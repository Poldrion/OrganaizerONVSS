package organizer.utils;

public enum Menu {
    Main("Main data"),
    BusinessPlan("Business Plans"),
    Details("Ordering details"),
    TechnicalRequirement("Database Technical Requirements"),
    CategoryAndSubcategory("Categories and Subcategories"),
    Nomenclature("Nomenclature"),
    CodeKSM("Code KSM"),
    Settings("Settings window");

    private String title;

    Menu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getFxml() {
        return String.format("views/%s.fxml", name());
    }
}
