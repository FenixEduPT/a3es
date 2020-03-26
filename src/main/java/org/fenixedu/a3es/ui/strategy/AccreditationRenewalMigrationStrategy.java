package org.fenixedu.a3es.ui.strategy;

public class AccreditationRenewalMigrationStrategy extends MigrationStrategy {

    @Override
    public String getProcessFolderName() {
        return "Apresentação do pedido";
    }

    @Override
    public String getCompetenceCoursesFolderIndex() {
        return "3.2.";
    }

    @Override
    public String getCompetenceCoursesFolderName() {
        return getCompetenceCoursesFolderIndex() + " Organização das Unidades Curriculares";
    }
}
